package com.qc.itaojin.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by fuqinqin on 2018/6/25.
 */
public enum MysqlDataType {
    BIT("bit"),
    TINYINT("tinyint"),
    SMALLINT("smallint"),
    MEDIUMINT("mediumint"),
    INT("int"),
    BIGINT("bigint"),

    FLOAT("float"),
    DOUBLE("double"),
    DECIMAL("decimal"),

    YEAR("year"),
    TIME("time"),
    DATE("date"),
    DATETIME("datetime"),
    TIMESTAMP("timestamp"),

    TINYTEXT("tinytext"),
    TEXT("text"),
    MEDIUMTEXT("mediumtext"),
    LONGTEXT("longtext"),

    CHAR("char"),
    VARCHAR("varchar"),

    BINARY("binary"),
    VARBINARY("varbinary"),
    BLOG("blog"),
    TINYBLOG("tinyblog"),
    MEDIUMBLOG("mediumblog"),
    LONGBLOG("longblog")
    ;

    private String text;

    MysqlDataType(String text) {
        this.text = text;
    }

    public static MysqlDataType nameOf(String name){
        if(StringUtils.isBlank(name)){
            return null;
        }

        name = name.toUpperCase();

        for (MysqlDataType mysqlDataType : MysqlDataType.values()) {
            if(mysqlDataType.name().equals(name)){
                return mysqlDataType;
            }
        }

        return null;
    }

    public static boolean exists(MysqlDataType type){
        if(type == null){
            return false;
        }

        for (MysqlDataType mysqlDataType : MysqlDataType.values()) {
            if(mysqlDataType.equalsTo(type)){
                return true;
            }
        }

        return false;
    }

    public boolean equalsTo(MysqlDataType type){
        if(type == null){
            return false;
        }

        if(type == this){
            return true;
        }

        if(type.text.equals(this.text) && type.name().equals(this.name())){
            return true;
        }

        return false;
    }

    public static String transToHiveDataType(MysqlDataType type){
        if(type == null){
            return "string";
        }

        if(type.equalsTo(MysqlDataType.TINYINT)){
            return "tinyint";
        }

        if(type.equalsTo(MysqlDataType.SMALLINT)){
            return "smallint";
        }

        if(type.equalsTo(MysqlDataType.INT) || type.equalsTo(MysqlDataType.YEAR) || type.equalsTo(MysqlDataType.MEDIUMINT)
                || type.equalsTo(MysqlDataType.BIT)){
            return "int";
        }

        if(type.equalsTo(MysqlDataType.BIGINT)){
            return "bigint";
        }

        if(type.equalsTo(MysqlDataType.FLOAT)){
            return "float";
        }

        if(type.equalsTo(MysqlDataType.DOUBLE) || type.equalsTo(MysqlDataType.DECIMAL)){
            return "double";
        }

        // hive v0.8.0 + 才支持的数据类型
        if(type.equalsTo(MysqlDataType.TIME) || type.equalsTo(MysqlDataType.DATE)
                || type.equalsTo(MysqlDataType.DATETIME)
                || type.equalsTo(MysqlDataType.TIMESTAMP)){
            return "timestamp";
        }

        /*if(type.equalsTo(MysqlDataType.TINYTEXT) || type.equalsTo(MysqlDataType.TEXT)
                || type.equalsTo(MysqlDataType.MEDIUMTEXT)
                || type.equalsTo(MysqlDataType.LONGTEXT)
                || type.equalsTo(MysqlDataType.CHAR)
                || type.equalsTo(MysqlDataType.VARCHAR)){
            return "string";
        }*/

        // hive v0.8.0 + 才支持的数据类型
        if(type.equalsTo(MysqlDataType.BINARY) || type.equalsTo(MysqlDataType.VARBINARY)
                || type.equalsTo(MysqlDataType.BLOG)
                || type.equalsTo(MysqlDataType.TINYBLOG)
                || type.equalsTo(MysqlDataType.MEDIUMBLOG)
                || type.equalsTo(MysqlDataType.LONGBLOG)){
            return "binary";
        }

        return "string";
    }
}
