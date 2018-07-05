package com.qc.itaojin.dao;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public interface ISqoopShellDao {

    /**
     * 根据schema和table，生成sqoop导入habase的脚本
     * */
    public String generateSqoopShell(String schema, String tableName);

}
