package com.example.dynamic.config;

import com.example.dynamic.datasource.DynamicDataSource;
import com.example.dynamic.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

/**
 * @author linmf
 * @Description
 * @date 2020/11/29 21:15
 */
@Configuration
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    @Autowired
    public DynamicDataSourceTransactionManager(DynamicDataSource dynamicDataSource) {
        super(dynamicDataSource);
    }

    /**
     * 只读事务到读库，读写事务到写库
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        //获取事务的readOnly属性值
        boolean readOnly = definition.isReadOnly();
        if(readOnly) {
            DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB_SLAVE);
        } else {
            DynamicDataSourceHolder.setDataSource(DynamicDataSourceHolder.DB_MASTER);
        }
        super.doBegin(transaction, definition);
    }

    /**
     * 清理本地线程的数据源
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DynamicDataSourceHolder.clearDataSource();
    }
}
