package cn.sissors.hummingbird.runtime.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * This is an assistance tool for handling <i>.properties</i> format file.
 *
 * <p>Also, you can use this handler individually, to create or update <i>.properties</i> format file.
 *
 * @author zyz
 * @version 2018-05-10
 */
public class PropertiesHandler {
    /**
     * Load <i>.properties</i> file and return a {@link java.util.Properties} object
     *
     * @param inputStreamReader the input stream of external file
     * @return a {@link java.util.Properties} object
     * @throws IOException if an error occurred when reading from the input stream.
     */
    public static Properties load(InputStreamReader inputStreamReader) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStreamReader);
        return properties;
    }

    /**
     * Load <i>.properties</i> file and return a {@link java.util.Properties} object
     *
     * @param path the path of external file
     * @return a {@link java.util.Properties} object
     * @throws IOException file not exists or if an error occurred when reading from the input stream.
     */
    public static Properties load(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        return properties;
    }

    /**
     * Update a {@link java.util.Properties} object with a java map.
     *
     * @param properties the properties
     * @param params     java map
     * @return the properties after updating
     */
    public static Properties update(Properties properties, Map<String, String> params) {
        params.forEach(properties::setProperty);
        return properties;
    }

    /**
     * Save a {@link java.util.Properties} object to external file.
     *
     * @param path       external location
     * @param properties a {@link java.util.Properties} object
     * @throws IOException if writing this property list to the specified output stream throws an <tt>IOException</tt>.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void save(String path, Properties properties) throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8);
        properties.store(outputStreamWriter, "");
    }

    /**
     * Save a java map to external in <i>.properties</i> format.
     *
     * @param path   external location
     * @param params java map
     * @throws IOException if writing this property list to the specified output stream throws an <tt>IOException</tt>.
     */
    public static void save(String path, Map<String, String> params) throws IOException {
        Properties properties = new Properties();
        params.forEach(properties::setProperty);
        save(path, properties);
    }
}
