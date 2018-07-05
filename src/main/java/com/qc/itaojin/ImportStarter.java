package com.qc.itaojin;

import com.qc.itaojin.service.IAliyunService;
import com.qc.itaojin.service.ISqoopShellService;
import com.qc.itaojin.util.FileUtil;
import com.qc.itaojin.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Set;

/**
 * Created by fuqinqin on 2018/7/5.
 */
@Component
public class ImportStarter {

    private static final String PATH = "F://generateSqoopSql.sh";

    @Autowired
    private IAliyunService aliyunService;
    @Autowired
    private ISqoopShellService sqoopShellService;

    public void process(){
        // step 1. 读取MySQL中所有schema
        Properties pros = PropertiesUtil.loadProperties("jdbc-aliyun.properties");
        String schemaStr = pros.getProperty("jdbc.schemas");
        if(StringUtils.isBlank(schemaStr)){
            return;
        }
        String[] schemas = schemaStr.split(",");
        System.out.println("【sqoop shell】schemas = "+schemas.toString());
        System.out.println("【sqoop shell】获取所有schema完毕。。。");
        for (String schema : schemas) {
            // step 2. 根据schema获取所有table
            Set<String> tables = aliyunService.queryTablesBySchema(schema);
            System.out.println("【sqoop shell】根据schema="+schema+"获取所有table完毕。。。");

            if(CollectionUtils.isEmpty(tables)){
                continue;
            }

            // step 3. 根据schema和table生成Hive的SQL
            for (String table : tables) {
                String shell = sqoopShellService.generateSqoopShell(schema, table);
                if(StringUtils.isNotBlank(shell)){
                    FileUtil.writeStringTo(shell, PATH);
                }
                System.out.println("【sqoop shell】sqoop shell:"+shell);
            }
        }
    }

}
