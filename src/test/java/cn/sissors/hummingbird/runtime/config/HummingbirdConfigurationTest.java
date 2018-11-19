package cn.sissors.hummingbird.runtime.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author zyz
 * @version 2018-10-28
 */
public class HummingbirdConfigurationTest {
    @Test
    public void test() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdConfiguration();
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
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdConfiguration();
        System.out.println("keyword = " + hummingbirdConfiguration.get("keyword"));
        assertEquals(hummingbirdConfiguration.get("keyword"), "塑料");
    }

    @Test
    public void testProperties() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdConfiguration();
        System.out.println(hummingbirdConfiguration.getProperties());
        assertEquals(hummingbirdConfiguration.getProperties().get("keyword"), "塑料");
    }
}