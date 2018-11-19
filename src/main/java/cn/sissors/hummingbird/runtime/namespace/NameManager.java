package cn.sissors.hummingbird.runtime.namespace;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * NameManager provides a tool to generate names used for system. This can ensure
 * no duplicate name appearing at one time period.
 *
 * <p>But, to ensure performance, duplicate checking will run <b>{@code MAX_CHECKING_ROUND}</b>
 * times at max. After <b>{@code MAX_CHECKING_ROUND}</b> times re-generation,
 * NameManager will always return a response, even there still exists a
 * duplicate name already. And, if so, the new name will replace the old one.
 *
 * @author zyz
 * @version 2018-10-23
 */
public class NameManager {
    private final static String DEFAULT_NAMESPACE = "";
    private static Integer UNIQUE_NAME_LENGTH = 6;
    private static Long MAX_CHECKING_ROUND = 99999999L;

    private static Map<String, Set<String>> NAME_RECORDS;

    static {
        NAME_RECORDS = Maps.newConcurrentMap();
    }

    /**
     * Set the length of random name.
     *
     * <p>To ensure no duplicate name existing, you can set the length to a big integer.
     *
     * @param UNIQUE_NAME_LENGTH the length of random name
     */
    public static void UNIQUE_NAME_LENGTH(int UNIQUE_NAME_LENGTH) {
        NameManager.UNIQUE_NAME_LENGTH = UNIQUE_NAME_LENGTH;
    }

    /**
     * Set the round of duplicate checking.
     *
     * <p>To ensure no duplicate name existing, you can set the round to a big integer.
     * But too many rounds would go against performance.
     *
     * @param MAX_CHECKING_ROUND the number of checking round
     */
    public static void MAX_LOOP_ROUND(long MAX_CHECKING_ROUND) {
        NameManager.MAX_CHECKING_ROUND = MAX_CHECKING_ROUND > -1 ? MAX_CHECKING_ROUND : 0L;
    }

    /**
     * Generate a unique name under DEFAULT_NAMESPACE.
     *
     * @return a unique name under DEFAULT_NAMESPACE
     */
    public static String uniqueName() {
        return uniqueName(DEFAULT_NAMESPACE);
    }

    /**
     * Generate a unique name under namespace
     *
     * @param namespace namespace
     * @return a unique name under namespace
     */
    public static String uniqueName(String namespace) {
        long loop = 0L;
        String name = null;
        while (loop++ < MAX_CHECKING_ROUND && (name == null || (NAME_RECORDS.containsKey(namespace) && NAME_RECORDS.get(namespace).contains(name)))) {
            name = String.format("%s", RandomStringUtils.randomAlphanumeric(UNIQUE_NAME_LENGTH));
        }
        if (!NAME_RECORDS.containsKey(namespace)) {
            NAME_RECORDS.put(namespace, Collections.newSetFromMap(Maps.newConcurrentMap()));
        }
        NAME_RECORDS.get(namespace).add(name);
        return namespace.equals(DEFAULT_NAMESPACE) ? name : String.format("%s.%s", namespace, name);
    }

    /**
     * Release the name under DEFAULT_NAMESPACE after using.
     *
     * @param name name to release
     */
    public static void release(String name) {
        release(DEFAULT_NAMESPACE, name);
    }

    /**
     * Release the name under namespace after using.
     *
     * @param namespace namespace that name belongs to
     * @param name      name to release
     */
    public static void release(String namespace, String name) {
        if (NAME_RECORDS.containsKey(namespace)) {
            String subName = StringUtils.substringAfter(name, namespace + ".");
            if (subName != null) {
                NAME_RECORDS.get(namespace).remove(subName);
            }
        }
    }
}
