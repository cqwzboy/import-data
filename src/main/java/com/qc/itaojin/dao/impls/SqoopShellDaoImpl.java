package com.qc.itaojin.dao.impls;

import com.qc.itaojin.common.Constants;
import com.qc.itaojin.dao.ISqoopShellDao;
import com.qc.itaojin.dao.common.AliyunBaseDao;
import com.qc.itaojin.enums.KeyType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuqinqin on 2018/6/26.
 */
@Component
public class SqoopShellDaoImpl extends AliyunBaseDao implements ISqoopShellDao {

    private String connect = "--connect %s%s ";
    private String username = "--username %s ";
    private String password = "--password %s ";
    private String table = "--table %s ";
    private String deleteTarget = "--delete-target-dir ";
    private String hbaseTabel = "--hbase-table %s:%s ";
    private String columnFamily = "--column-family %s ";
    private String hbaseRowKey = "--hbase-row-key %s ";
    private String nullFormat = "--null-string '\\\\N' --null-non-string '\\\\N'";

    @Override
    public String generateSqoopShell(String schema, String tableName) {
        Connection connection = null;
        ResultSet pkResultSet = null;

        StringBuilder shell = null;

        // 设置数据库
        setSchema(schema);

        try {
            connection = getConn();
            DatabaseMetaData metaData = connection.getMetaData();

            // 获取表格的主键
            List<String> pks = new ArrayList<>();
            pkResultSet = metaData.getPrimaryKeys(null, schema, tableName);
            while(pkResultSet.next()){
                pks.add(pkResultSet.getString("COLUMN_NAME"));
            }

            //主键类型
            KeyType keyType;
            if(CollectionUtils.isEmpty(pks)){
                keyType = KeyType.NONE;
                // TODO
                return null;
            }else if(pks.size() == 1){
                keyType = KeyType.PRIMARY_KEY;
            }else{
                keyType = KeyType.COMBINE_KEY;
            }

            shell = new StringBuilder("sqoop import ");
            shell.append(String.format(connect, pros.getProperty("jdbc.url"), getSchema()));
            shell.append(String.format(username, pros.getProperty("jdbc.username")));
            shell.append(String.format(password, pros.getProperty("jdbc.password")));
            shell.append(String.format(table, tableName));
            shell.append(deleteTarget);
            shell.append(String.format(hbaseTabel, buildSchema(schema), tableName));
            shell.append(String.format(columnFamily, "f1"));

            String rowKey;
            if(keyType.equalsTo(KeyType.PRIMARY_KEY)){
                rowKey = pks.get(0);
            }else if(keyType.equalsTo(KeyType.COMBINE_KEY)){
                StringBuilder sb = new StringBuilder();
                for (String pk : pks) {
                    sb.append(pk).append(",");
                }
                sb.setLength(sb.length() - 1);
                rowKey = sb.toString();
            }else{
                rowKey = "";
            }
            shell.append(String.format(hbaseRowKey, rowKey));
            shell.append(nullFormat);
            shell.append(";");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, pkResultSet);
        }

        return shell.toString();
    }

    private String buildSchema(String schema){
        StringBuilder nameSpace = new StringBuilder();
        nameSpace.append(Constants.SCHEMA)
                .append("000")
                .append(schema);

        return nameSpace.toString();
    }
}
