package com.horyu1234.kkutuweb.yaml;

import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;

/**
 * Created by horyu on 2018-03-02
 */
class YamlResourcePropertySource extends PropertiesPropertySource {
    YamlResourcePropertySource(String name, EncodedResource resource) throws IOException {
        super(name, new YamlPropertiesProcessor(resource.getResource()).createProperties());
    }
}