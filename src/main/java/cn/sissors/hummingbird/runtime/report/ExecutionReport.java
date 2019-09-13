package cn.sissors.hummingbird.runtime.report;

import cn.sissors.hummingbird.collect.container.CSVTableContainer;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

/**
 * ExecutionReport is a tool to store execution stats during the running of
 * program. It's in key-value format, like {@link Map}. Besides, it provides
 * a series of additional operations, such as regex searching and lambda 
 * functions ({@link #forEach(BiConsumer)}, {@link #sort(Comparator)}) to
 * improve convenience.
 *
 * @author zyz
 * @version 2018-10-25
 */
public class ExecutionReport implements Serializable {
    private Map<String, Object> reports;

    private ExecutionReport() {
        reports = Maps.newConcurrentMap();
    }

    private ExecutionReport(Map<String, Object> reports) {
        this();
        this.reports.putAll(reports);
    }

    /**
     * Create a new empty report.
     *
     * @return a new report object
     */
    @NotNull
    @Contract(" -> new")
    public static ExecutionReport create() {
        return new ExecutionReport();
    }

    /**
     * Create a new report which contains given values.
     *
     * @param reports the given values
     * @return a new report object
     */
    @NotNull
    @Contract("_ -> new")
    public static ExecutionReport create(Map<String, Object> reports) {
        return new ExecutionReport(reports);
    }

    /**
     * Associate the specified value with the specified key in the report.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    public void put(String key, Object value) {
        this.reports.put(key, value);
    }

    /**
     * Add a series of key-value pairs in the report.
     *
     * @param reports a series of key-value pairs
     */
    public void put(Map<String, Object> reports) {
        this.reports.putAll(reports);
    }

    /**
     * Get the value associated with the specified key.
     *
     * @param key the specified key
     * @return value associated with the specified key
     */
    public Object get(String key) {
        return reports.get(key);
    }

    /**
     * Get the value associated with the specified key in {@link java.lang.String} type.
     *
     * @param key the specified key
     * @return value associated with the specified key
     */
    public String getString(String key) {
        return reports.containsKey(key) ? reports.get(key).toString() : null;
    }

    /**
     * Get the value associated with the specified key in {@link java.lang.Integer} type.
     *
     * @param key the specified key
     * @return value associated with the specified key
     */
    public Integer getInt(String key) {
        return reports.containsKey(key) ? (Integer) reports.get(key) : null;
    }

    /**
     * Get the value associated with the specified key in {@link java.lang.Long} type.
     *
     * @param key the specified key
     * @return value associated with the specified key
     */
    public Long getLong(String key) {
        return reports.containsKey(key) ? (Long) reports.get(key) : null;
    }

    /**
     * Get the value associated with the specified key in {@link java.lang.Double} type.
     *
     * @param key the specified key
     * @return value associated with the specified key
     */
    public Double getDouble(String key) {
        return reports.containsKey(key) ? (Double) reports.get(key) : null;
    }

    /**
     * Get the value associated with the specified key in generic <b>T</b> type.
     *
     * @param key   the specified key
     * @param clazz target type class
     * @param <T>   target object type
     * @return value associated with the specified key
     */
    public <T> T get(String key, Class<T> clazz) {
        return reports.containsKey(key) ? clazz.cast(reports.get(key)) : null;
    }

    /**
     * Get the entire report map.
     *
     * @return the entire report map
     */
    public Map<String, Object> getReport() {
        return reports;
    }

    /**
     * Get a sub-report which only contains the given category.
     *
     * <p>For example, the origin report contains 5 elements:
     * <ul>
     *     <li><code>(A, 1)</code></li>
     *     <li><code>(A.a, 2)</code></li>
     *     <li><code>(A.b, 3)</code></li>
     *     <li><code>(B, 4)</code></li>
     *     <li><code>(B.a, 5)</code></li>
     * </ul>
     * Then, through <code>getCategory("A")</code>, we will get a new
     * sub-report which only contains 3 elements:
     * <ul>
     *     <li><code>(A, 1)</code></li>
     *     <li><code>(A.a, 2)</code></li>
     *     <li><code>(A.b, 3)</code></li>
     * </ul>
     *
     * @param category the specified category
     * @return a sub-report
     */
    public ExecutionReport getCategory(String category) {
        return search(category).merge(search(category + "\\..*"));
    }

    /**
     * Get a sub-report matches the given regex pattern.
     *
     * <p>For example, the origin report contains 4 elements:
     * <ul>
     *     <li><code>(T.u.a, 1)</code></li>
     *     <li><code>(T.u.b, 2)</code></li>
     *     <li><code>(T.v.a, 3)</code></li>
     *     <li><code>(T.v.b, 4)</code></li>
     * </ul>
     * Then, through <code>search("T\\..+\\.a")</code>, we will get a
     * new sub-report which only contains 2 elements:
     * <ul>
     *     <li><code>(T.u.a, 1)</code></li>
     *     <li><code>(T.v.a, 3)</code></li>
     * </ul>
     *
     * @param pattern the regex formula
     * @return a sub-report
     */
    public ExecutionReport search(String pattern) {
        Map<String, Object> part = Maps.newConcurrentMap();
        Pattern _pattern = Pattern.compile(pattern);
        reports.keySet().forEach(_key -> {
            if (_pattern.matcher(_key).matches()) {
                part.put(_key, get(_key));
            }
        });
        return new ExecutionReport(part);
    }

    /**
     * Merge another report into this one.
     *
     * <p><b>Notice: </b>If there exists duplicate key, the value in another
     * report will override the value in current report.
     *
     * @param other another report
     * @return report after mergence
     */
    public ExecutionReport merge(ExecutionReport other) {
        reports.putAll(other.reports);
        return this;
    }

    /**
     * Print the container to the console.
     */
    public void print() {
        System.out.println(toString());
    }

    /**
     * Performs the given action for each entry in this map until all entries
     * have been processed or the action throws an exception. Unless
     * otherwise specified by the implementing class, actions are performed in
     * the order of entry set iteration (if an iteration order is specified.)
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each entry
     */
    public void forEach(BiConsumer<String, Object> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<String, Object> entry : reports.entrySet()) {
            String k;
            Object v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    /**
     * Returns a new execution report consisting of all elements, sorted
     * according to the provided {@code Comparator}. The {@code report}
     * map in the new created {@code ExecutionReport} would be changed
     * into {@link java.util.LinkedHashMap} to guarantee order.
     *
     * @param comparator a {@code Comparator<String>} for comparing keys
     *                   in the report
     * @return the new execution report
     * @since 1.3.0
     */
    public ExecutionReport sort(Comparator<String> comparator) {
        ExecutionReport executionReport = ExecutionReport.create();
        executionReport.reports = Maps.newLinkedHashMap();
        this.reports.keySet()
                .stream()
                .sorted(comparator)
                .forEach(key -> executionReport.put(key, this.get(key)));
        return executionReport;
    }

    @Override
    public String toString() {
        CSVTableContainer<String, String, String> table =
                new CSVTableContainer<>("key", String.class, String.class, String.class);
        this.forEach((key, value) -> table.push(key, "value", value.toString()));
        return table.toString();
    }
}
