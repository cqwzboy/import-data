package com.qc.itaojin;

import com.qc.itaojin.listener.GloableListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ImportDataApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImportDataApplication.class);
        springApplication.addListeners(new GloableListener());
        springApplication.run(args);
    }
}
