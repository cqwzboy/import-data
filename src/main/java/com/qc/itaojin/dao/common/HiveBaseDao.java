package com.qc.itaojin.dao.common;

import com.qc.itaojin.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * Created by fuqinqin on 2018/6/25.
 */
@Component
public class HiveBaseDao extends BaseDao {

    /**
     * 连接池缓存区
     * */
    private static Map<String, Connection> connectionPool = new HashMap<>();

    private final String driver;
    private final String url;

    protected String schema;

    public HiveBaseDao(){
        Properties pros = PropertiesUtil.loadProperties("jdbc-hive.properties");
        if(pros == null){
            throw new IllegalArgumentException("解析 jdbc-hive.properties 失败");
        }

        driver = pros.getProperty("hive.driver");
        url = pros.getProperty("hive.url");
    }

    public void setSchema(String schema){
        this.schema = schema;
    }

    @Override
    protected String getSchema() {
        return this.schema;
    }

    /**
     * 获取Hive的连接对象
     * */
    public Connection getConn(){
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
            conn = DriverManager.getConnection(url + getSchema());
//            connectionPool.put(getSchema(), conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * 执行Hive的SQL
     * */
    public boolean execute(String sql){
        if(StringUtils.isBlank(sql)){
            return false;
        }

        Connection conn = null;
        PreparedStatement pstat = null;

        try{
            conn = getConn();
            if(conn == null){
                return false;
            }

            pstat = conn.prepareStatement(sql);
            pstat.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstat);
        }

        return false;
    }

    /**
     * 获取Hive中所有数据库实例集合
     * */
    public Set<String> findAllDataBases(){
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet res = null;

        try {
            Set<String> set = new HashSet<>();
            conn = getConn();
            pstat = conn.prepareStatement("show databases");
            res = pstat.executeQuery();
            if(res != null){
                while (res.next()){
                    set.add(res.getString(1));
                }
            }

            return set;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(conn, pstat, res);
        }

        return null;
    }

    /**
     * 获取Hive中所有数据库实例集合
     * */
    public Set<String> findAllTables(){
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet res = null;

        try {
            Set<String> set = new HashSet<>();
            conn = getConn();
            pstat = conn.prepareStatement("show tables in "+getSchema());
            res = pstat.executeQuery();
            if(res != null){
                while (res.next()){
                    set.add(res.getString(1));
                }
            }

            return set;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(conn, pstat, res);
        }

        return null;
    }

    /**
     * 查询某个数据库实例是否已经存在
     * */
    public boolean schemaExists(String schema){
        if(StringUtils.isBlank(schema)){
            return false;
        }

        Set<String> schemas = findAllDataBases();
        if(CollectionUtils.isEmpty(schemas)){
            return false;
        }

        return schemas.contains(schema);
    }

}
