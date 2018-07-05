package com.qc.itaojin.dao.common;

import com.qc.itaojin.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by fuqinqin on 2018/6/25.
 */
public class AliyunBaseDao extends BaseDao {

    private final String driver;
    private final String url;
    private final String userName;
    private final String password;

    protected String schema;

    protected Properties pros;

    /**
     * 连接池缓存区
     * */
    private static Map<String, Connection> connectionPool = new HashMap<>();

    public AliyunBaseDao(){
        pros = PropertiesUtil.loadProperties("jdbc-aliyun.properties");
        if(pros == null){
            throw new IllegalArgumentException("加载 jdbc-aliyun.properties 失败");
        }

        driver = pros.getProperty("jdbc.driver");
        url = pros.getProperty("jdbc.url");
        userName = pros.getProperty("jdbc.username");
        password = pros.getProperty("jdbc.password");
    }

    protected void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    protected String getSchema() {
        return this.schema;
    }

    /**
     * 获取数据库连接
     * */
    protected Connection getConn(){
        /*if(connectionPool.containsKey(getSchema())){
            return connectionPool.get(getSchema());
        }*/

        Connection conn = null;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return conn;
        }

        try {
            conn = DriverManager.getConnection(url+getSchema(),userName,password);
//            connectionPool.put(getSchema(), conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return conn;
    }

    /**
     * 获取 DatabaseMetaData
     * */
    protected DatabaseMetaData getMetaData(){
        Connection conn = getConn();
        if(conn == null){
            return null;
        }

        try {
            return conn.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }

        return null;
    }

}
