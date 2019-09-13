package cn.sissors.hummingbird.runtime.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Contract(pure = true)
    public String get(String key) {
        return configs.get(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    @Contract(pure = true)
    public String getOrDefault(String key, String defaultValue) {
        return configs.getOrDefault(key, defaultValue);
    }

    /**
     * Get the setting based on key in integer.
     *
     * @param key the specified key
     * @return the setting value
     */
    @Contract(pure = true)
    public int getInt(String key) {
        return Integer.parseInt(configs.get(key));
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key in integer.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    @Contract(pure = true)
    public int getIntOrDefault(String key, int defaultValue) {
        return Integer.parseInt(getOrDefault(key, String.valueOf(defaultValue)));
    }

    /**
     * Get the setting based on key in double.
     *
     * @param key the specified key
     * @return the setting value
     */
    @Contract(pure = true)
    public double getDouble(String key) {
        return Double.parseDouble(configs.get(key));
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key in double.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    @Contract(pure = true)
    public double getDoubleOrDefault(String key, double defaultValue) {
        return Double.parseDouble(getOrDefault(key, String.valueOf(defaultValue)));
    }

    /**
     * Get the setting based on key in long.
     *
     * @param key the specified key
     * @return the setting value
     */
    @Contract(pure = true)
    public long getLong(String key) {
        return Long.parseLong(configs.get(key));
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key in long.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    @Contract(pure = true)
    public long getLongOrDefault(String key, long defaultValue) {
        return Long.parseLong(getOrDefault(key, String.valueOf(defaultValue)));
    }

    /**
     * Get the setting based on key in boolean.
     *
     * @param key the specified key
     * @return the setting value
     */
    @Contract(pure = true)
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(configs.get(key));
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key in boolean.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the setting value
     */
    @Contract(pure = true)
    public boolean getBooleanOrDefault(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getOrDefault(key, String.valueOf(defaultValue)));
    }

    /**
     * Get the setting based on key in string list.
     *
     * @param key the specified key
     * @return the setting value
     */
    @Contract(pure = true)
    public List<String> getList(String key) {
        return getList(key, ",");
    }

    /**
     * Get the setting based on key in string list.
     *
     * @param key       the specified key
     * @param separator the separator to split value
     * @return the setting value
     */
    @Contract(pure = true)
    public List<String> getList(String key, String separator) {
        separator = separator.replaceAll("\\|", "\\\\|");
        return Lists.newArrayList(configs.get(key).split(separator)).stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * Get the setting based on key in integer list.
     *
     * @param key the specified key
     * @return the setting value
     */
    @Contract(pure = true)
    public List<Integer> getListInt(String key) {
        return getListInt(key, ",");
    }

    /**
     * Get the setting based on key in integer list.
     *
     * @param key       the specified key
     * @param separator the separator to split value
     * @return the setting value
     */
    @Contract(pure = true)
    public List<Integer> getListInt(String key, String separator) {
        return getList(key, separator).stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    /**
     * Get the setting based on key in double list.
     *
     * @param key the specified key
     * @return the setting value
     */
    @Contract(pure = true)
    public List<Double> getListDouble(String key) {
        return getListDouble(key, ",");
    }

    /**
     * Get the setting based on key in double list.
     *
     * @param key       the specified key
     * @param separator the separator to split value
     * @return the setting value
     */
    @Contract(pure = true)
    public List<Double> getListDouble(String key, String separator) {
        return getList(key, separator).stream().map(Double::parseDouble).collect(Collectors.toList());
    }

    /**
     * Get the setting based on key in long list.
     *
     * @param key the specified key
     * @return the setting value
     */
    @Contract(pure = true)
    public List<Long> getListLong(String key) {
        return getListLong(key, ",");
    }

    /**
     * Get the setting based on key in double list.
     *
     * @param key       the specified key
     * @param separator the separator to split value
     * @return the setting value
     */
    @Contract(pure = true)
    public List<Long> getListLong(String key, String separator) {
        return getList(key, separator).stream().map(Long::parseLong).collect(Collectors.toList());
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map
     */
    @Contract(pure = true)
    public Set<String> keys() {
        return configs.keySet();
    }

    /**
     * Set a value to the specified key.
     *
     * @param key   the specified key
     * @param value the setting value
     */
    public void set(String key, String value) {
        configs.put(key, value);
    }

    /**
     * Set a series of values in list to the specified key.
     *
     * @param key    the specified key
     * @param values the setting value
     */
    public <T> void set(String key, List<T> values) {
        configs.put(key, StringUtils.join(values, ","));
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
