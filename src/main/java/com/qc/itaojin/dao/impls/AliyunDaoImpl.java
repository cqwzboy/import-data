package com.qc.itaojin.dao.impls;

import com.qc.itaojin.dao.IAliyunDao;
import com.qc.itaojin.dao.common.AliyunBaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fuqinqin on 2018/6/26.
 */
@Component
public class AliyunDaoImpl extends AliyunBaseDao implements IAliyunDao {

    @Override
    public Set<String> queryTablesBySchema(String schema) {
        Connection conn = null;
        ResultSet schemaRes = null;

        // 设置数据库
        setSchema(schema);
        Set<String> set = new HashSet<>();

        try {
            conn = getConn();
            DatabaseMetaData metaData = conn.getMetaData();
            // 获取数据库下所有的表信息
            schemaRes = metaData.getTables(null, "%", "%", new String[]{"TABLE"});
            String tableName;
            while (schemaRes.next()){
                tableName = schemaRes.getString("TABLE_NAME");
                System.out.println("tableName="+tableName);
                if(StringUtils.isNotEmpty(tableName)){
                    set.add(tableName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, null, schemaRes);
        }

        return set;
    }
}
