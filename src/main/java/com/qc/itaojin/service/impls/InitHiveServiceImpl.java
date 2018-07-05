package com.qc.itaojin.service.impls;

import com.qc.itaojin.common.Constants;
import com.qc.itaojin.dao.common.HiveBaseDao;
import com.qc.itaojin.service.IInitHiveService;
import com.qc.itaojin.service.common.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by fuqinqin on 2018/6/25.
 */
@Service
@Slf4j
public class InitHiveServiceImpl extends BaseService implements IInitHiveService {

    @Autowired
    private HiveBaseDao hiveBaseDao;

    @Override
    public boolean initBySchema(String schema, Map<String, String> sqlMap) {
        if(StringUtils.isBlank(schema) || MapUtils.isEmpty(sqlMap)){
            return false;
        }

        // step 1. 查询该schema是否存在，不存在则新建
        hiveBaseDao.setSchema(Constants.HiveConstants.DEFAULT_SCHEMA);
        if(!hiveBaseDao.schemaExists(schema)){
            System.out.println("Hive 不存在实例"+schema+"，新建之");

            // step 2. 创建schema
            if(!hiveBaseDao.execute("create database "+schema)){
                System.out.println("Hive 创建数据库实例 "+schema+" 失败");
                return false;
            }
        }

        // step 3. 判断新建的表格中是否已经存在，若不存在则新建
        hiveBaseDao.setSchema(schema);
        Set<String> needCreateTables = new HashSet<>();
        Set<String> oldTables = hiveBaseDao.findAllTables();
        Set<String> newTables = sqlMap.keySet();
        if(CollectionUtils.isEmpty(oldTables)){
            needCreateTables = newTables;
        }else{
            for (String newTable : newTables) {
                if(!oldTables.contains(newTable)){
                    needCreateTables.add(newTable);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(needCreateTables)){
            for (String t : needCreateTables) {
                // 建表
                if(!hiveBaseDao.execute(sqlMap.get(t))){
                    System.out.println("Hive 建表 "+t+" 失败");
                    return false;
                }else{
                    System.out.println("Hive 建表 "+t+" 成功");
                }
            }

            System.out.println("初始化Hive数据库"+schema+"成功！");
        }else{
            System.out.println("无新表可更新");
        }

        return true;
    }

}
