package cn.sissors.hummingbird.runtime.config;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * JSONConfiguration is to handle configuration in <i>JSON</i> format.
 *
 * @author zyz
 * @version 2018-11-30
 */
public abstract class JSONConfiguration extends Configuration {
    enum JSONType {
        Object("org.json.JSONObject"),
        Array("org.json.JSONArray");

        private String canonicalName;

        JSONType(String canonicalName) {
            this.canonicalName = canonicalName;
        }

        static boolean isObject(@NotNull Object value) {
            return Object.canonicalName.equals(value.getClass().getCanonicalName());
        }

        static boolean isArray(@NotNull Object value) {
            return Array.canonicalName.equals(value.getClass().getCanonicalName());
        }
    }

    private void parse(@NotNull JSONObject json, String prefix) {
        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = json.get(key);
            if (JSONType.isObject(value)) {
                parse(json.getJSONObject(key), prefix + key + ".");
            } else if (JSONType.isArray(value)) {
                parse(json.getJSONArray(key), prefix + key + ".");
            } else {
                set(prefix + key, String.valueOf(value));
            }
        }
    }

    private void parse(@NotNull JSONArray json, String prefix) {
        List<String> values = Lists.newLinkedList();
        for (int k = 0; k < json.length(); k++) {
            Object value = json.get(k);
            if (JSONType.isObject(value)) {
                parse(json.getJSONObject(k), prefix);
            } else if (JSONType.isArray(value)) {
                parse(json.getJSONArray(k), prefix);
            } else {
                values.add(String.valueOf(value));
            }
        }
        if (values.size() > 0) {
            set(prefix.substring(0, prefix.length() - 1), StringUtils.join(values, ","));
        }
    }

    /**
     * Defined the rule to load a <i>JSON</i> file into memory.
     */
    @Override
    public JSONConfiguration load() {
        for (String config : locations()) {
            try {
                File file;
                if (config.toLowerCase().startsWith("classpath")) {
                    file = new File(Objects.requireNonNull(this.getClass().getResource("/")).getPath() + StringUtils.substringAfter(config, ":").trim());
                } else {
                    file = new File(config);
                }
                if (!file.exists()) {
                    continue;
                }
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(content);
                parse(json, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
