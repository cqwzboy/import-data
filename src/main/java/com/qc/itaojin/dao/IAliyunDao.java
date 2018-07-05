package com.qc.itaojin.dao;

import java.util.Set;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public interface IAliyunDao {

    /**
     * 根据schema查询table集合
     * */
    public Set<String> queryTablesBySchema(String schema);

}
