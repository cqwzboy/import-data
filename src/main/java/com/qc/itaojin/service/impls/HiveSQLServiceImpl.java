package com.qc.itaojin.service.impls;

import com.qc.itaojin.dao.IHiveDDLFromAliyunDao;
import com.qc.itaojin.dao.impls.HiveDDLFromAliyunDaoImpl;
import com.qc.itaojin.service.IHiveSQLService;
import com.qc.itaojin.service.common.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fuqinqin on 2018/6/26.
 */
@Service
@Slf4j
public class HiveSQLServiceImpl extends BaseService implements IHiveSQLService {

    @Autowired
    private IHiveDDLFromAliyunDao hiveDDLFromAliyunDao;

    @Override
    public String generateDDL(String schema, String tableName) {
        if(StringUtils.isBlank(schema) || StringUtils.isBlank(tableName)){
            return null;
        }

        return hiveDDLFromAliyunDao.generateHiveDDL(schema, tableName);
    }
}
