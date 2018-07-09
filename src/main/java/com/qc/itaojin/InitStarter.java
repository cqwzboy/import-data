package com.qc.itaojin;

import com.qc.itaojin.common.Constants;
import com.qc.itaojin.service.IAliyunService;
import com.qc.itaojin.service.IHiveSQLService;
import com.qc.itaojin.service.IInitHiveService;
import com.qc.itaojin.util.PropertiesUtil;
import com.qc.itaojin.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.qc.itaojin.ImportDataApplication.SCHEMA;
import static com.qc.itaojin.common.GloableShare.DB_INFO;

/**
 * Created by fuqinqin on 2018/7/5.
 */
@Component
public class InitStarter {

    @Autowired
    private IAliyunService aliyunService;
    @Autowired
    private IHiveSQLService hiveSQLService;
    @Autowired
    private IInitHiveService initHiveService;

    /**
     * 全流程测试
     * */
    public void init(){
        Map<String, Map<String, String>> map = new HashMap<>();

        // step 1. 读取MySQL中所有schema
        Properties pros = PropertiesUtil.loadProperties("jdbc-aliyun.properties");
        String schemaStr = pros.getProperty("jdbc.schemas");
        if(StringUtils.isBlank(schemaStr)){
            return;
        }
        String[] schemas = schemaStr.split(",");
        System.out.println("schemas = "+schemas.toString());
        System.out.println("获取所有schema完毕。。。");
        for (String schema : schemas) {
            // step 2. 根据schema获取所有table
            Set<String> tables = aliyunService.queryTablesBySchema(schema);
            System.out.println("根据schema="+schema+"获取所有table完毕。。。");

            if(CollectionUtils.isEmpty(tables)){
                continue;
            }

            if(!DB_INFO.containsKey(schema)){
                DB_INFO.put(buildSchema(schema), new HashSet<>());
            }

            Map<String, String> tableMap = new HashMap<>();
            // step 3. 根据schema和table生成Hive的SQL
            for (String table : tables) {
                String hiveSql = hiveSQLService.generateDDL(schema, table);
                if(StringUtils.isNotBlank(hiveSql)){
                    DB_INFO.get(buildSchema(schema)).add(table);
                    tableMap.put(table, hiveSql);
                    System.out.println("table="+table+"，生成HiveSQL成功。");
                }
            }

            System.out.println("根据schema="+schema+"和table生成Hive的SQL完毕。。。");

            if(MapUtils.isNotEmpty(tableMap)){
                map.put(schema, tableMap);
            }
        }

        if(MapUtils.isEmpty(map)){
            return;
        }

        //step 4. 开始初始化Hive数据库
        System.out.println("---------开始初始化--------");
        for(Map.Entry<String, Map<String, String>> entry : map.entrySet()){
            String schema = entry.getKey();
            Map<String, String> hiveSqlMap = entry.getValue();

            if(!initHiveService.initBySchema(buildSchema(schema), hiveSqlMap)){
                System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Hive初始化schema="+schema+"异常 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }else{
                System.out.println("=================================================================================================== Hive初始化schema="+schema+"成功 ===================================================================================================");
            }
        }

        System.out.println("初始化完毕！！！！！！！");
    }

    private String buildSchema(String schema){
        StringBuilder nameSpace = new StringBuilder();
        nameSpace.append(SCHEMA)
                .append("000")
                .append(schema);

        return nameSpace.toString();
    }

}
