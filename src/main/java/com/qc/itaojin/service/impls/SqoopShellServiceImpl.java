package com.qc.itaojin.service.impls;

import com.qc.itaojin.dao.ISqoopShellDao;
import com.qc.itaojin.dao.impls.SqoopShellDaoImpl;
import com.qc.itaojin.service.ISqoopShellService;
import com.qc.itaojin.service.common.BaseService;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public class SqoopShellServiceImpl extends BaseService implements ISqoopShellService {

    private ISqoopShellDao sqoopShellDao;

    public SqoopShellServiceImpl(){
        this.sqoopShellDao = new SqoopShellDaoImpl();
    }

    @Override
    public String generateSqoopShell(String schema, String table) {
        if(StringUtils.isBlank(schema) || StringUtils.isBlank(table)){
            return null;
        }
        return sqoopShellDao.generateSqoopShell(schema, table);
    }
}
