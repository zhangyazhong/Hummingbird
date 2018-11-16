package cn.sissors.hummingbird.runtime.config;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Configuration class is used to simplify the process of loading multiple configurations into memory.
 *
 * @author zyz
 * @version 2018-10-23
 */
public abstract class Configuration {
    private Map<String, String> configs;

    public Configuration() {
        this.configs = Maps.newHashMap();
        this.load();
    }

    /**
     * Get the setting based on key.
     *
     * @param key the specified key
     * @return the setting value
     */
    public String get(String key) {
        return configs.get(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    public String getOrDefault(String key, String defaultValue) {
        return configs.getOrDefault(key, defaultValue);
    }

    /**
     * Get the setting based on key in integer.
     *
     * @param key the specified key
     * @return the setting value
     */
    public Integer getInt(String key) {
        return Integer.parseInt(configs.get(key));
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key in integer.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    public Integer getIntOrDefault(String key, int defaultValue) {
        return Integer.parseInt(getOrDefault(key, String.valueOf(defaultValue)));
    }

    /**
     * Get the setting based on key in double.
     *
     * @param key the specified key
     * @return the setting value
     */
    public Double getDouble(String key) {
        return Double.parseDouble(configs.get(key));
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key in double.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    public Double getDoubleOrDefault(String key, double defaultValue) {
        return Double.parseDouble(getOrDefault(key, String.valueOf(defaultValue)));
    }

    /**
     * Get the setting based on key in long.
     *
     * @param key the specified key
     * @return the setting value
     */
    public Long getLong(String key) {
        return Long.parseLong(configs.get(key));
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key in long.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    public Long getLongOrDefault(String key, long defaultValue) {
        return Long.parseLong(getOrDefault(key, String.valueOf(defaultValue)));
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map
     */
    public Set<String> keys() {
        return configs.keySet();
    }

    /**
     * Set a value to the specified key.
     *
     * @param key the specified key
     * @param value the setting value
     */
    public void set(String key, String value) {
        configs.put(key, value);
    }

    /**
     * Based on external locations, load configurations into memory.
     *
     * @return configuration object itself
     */
    public abstract Configuration load();

    /**
     * Get locations of all configuration files.
     *
     * <p><b>RECOMMENDED:</b> better to use {@link java.util.LinkedList}
     * or {@link com.google.common.collect.ImmutableList} to ensure the
     * loading order of configuration as there may exist different values
     * with the same key.
     *
     * @return a list contains all locations
     */
    public abstract List<String> locations();
}
