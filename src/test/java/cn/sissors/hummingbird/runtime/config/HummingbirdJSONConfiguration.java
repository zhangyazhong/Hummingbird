package cn.sissors.hummingbird.runtime.config;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author sissors-lab
 * @version 2018-11-30
 */
public class HummingbirdJSONConfiguration extends JSONConfiguration {
    @Override
    public List<String> locations() {
        return ImmutableList.of(
                "classpath: default.json",
                "classpath: missing.json",
                "/etc/cn.sissors.hummingbird.json"
        );
    }
}
