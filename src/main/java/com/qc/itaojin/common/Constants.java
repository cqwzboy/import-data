package com.qc.itaojin.common;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public class Constants {

    /**
     * 物理schema
     * */
    public static final String SCHEMA = "tjk";

    /**
     * Hive相关的常量
     * */
    public interface HiveConstants{
        /**
         * 默认数据库
         * */
        String DEFAULT_SCHEMA = "default";
    }

    /**
     * HBase相关常量
     * */
    public interface HBaseConstants{
        /**
         * 默认列族
         * */
        String DEFAULT_FAMILY = "f1";

        /**
         * HBase默认Versions数量
         * */
        int DEFAULT_VERSIONS = 50;
    }

}
