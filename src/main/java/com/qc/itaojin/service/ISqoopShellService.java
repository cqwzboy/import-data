package com.qc.itaojin.service;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public interface ISqoopShellService {

    /**
     * 根据schema和table，生成sqoop导入habase的脚本
     * */
    public String generateSqoopShell(String schema, String table);

}
