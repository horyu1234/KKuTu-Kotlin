package com.horyu1234.kkutuweb;

import com.horyu1234.kkutuweb.util.JarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationHome;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class KKuTuWebApplication {
    public static final String SETTING_FILE_NAME = "application";
    private static final Logger LOGGER = LoggerFactory.getLogger(KKuTuWebApplication.class);

    public static void main(String[] args) {
        setStartupDirSystemProperty();
        copyResourcesIfNotExist();

        SpringApplication.run(KKuTuWebApplication.class, args);
    }

    private static void setStartupDirSystemProperty() {
        File applicationStartupDir = new ApplicationHome(KKuTuWebApplication.class).getDir();
        String startupDirPath = applicationStartupDir.getPath();
        startupDirPath = startupDirPath.replace("\\", "/");

        System.setProperty("app.home", startupDirPath);
    }

    private static void copyResourcesIfNotExist() {
        if (JarUtils.copyResource(SETTING_FILE_NAME + ".yml", "config")) {
            LOGGER.info("설정 파일이 존재하지 않아, 새로 생성되었습니다.");
        }

        if (JarUtils.copyResource("static", "public")) {
            LOGGER.info("웹 리소스 파일이 존재하지 않아, 새로 생성되었습니다.");
        }

        if (JarUtils.copyResource("templates", "public")) {
            LOGGER.info("웹 템플릿 파일이 존재하지 않아, 새로 생성되었습니다.");
        }
    }
}
