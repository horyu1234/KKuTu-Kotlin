package com.horyu1234.kkutuweb.yaml;

import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.core.CollectionFactory;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by horyu on 2018-03-02
 */
public class YamlPropertiesProcessor extends YamlProcessor {
    YamlPropertiesProcessor(Resource resource) throws IOException {
        if (!resource.exists()) {
            throw new FileNotFoundException();
        }

        this.setResources(resource);
    }

    public Properties createProperties() {
        Properties result = CollectionFactory.createStringAdaptingProperties();
        process((properties, map) -> result.putAll(properties));

        return result;
    }
}