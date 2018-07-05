package com.qc.itaojin.dao;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public interface IHiveDDLFromAliyunDao {

    /**
     * 从阿里云数据库中读取表结构信息，并且生成HIve的DDL语句
     * */
    public String generateHiveDDL(String schema, String tableName);

}
