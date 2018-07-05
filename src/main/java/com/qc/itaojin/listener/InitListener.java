package com.qc.itaojin.listener;

import com.qc.itaojin.InitStarter;
import com.qc.itaojin.common.Constants;
import com.qc.itaojin.common.Constants.HBaseConstants;
import com.qc.itaojin.common.GloableShare;
import com.qc.itaojin.service.IHBaseService;
import com.qc.itaojin.service.impls.HBaseServiceImpl;
import com.qc.itaojin.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;
import java.util.Set;

import static com.qc.itaojin.common.GloableShare.DB_INFO;

/**
 * Created by fuqinqin on 2018/7/5.
 */
@Slf4j
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        // generate HiveSQL
        InitStarter initStarter = applicationContext.getBean("initStarter", InitStarter.class);
        initStarter.init();

        // update HBase Versions
        IHBaseService hBaseService = applicationContext.getBean("ihBaseService", IHBaseService.class);
        if(MapUtils.isNotEmpty(DB_INFO)){
            // 开启使用单一连接
            hBaseService.useSingleConn();
            try{
                out:for(Map.Entry<String, Set<String>> entry : DB_INFO.entrySet()){
                    String schema = entry.getKey();
                    Set<String> tables = entry.getValue();
                    if(CollectionUtils.isEmpty(tables)){
                        continue out;
                    }

                    in:for (String table : tables) {
                        if("新表".equals(table)){
                            continue in;
                        }
                        hBaseService.updateVersions(schema, table, HBaseConstants.DEFAULT_FAMILY, HBaseConstants.DEFAULT_VERSIONS);
                        log.info("修改 {} 的VERSIONS参数成功！", StringUtils.contact(schema, ":", table));
                    }
                }
            }finally {
                // 关闭连接
                hBaseService.closeSingleConn();
            }
        }
    }
}
