package com.example.dynamic.config;

import com.example.dynamic.datasource.DynamicDataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

/**
 * @author linmf
 * @Description
 * @date 2020/11/29 21:16
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicDataSourcePlugin implements Interceptor {
    /**
     * sql匹配规则
     */
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    /**
     * 进行拦截操作，增删改和事务操作使用master，查询使用slave，里面有具体的实现代码，感兴趣可以学习mybatis源码去理解
     * 你也可以根据自己的实际业务逻辑去控制
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = null;
        Object[] objects = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) objects[0];
        String lookupKey = DynamicDataSourceHolder.DB_MASTER;
        try {
            //是否使用事务管理
            boolean syschronizationActive = TransactionSynchronizationManager.isActualTransactionActive();
            if (!syschronizationActive) {
                //读方法
                if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                    //如果selectKey为自增id查询主键（SELECT LAST INSERT_ID）方法，使用主库
                    if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        lookupKey = DynamicDataSourceHolder.DB_MASTER;
                    } else {
                        BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(objects[1]);
                        String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                        //判断是否为“增删改”
                        if (sql.matches(REGEX)) {
                            lookupKey = DynamicDataSourceHolder.DB_MASTER;
                        } else {
                            lookupKey = DynamicDataSourceHolder.DB_SLAVE;
                        }
                    }
                }
            } else {
                //说明：如果启用事务管理器，那么这里就无法再修改数据源了，因为一旦启用事务时就确定了数据源(除非在自定义JDBC事务管理器类中重写doBegin方法来动态选择数据源)
                lookupKey = DynamicDataSourceHolder.DB_MASTER;
            }
            System.out.println("设置方法:"+mappedStatement.getId()+"; use:"+lookupKey+"; SqlCommanType:"+mappedStatement.getSqlCommandType().name());
            DynamicDataSourceHolder.setDataSource(lookupKey);
            result = invocation.proceed();
        }
        catch (Throwable ex) {
            log.error(String.format("Choose DataSource error, method:%s, msg:%s", mappedStatement.getId(), ex.getMessage()));
        }
        finally {
            DynamicDataSourceHolder.clearDataSource();
        }
        return result;
    }

    /**
     * 设置拦截对象
     * Executor在mybatis中是用来增删改查的，进行拦截
     * @param target 拦截的对象
     * @return
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {}
}

