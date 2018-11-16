package cn.sissors.hummingbird.runtime.config;

import org.junit.Test;

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
    }

    @Test
    public void testChinese() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdConfiguration();
        System.out.println("keyword = " + hummingbirdConfiguration.get("keyword"));
    }

    @Test
    public void testProperties() {
        PropertiesConfiguration hummingbirdConfiguration = new HummingbirdConfiguration();
        System.out.println(hummingbirdConfiguration.getProperties());
    }
}