package com.qc.itaojin.service.impls;

import com.qc.itaojin.dao.ISqoopShellDao;
import com.qc.itaojin.dao.impls.SqoopShellDaoImpl;
import com.qc.itaojin.service.ISqoopShellService;
import com.qc.itaojin.service.common.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fuqinqin on 2018/6/26.
 */
@Service
public class SqoopShellServiceImpl extends BaseService implements ISqoopShellService {

    @Autowired
    private ISqoopShellDao sqoopShellDao;

    @Override
    public String generateSqoopShell(String schema, String table) {
        if(StringUtils.isBlank(schema) || StringUtils.isBlank(table)){
            return null;
        }
        return sqoopShellDao.generateSqoopShell(schema, table);
    }
}
