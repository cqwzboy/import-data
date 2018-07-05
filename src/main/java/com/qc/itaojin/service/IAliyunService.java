package com.qc.itaojin.service;

import java.util.Set;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public interface IAliyunService {

    /**
     * 根据数据库查询所有表名集合
     * */
    public Set<String> queryTablesBySchema(String schema);

}
