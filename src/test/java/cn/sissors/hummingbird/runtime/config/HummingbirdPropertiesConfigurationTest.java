package cn.sissors.hummingbird.runtime.config;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author zyz
 * @version 2018-10-28
 */
public class HummingbirdPropertiesConfigurationTest {
    @Test
    public void test() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        System.out.println("a = " + hummingbirdConfiguration.get("a"));
        System.out.println("b = " + hummingbirdConfiguration.get("b"));
        System.out.println("c = " + hummingbirdConfiguration.getInt("c"));
        System.out.println("d = " + hummingbirdConfiguration.get("d"));
        assertEquals(hummingbirdConfiguration.get("a"), "hello");
        assertEquals(hummingbirdConfiguration.get("b"), "world");
        assertEquals(hummingbirdConfiguration.get("c"), "20");
        assertEquals(hummingbirdConfiguration.get("d"), "success");
    }

    @Test
    public void testChinese() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        System.out.println("keyword = " + hummingbirdConfiguration.get("keyword"));
        assertEquals(hummingbirdConfiguration.get("keyword"), "塑料");
    }

    @Test
    public void testProperties() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        System.out.println(hummingbirdConfiguration.getProperties());
        assertEquals(hummingbirdConfiguration.getProperties().get("keyword"), "塑料");
    }

    @Test
    public void testString() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertEquals(hummingbirdConfiguration.get("a"), "hello");
        assertEquals(hummingbirdConfiguration.getOrDefault("b", "default"), "world");
        assertEquals(hummingbirdConfiguration.getOrDefault("b_", "default"), "default");
    }

    @Test
    public void testInt() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertEquals(hummingbirdConfiguration.getInt("c"), 20);
        assertEquals(hummingbirdConfiguration.getIntOrDefault("c", 30), 20);
        assertEquals(hummingbirdConfiguration.getIntOrDefault("c_", 30), 30);
    }

    @Test
    public void testDouble() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertEquals(hummingbirdConfiguration.getDouble("e"), 3.5, 0.00000001);
        assertEquals(hummingbirdConfiguration.getDoubleOrDefault("e", 4.5), 3.5, 0.00000001);
        assertEquals(hummingbirdConfiguration.getDoubleOrDefault("e_", 4.5), 4.5, 0.00000001);
    }

    @Test
    public void testLong() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertEquals(hummingbirdConfiguration.getLong("f"), 2147483648L);
        assertEquals(hummingbirdConfiguration.getLongOrDefault("f", 1L), 2147483648L);
        assertEquals(hummingbirdConfiguration.getLongOrDefault("f_", 1L), 1L);
    }

    @Test
    public void testBoolean() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertTrue(hummingbirdConfiguration.getBoolean("k"));
        assertTrue(hummingbirdConfiguration.getBoolean("l"));
        assertFalse(hummingbirdConfiguration.getBooleanOrDefault("k_", false));
    }

    @Test
    public void testList() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getList("g"), ImmutableList.of("hello", "world")));
        assertFalse(isEqually(hummingbirdConfiguration.getList("h"), ImmutableList.of("hello", "world")));
        assertTrue(isEqually(hummingbirdConfiguration.getList("h", "|"), ImmutableList.of("hello", "world")));
    }

    @Test
    public void testListInt() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getListInt("i"), ImmutableList.of(8, 6, 5)));
        assertTrue(isEqually(hummingbirdConfiguration.getListInt("j", "|"), ImmutableList.of(8, 6, 5)));
    }

    @Test
    public void testListDouble() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getListDouble("m"), ImmutableList.of(2.7, 6.5, 4.8), 0.00000001));
        assertTrue(isEqually(hummingbirdConfiguration.getListDouble("n", "|"), ImmutableList.of(2.7, 6.5, 4.8), 0.00000001));
        assertTrue(isEqually(hummingbirdConfiguration.getListDouble("o", "|"), ImmutableList.of(2.7, 3), 0.00000001));
    }

    @Test
    public void testListLong() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getListLong("p"), ImmutableList.of(2147483647L, 2147483648L)));
        assertTrue(isEqually(hummingbirdConfiguration.getListLong("q", "|"), ImmutableList.of(2147483647L, 2147483648L)));
    }

    @Test
    public void testSetSingle() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertEquals(hummingbirdConfiguration.getInt("c"), 20);
        assertEquals(hummingbirdConfiguration.getIntOrDefault("c_", 100), 100);
        hummingbirdConfiguration.set("c", "30");
        hummingbirdConfiguration.set("c_", "150");
        assertEquals(hummingbirdConfiguration.getInt("c"), 30);
        assertEquals(hummingbirdConfiguration.getIntOrDefault("c_", 100), 150);
    }

    @Test
    public void testSetList() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdPropertiesConfiguration();
        assertTrue(isEqually(hummingbirdConfiguration.getListInt("i"), ImmutableList.of(8, 6, 5)));
        hummingbirdConfiguration.set("i", ImmutableList.of(20, 30, 50));
        assertTrue(isEqually(hummingbirdConfiguration.getListInt("i"), ImmutableList.of(20, 30, 50)));
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

    @Test
    public void testSeparator() {
        String a = "2.7 | 6.5 | 4.8";
        System.out.println(StringUtils.join(a.split("\\|"), ","));
    }
}