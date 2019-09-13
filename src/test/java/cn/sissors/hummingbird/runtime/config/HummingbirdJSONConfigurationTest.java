package cn.sissors.hummingbird.runtime.config;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        assertEquals(configuration.getInt("system.core"), 4);
        assertEquals(configuration.getLong("system.core"), 4);
        assertEquals(configuration.get("system.memory"), "8g");
    }

    @Test
    public void testChinese() {
        assertEquals(configuration.get("system.network.location"), "上海");
        assertTrue(configuration.getBoolean("system.network.async"));
        assertEquals(configuration.getDouble("system.network.bandwidth"), 54.0, 0.001);
    }

    @Test
    public void testDefault() {
        assertEquals(configuration.getOrDefault("system.name", "null"), "ConfigTest");
        assertEquals(configuration.getIntOrDefault("system.core", -1), 4);
        assertEquals(configuration.getLongOrDefault("system.core", -1), 4);
        assertEquals(configuration.getOrDefault("system.memory", "0g"), "8g");
        assertEquals(configuration.getOrDefault("system.network.location", "null"), "上海");
        assertTrue(configuration.getBooleanOrDefault("system.network.async", false));
        assertEquals(configuration.getDoubleOrDefault("system.network.bandwidth", -1), 54.0, 0.001);
    }

    @Test
    public void testNull() {
        assertEquals(configuration.get("system.ref"), "null");
    }

    @Test
    public void testString() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertEquals(hummingbirdConfiguration.get("system.args.a"), "hello");
        assertEquals(hummingbirdConfiguration.getOrDefault("system.args.b", "default"), "world");
        assertEquals(hummingbirdConfiguration.getOrDefault("system.args.b_", "default"), "default");
    }

    @Test
    public void testInt() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertEquals(hummingbirdConfiguration.getInt("system.args.c"), 20);
        assertEquals(hummingbirdConfiguration.getIntOrDefault("system.args.c", 30), 20);
        assertEquals(hummingbirdConfiguration.getIntOrDefault("system.args.c_", 30), 30);
    }

    @Test
    public void testDouble() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertEquals(hummingbirdConfiguration.getDouble("system.args.e"), 3.5, 0.00000001);
        assertEquals(hummingbirdConfiguration.getDoubleOrDefault("system.args.e", 4.5), 3.5, 0.00000001);
        assertEquals(hummingbirdConfiguration.getDoubleOrDefault("system.args.e_", 4.5), 4.5, 0.00000001);
    }

    @Test
    public void testLong() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertEquals(hummingbirdConfiguration.getLong("system.args.f"), 2147483648L);
        assertEquals(hummingbirdConfiguration.getLongOrDefault("system.args.f", 1L), 2147483648L);
        assertEquals(hummingbirdConfiguration.getLongOrDefault("system.args.f_", 1L), 1L);
    }

    @Test
    public void testBoolean() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertTrue(hummingbirdConfiguration.getBoolean("system.args.k"));
        assertTrue(hummingbirdConfiguration.getBoolean("system.args.l"));
        assertFalse(hummingbirdConfiguration.getBooleanOrDefault("system.args.k_", false));
    }

    @Test
    public void testList() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getList("system.args.g"), ImmutableList.of("hello", "world")));
        assertFalse(isEqually(hummingbirdConfiguration.getList("system.args.h"), ImmutableList.of("hello", "world")));
        assertTrue(isEqually(hummingbirdConfiguration.getList("system.args.h", "|"), ImmutableList.of("hello", "world")));
    }

    @Test
    public void testListInt() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getListInt("system.args.i"), ImmutableList.of(8, 6, 5)));
        assertTrue(isEqually(hummingbirdConfiguration.getListInt("system.args.j", "|"), ImmutableList.of(8, 6, 5)));
    }

    @Test
    public void testListDouble() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getListDouble("system.args.m"), ImmutableList.of(2.7, 6.5, 4.8), 0.00000001));
        assertTrue(isEqually(hummingbirdConfiguration.getListDouble("system.args.n", "|"), ImmutableList.of(2.7, 6.5, 4.8), 0.00000001));
        assertTrue(isEqually(hummingbirdConfiguration.getListDouble("system.args.o", "|"), ImmutableList.of(2.7, 3), 0.00000001));
    }

    @Test
    public void testListLong() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getListLong("system.args.p"), ImmutableList.of(2147483647L, 2147483648L)));
        assertTrue(isEqually(hummingbirdConfiguration.getListLong("system.args.q", "|"), ImmutableList.of(2147483647L, 2147483648L)));
    }

    @Test
    public void testSetSingle() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertEquals(hummingbirdConfiguration.getInt("system.args.c"), 20);
        assertEquals(hummingbirdConfiguration.getIntOrDefault("system.args.c_", 100), 100);
        hummingbirdConfiguration.set("system.args.c", "30");
        hummingbirdConfiguration.set("system.args.c_", "150");
        assertEquals(hummingbirdConfiguration.getInt("system.args.c"), 30);
        assertEquals(hummingbirdConfiguration.getIntOrDefault("system.args.c_", 100), 150);
    }

    @Test
    public void testSetList() {
        JSONConfiguration hummingbirdConfiguration = new HummingbirdJSONConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getListInt("system.args.i"), ImmutableList.of(8, 6, 5)));
        hummingbirdConfiguration.set("system.args.i", ImmutableList.of(20, 30, 50));
        assertTrue(isEqually(hummingbirdConfiguration.getListInt("system.args.i"), ImmutableList.of(20, 30, 50)));
    }

    private <L, R> boolean isEqually(@NotNull List<L> list1, @NotNull List<R> list2) {
        return list1.size() == list2.size()
                && IntStream.range(0, (list1.size() + list2.size()) >> 1).allMatch(idx -> list1.get(idx).equals(list2.get(idx)));
    }

    private boolean isEqually(@NotNull List<? extends Number> list1, @NotNull List<? extends Number> list2, double delta) {
        return list1.size() == list2.size()
                && IntStream.range(0, (list1.size() + list2.size()) >> 1)
                .allMatch(idx -> list1.get(idx).doubleValue() - list2.get(idx).doubleValue() <= delta);
    }
}
