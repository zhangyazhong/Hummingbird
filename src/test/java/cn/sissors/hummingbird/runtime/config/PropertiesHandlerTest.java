package cn.sissors.hummingbird.runtime.config;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author yazhong.zyz
 * @version 2019-08-21
 */
public class PropertiesHandlerTest {
    @Test
    public void testLoad() throws IOException {
        Properties properties = PropertiesHandler.load("src/test/resources/user-defined.properties");
        assertEquals("success", properties.getProperty("d"));
    }

    @Test
    public void testUpdate() throws IOException {
        Properties properties = PropertiesHandler.load("src/test/resources/user-defined.properties");
        assertEquals("success", properties.getProperty("d"));
        PropertiesHandler.update(properties, ImmutableMap.of(
                "d", "success_updated",
                "d_", "success_created"
        ));
        assertEquals("success_updated", properties.getProperty("d"));
        assertEquals("success_created", properties.getProperty("d_"));
    }

    @Test
    public void testSave() throws IOException {
        Properties properties = PropertiesHandler.load("src/test/resources/user-defined.properties");
        PropertiesHandler.save("persistence/user-defined1.properties", properties);
        PropertiesHandler.save("persistence/user-defined2.properties", ImmutableMap.of(
                "d", "success_updated",
                "d_", "success_created"
        ));
    }
}
