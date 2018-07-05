package com.qc.itaojin.service;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public interface IHiveSQLService {

    /**
     * 生成Hive关联HBase的建表语句
     * */
    public String generateDDL(String schema, String tableName);

}
