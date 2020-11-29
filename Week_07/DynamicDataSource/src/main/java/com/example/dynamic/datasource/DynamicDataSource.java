package com.example.dynamic.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author linmf
 * @Description
 * @date 2020/11/29 21:13
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 决定使用哪个数据源之前需要把多个数据源的信息以及默认数据源信息配置好
     *
     * @param defaultTargetDataSource 默认数据源
     * @param targetDataSources       目标数据源
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
    }

    /**
     * 获取读数据源方式，0：随机，1：轮询
     */
    private int readDataSourcePollPattern = 0;

    /**
     * 读数据源个数
     */
    private int slaveCount = 0;

    /**
     * 记录读库的key
     */
    private List<Object> slaveDataSources = new ArrayList<Object>(0);

    /**
     * 轮询计数,初始为0,AtomicInteger是线程安全的
     */
    private AtomicInteger counter = new AtomicInteger(0);

    /**
     * 每次操作数据库都会调用此方法，根据返回值动态选择数据源
     * 定义当前使用的数据源（返回值为动态数据源的key值）
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        //如果使用主库，则直接返回
        if (DynamicDataSourceHolder.isMaster()) {
            return DynamicDataSourceHolder.getDataSource();
        }
        int index = 0;
        //如果不是主库则选择从库
        if(readDataSourcePollPattern == 1) {
            //轮询方式
            index = getSlaveIndex();
        }
        else {
            //随机方式
            index = ThreadLocalRandom.current().nextInt(0, slaveCount);
        }
        log.info("选择从库索引："+index);
        return slaveDataSources.get(index);
    }

    /**
     * 该方法会在Spring Bean 加载初始化的时候执行，功能和 bean 标签的属性 init-method 一样
     * 把所有的slave库key放到slaveDataSources里
     */
    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        // 由于父类的resolvedDataSources属性是私有的子类获取不到，需要使用反射获取
        Field field = ReflectionUtils.findField(AbstractRoutingDataSource.class, "resolvedDataSources");
        // 设置可访问
        field.setAccessible(true);

        try {
            Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>) field.get(this);
            // 读库的数据量等于数据源总数减去写库的数量
            this.slaveCount = resolvedDataSources.size() - 1;
            for (Map.Entry<Object, DataSource> entry : resolvedDataSources.entrySet()) {
                if (DynamicDataSourceHolder.DB_MASTER.equals(entry.getKey())) {
                    continue;
                }
                slaveDataSources.add(entry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 轮询算法实现
     * @return
     */
    private int getSlaveIndex() {
        long currValue = counter.incrementAndGet();
        if (counter.get() > 9999) { //以免超出int范围
            counter.set(0); //还原
        }
        //得到的下标为：0、1、2、3……
        int index = (int)(currValue % slaveCount);
        return index;
    }

    public void setReadDataSourcePollPattern(int readDataSourcePollPattern) {
        this.readDataSourcePollPattern = readDataSourcePollPattern;
    }
}
