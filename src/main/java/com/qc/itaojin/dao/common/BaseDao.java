package com.qc.itaojin.dao.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by fuqinqin on 2018/6/25.
 */
public abstract class BaseDao {

    protected abstract String getSchema();

    protected void close(Connection conn){
        close(conn, null, null);
    }

    protected void close(Connection conn, Statement state){
        close(conn, state, null);
    }

    protected void close(Connection conn, Statement state, ResultSet res){
        close(state, conn, res);
    }

    protected void close(Statement state,Connection conn, ResultSet... res){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(state != null){
            try {
                state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(res!=null && res.length>0){
            for (ResultSet re : res) {
                if(re != null){
                    try {
                        re.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
