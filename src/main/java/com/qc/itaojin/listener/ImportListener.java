package com.qc.itaojin.listener;

import com.qc.itaojin.ImportStarter;
import com.qc.itaojin.InitStarter;
import com.qc.itaojin.common.Constants.HBaseConstants;
import com.qc.itaojin.service.IHBaseService;
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
public class ImportListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        ImportStarter importStarter = applicationContext.getBean("importStarter", ImportStarter.class);
        importStarter.process();
        log.info("=============================================== generate sqoop shell success ================================================");
    }
}
