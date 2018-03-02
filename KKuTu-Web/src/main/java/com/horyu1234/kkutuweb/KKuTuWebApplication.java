package com.horyu1234.kkutuweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationHome;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@SpringBootApplication
public class KKuTuWebApplication {
    public static final String SETTING_FILE_NAME = "application";
    private static final Logger LOGGER = LoggerFactory.getLogger(KKuTuWebApplication.class);

    public static void main(String[] args) {
        File applicationStartupDir = new ApplicationHome(KKuTuWebApplication.class).getDir();
        System.setProperty("app.home", applicationStartupDir.getPath());

        if (!createSettingFileIfNotExist()) {
            return;
        }

        SpringApplication.run(KKuTuWebApplication.class, args);
    }

    private static boolean createSettingFileIfNotExist() {
        File settingFile = new File(System.getProperty("app.home"), SETTING_FILE_NAME + ".yml");

        if (!settingFile.exists()) {
            try (InputStream resourceFileStream = getResourceFileStream(SETTING_FILE_NAME + ".default.yml")) {
                Files.copy(resourceFileStream, settingFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                LOGGER.info("설정 파일이 존재하지 않아, 새로 생성되었습니다.");
                return true;
            } catch (IOException e) {
                LOGGER.error("설정 파일을 생성하는 중 오류가 발생하였습니다.", e);
                return false;
            }
        }

        return true;
    }

    private static InputStream getResourceFileStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}
