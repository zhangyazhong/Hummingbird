package cn.sissors.hummingbird.runtime.config;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author zyz
 * @version 2018-10-28
 */
public class HummingbirdPropertiesConfiguration extends PropertiesConfiguration {
    @Override
    public List<String> locations() {
        return ImmutableList.of(
                "classpath: default.properties",
                "classpath: user-defined.properties",
                "classpath: missing.properties",
                "/etc/cn.sissors.hummingbird.properties"
        );
    }
}
