package cn.sissors.hummingbird.runtime.config;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * PropertiesConfiguration class is used to handle configuration in <i>.properties</i> format.
 *
 * @author zyz
 * @version 2018-10-28
 */
public abstract class PropertiesConfiguration extends Configuration {
    /**
     * Defined the rule to load a <i>.properties</i> file into memory.
     */
    public PropertiesConfiguration load() {
        for (String config : locations()) {
            try {
                InputStreamReader inputStreamReader;
                if (config.toLowerCase().startsWith("classpath")) {
                    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(StringUtils.substringAfter(config, ":").trim());
                    if (inputStream == null) {
                        continue;
                    }
                    inputStreamReader = new InputStreamReader((inputStream), StandardCharsets.UTF_8);
                } else {
                    File file = new File(config);
                    if (!file.exists()) {
                        continue;
                    }
                    inputStreamReader = new InputStreamReader(new FileInputStream(config), StandardCharsets.UTF_8);
                }
                Properties properties = PropertiesHandler.load(inputStreamReader);
                for (String prop : properties.stringPropertyNames()) {
                    String value = properties.getProperty(prop).trim();
                    set(prop, value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * Get all configuration paris in properties format
     *
     * @return saved properties
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        keys().forEach(key -> properties.put(key, get(key)));
        return properties;
    }
}
