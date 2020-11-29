package com.example.dynamic.datasource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author linmf
 * @Description
 * @date 2020/11/29 21:12
 */
@Slf4j
public class DynamicDataSourceHolder {
    /**
     * 线程安全，记录当前线程的数据源key
     */
    private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    /**
     * 主库，只允许一个
     */
    public static final String DB_MASTER = "master";
    /**
     * 从库，允许多个
     */
    public static final String DB_SLAVE = "slave";

    /**
     * 获取当前线程的数据源
     * @return
     */
    public static String getDataSource() {
        String db = contextHolder.get();
        if(db == null) {
            //默认是master库
            db = DB_MASTER;
        }
        log.info("所使用的数据源为：" + db);
        return db;
    }

    /**
     * 设置当前线程的数据源
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    /**
     * 清理连接类型
     */
    public static void clearDataSource() {
        contextHolder.remove();
    }

    /**
     * 判断是否是使用主库，提高部分使用
     * @return
     */
    public static boolean isMaster() {
        return DB_MASTER.equals(getDataSource());
    }
}
