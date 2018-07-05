package com.qc.itaojin.dao.impls;

import com.qc.itaojin.common.Constants;
import com.qc.itaojin.common.Constants.HBaseConstants;
import com.qc.itaojin.dao.IHiveDDLFromAliyunDao;
import com.qc.itaojin.dao.common.AliyunBaseDao;
import com.qc.itaojin.enums.KeyType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static com.qc.itaojin.enums.MysqlDataType.nameOf;
import static com.qc.itaojin.enums.MysqlDataType.transToHiveDataType;

/**
 * Created by fuqinqin on 2018/6/26.
 */
@Component
public class HiveDDLFromAliyunDaoImpl extends AliyunBaseDao implements IHiveDDLFromAliyunDao {

    @Override
    public String generateHiveDDL(String schema, String tableName) {
        Connection connection = null;
        ResultSet pkResultSet = null;
        ResultSet rs = null;

        StringBuilder sql = null;

        // 设置数据库
        setSchema(schema);

        try {
            connection = getConn();
            DatabaseMetaData metaData = connection.getMetaData();

            // 获取表格的主键
            Set<String> pks = new HashSet<>();
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
                System.out.println("========================= 联合主键：schema="+schema+", table="+tableName+" ==========================");
            }

            // 获取数据库下所有的表信息
            String columnName;
            String columnType;
            String comment;
            // 拼接Hive的sql缓存区
            sql = new StringBuilder();
            sql.append("CREATE TABLE ")
                    .append(tableName).append("\n")
                    .append("(").append("\n");
            //映射字符串
            StringBuilder mapStr = new StringBuilder(":key,");
            //如果表是联合主键，则Hive的建表语句中需要新增一列：联合主键
            if(keyType.equalsTo(KeyType.COMBINE_KEY)){
                sql.append("COMBINE_KEY_121414125").append(" ").append("varchar(4000)").append(" ").append("comment '联合主键',\n");
            }
            //获取表内的字段名称和类型
            rs = metaData.getColumns(null, schema, tableName, "%");
            // 列数，如果在单主键的表格中只有一个列，则需要添加一列占位列，否则hbase报错
            int columnNum = 0;
            while (rs.next()){
                columnName = rs.getString("COLUMN_NAME");
                columnType = rs.getString("TYPE_NAME");
                comment = rs.getString("REMARKS");

                columnNum++;

                //注释中存在分号则替换成逗号
                comment = comment.replaceAll(";", ",");

                sql.append("`").append(columnName).append("` ")
                        .append(transToHiveDataType(nameOf(columnType))).append(" ")
                        .append("comment '").append(comment).append("',").append("\n");

                // 当且仅当是单主键时":key"对应的是主键，故而无需再次申明
                if(keyType.equalsTo(KeyType.PRIMARY_KEY) && pks.contains(columnName)){
                    continue;
                }
                mapStr.append(HBaseConstants.DEFAULT_FAMILY).append(":").append(columnName).append(",");
            }

            if(keyType.equalsTo(KeyType.PRIMARY_KEY) && columnNum==1){
                sql.append("`placeholder` int comment 'placeholder',\n");
                mapStr.append("f1:placeholder,");
            }

            sql.setLength(sql.length() - 2);
            mapStr.setLength(mapStr.length() - 1);

            sql.append("\n")
                    .append(")").append("\n")
                    .append("ROW FORMAT").append("\n")
                    .append("SERDE 'org.apache.hadoop.hive.hbase.HBaseSerDe'").append("\n")
                    .append("STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'").append("\n")
                    .append("WITH SERDEPROPERTIES (").append("\n")
                    .append("'serialization.format'='\\t',").append("\n")
                    .append("'hbase.columns.mapping'='").append(mapStr.toString()).append("',").append("\n")
                    .append("'field.delim'='\\t'").append("\n")
                    .append(")").append("\n")
                    .append("TBLPROPERTIES ('hbase.table.name'='"+buildSchema(schema)+":").append(tableName).append("')");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, connection, pkResultSet, rs);
        }

        return sql.toString();
    }

    private String buildSchema(String schema){
        StringBuilder nameSpace = new StringBuilder();
        nameSpace.append(Constants.SCHEMA)
                .append("000")
                .append(schema);

        return nameSpace.toString();
    }
}
