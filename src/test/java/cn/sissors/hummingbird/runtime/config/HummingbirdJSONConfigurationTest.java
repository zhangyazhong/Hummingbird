package cn.sissors.hummingbird.runtime.config;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author sissors-lab
 * @version 2018-11-30
 */
public class HummingbirdJSONConfigurationTest {
    private HummingbirdJSONConfiguration configuration;

    @Before
    public void init() {
        configuration = new HummingbirdJSONConfiguration();
    }

    @Test
    public void testJSON() {
        assertEquals(configuration.get("system.name"), "ConfigTest");
        assertEquals(configuration.getInt("system.core").longValue(), 4);
        assertEquals(configuration.getLong("system.core").longValue(), 4);
        assertEquals(configuration.get("system.memory"), "8g");
    }

    @Test
    public void testChinese() {
        assertEquals(configuration.get("system.network.location"), "上海");
        assertEquals(configuration.getBoolean("system.network.async"), true);
        assertEquals(configuration.getDouble("system.network.bandwidth"), 54.0, 0.001);
    }

    @Test
    public void testDefault() {
        assertEquals(configuration.getOrDefault("system.name", "null"), "ConfigTest");
        assertEquals(configuration.getIntOrDefault("system.core", -1).longValue(), 4);
        assertEquals(configuration.getLongOrDefault("system.core", -1).longValue(), 4);
        assertEquals(configuration.getOrDefault("system.memory", "0g"), "8g");
        assertEquals(configuration.getOrDefault("system.network.location", "null"), "上海");
        assertEquals(configuration.getBooleanOrDefault("system.network.async", false), true);
        assertEquals(configuration.getDoubleOrDefault("system.network.bandwidth", -1), 54.0, 0.001);
    }

    @Test
    public void testNull() {
        assertEquals(configuration.get("system.ref"), "null");
    }
}
