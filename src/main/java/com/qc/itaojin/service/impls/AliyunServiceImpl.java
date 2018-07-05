package com.qc.itaojin.service.impls;

import com.qc.itaojin.dao.IAliyunDao;
import com.qc.itaojin.dao.impls.AliyunDaoImpl;
import com.qc.itaojin.service.IAliyunService;
import com.qc.itaojin.service.common.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by fuqinqin on 2018/6/26.
 */
@Service
public class AliyunServiceImpl extends BaseService implements IAliyunService {

    @Autowired
    private IAliyunDao aliyunDao;

    @Override
    public Set<String> queryTablesBySchema(String schema) {
        if(StringUtils.isBlank(schema)){
            return null;
        }

        Set<String> set = aliyunDao.queryTablesBySchema(schema);
        System.out.println("【查询所有表】schema="+schema+"，tables="+set.toString());

        return set;
    }
}
