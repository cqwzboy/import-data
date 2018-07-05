package com.qc.itaojin.service;

import java.util.Map;

/**
 * @desc 初始化Hive数据库
 * @author fuqinqin
 * @date 2018/6/25
 */
public interface IInitHiveService {

    /**
     * @desc 初始化某个schema下的环境，如果schema不存在就新建，如果表格已存在则不再初始化
     * @param schema 数据库实例
     * @param sqlMap 初始化schema的sql集合
     * @return boolean true-成功 false-失败
     * */
    public boolean initBySchema(String schema, Map<String, String> sqlMap);

}
