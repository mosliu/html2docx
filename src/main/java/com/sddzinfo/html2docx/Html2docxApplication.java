package com.sddzinfo.html2docx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
@ComponentScan(basePackages = {"com.sddzinfo"})
public class Html2docxApplication {

    public static void main(String[] args) {
        log.warn("main start");
        SpringApplication app = new SpringApplication(Html2docxApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
        log.warn("main end");
    }

}
