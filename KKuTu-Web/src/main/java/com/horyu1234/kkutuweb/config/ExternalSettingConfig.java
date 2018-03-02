package com.horyu1234.kkutuweb.config;

import com.horyu1234.kkutuweb.KKuTuWebApplication;
import com.horyu1234.kkutuweb.yaml.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by horyu on 2018-03-02
 */
@Configuration
@PropertySource(
        value = "file:${app.home}/" + KKuTuWebApplication.SETTING_FILE_NAME + ".yml",
        factory = YamlPropertySourceFactory.class
)
public class ExternalSettingConfig {
}
