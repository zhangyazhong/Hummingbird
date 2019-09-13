package cn.sissors.hummingbird.collect;

import cn.sissors.hummingbird.annotions.CanIgnoreReturnValue;
import cn.sissors.hummingbird.exceptions.ContainerRuntimeException;
import cn.sissors.hummingbird.exceptions.DataLoadingException;
import cn.sissors.hummingbird.exceptions.DataPersistenceException;
import cn.sissors.hummingbird.exceptions.IllegalValueTypeException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A collection that associates an ordered pair of keys, called a row key and a
 * column key, with a single value. A table may be sparse, with only a small
 * fraction of row key / column key pairs possessing a corresponding value.
 *
 * @author zyz
 * @version 2018-10-12
 */
public abstract class TableContainer<R, C, V> implements Cloneable, Serializable {
    private String NULL_CHARACTER_DISPLAY = "-";

    private static Long CONTAINER_ID;

    private String headerName;
    private Map<R, Map<C, V>> rowMap;
    private Map<C, Map<R, V>> columnMap;

    static {
        CONTAINER_ID = 0L;
    }

    /**
     * Container id is an auto-increment attribute for identifying a container.
     * The value of id just equals to the count of container that exists in memory.
     *
     * @return container count exists in memory
     */
    public static Long CONTAINER_COUNT() {
        return CONTAINER_ID;
    }

    /**
     * Used for printing through console.
     *
     * <p>The NULL_CHARACTER_DISPLAY is used to replace NULL value.
     *
     * @return the character replaced for NULL
     */
    public String NULL_CHARACTER_DISPLAY() {
        return NULL_CHARACTER_DISPLAY;
    }

    /**
     * Set customized NULL_CHARACTER_DISPLAY to replace NULL value when printing.
     *
     * @param NULL_CHARACTER_DISPLAY the character replaced
     * @return the character replaced for NULL
     */
    @CanIgnoreReturnValue
    public String NULL_CHARACTER_DISPLAY(String NULL_CHARACTER_DISPLAY) {
        return this.NULL_CHARACTER_DISPLAY = NULL_CHARACTER_DISPLAY;
    }

    /**
     * Get the table organized by row keys.
     *
     * @return a nested map whose structure is (row, (column, value))
     */
    public Map<R, Map<C, V>> rowMap() {
        return rowMap;
    }

    /**
     * Get the table organized by column keys.
     *
     * @return a nested map whose structure is (column, (row, value))
     */
    public Map<C, Map<R, V>> columnMap() {
        return columnMap;
    }

    /**
     * Get a single row based on row key.
     *
     * @param row row key
     * @return a map whose structure is (column, value)
     */
    public Map<C, V> singleRow(R row) {
        return rowMap().get(row);
    }

    /**
     * Get a single column based on column key.
     *
     * @param column column key
     * @return a map whose structure is (row, value)
     */
    public Map<R, V> singleColumn(C column) {
        return columnMap().get(column);
    }

    /**
     * Get row keys organized as a {@link java.util.List}.
     *
     * @return a list that contains all row keys
     */
    public List<R> rowKeys() {
        return Lists.newLinkedList(rowMap.keySet());
    }

    /**
     * Get column keys organized as a {@link java.util.List}.
     *
     * @return a list that contains all column keys
     */
    public List<C> columnKeys() {
        return Lists.newLinkedList(columnMap.keySet());
    }

    private TableContainer() {
        CONTAINER_ID++;
        this.rowMap = Maps.newLinkedHashMap();
        this.columnMap = Maps.newLinkedHashMap();
    }

    public TableContainer(String headerName) {
        this();
        this.headerName = headerName;
    }

    /**
     * Push a cell into container.
     *
     * @param row    row key
     * @param column column key
     * @param value  cell value
     * @return container itself (easy to invoke under chain-style)
     */
    @CanIgnoreReturnValue
    public TableContainer<R, C, V> push(R row, C column, V value) {
        rowMap.computeIfAbsent(row, k -> Maps.newLinkedHashMap());
        rowMap.get(row).put(column, value);
        columnMap.computeIfAbsent(column, k -> Maps.newLinkedHashMap());
        columnMap.get(column).put(row, value);
        return this;
    }

    /**
     * Get a cell based on (row, column).
     *
     * @param row    row key
     * @param column column key
     * @return a single cell
     */
    @Nullable
    public V get(R row, C column) {
        V v1 = rowMap.containsKey(row) ? rowMap.get(row).get(column) : null;
        V v2 = columnMap.containsKey(column) ? columnMap.get(column).get(row) : null;
        return v1 != null && v1.equals(v2) ? v1 : null;
    }

    /**
     * Clean all content in the container.
     *
     * @return container itself (easy to invoke under chain-style)
     */
    @CanIgnoreReturnValue
    public TableContainer<R, C, V> clean() {
        this.rowMap = Maps.newLinkedHashMap();
        this.columnMap = Maps.newLinkedHashMap();
        return this;
    }

    /**
     * Persist container data to external permanent storage.
     *
     * @param path external storage path
     * @throws DataPersistenceException error appearance such as {@link java.io.IOException} and so on
     */
    public abstract void persist(String path) throws DataPersistenceException;

    /**
     * Load data from external storage into container.
     *
     * @param path external storage path
     * @return the container that has been loaded
     * @throws DataLoadingException error appearance such as {@link java.io.IOException},
     *                              {@link IllegalValueTypeException} and so on
     */
    public abstract TableContainer<R, C, V> load(String path) throws DataLoadingException;

    /**
     * Merge two containers into single one and replaced the current.
     *
     * @param other another container with the sample (R, C, V) type
     * @return current container
     */
    @CanIgnoreReturnValue
    public TableContainer<R, C, V> merge(TableContainer<R, C, V> other) {
        other.rowKeys().forEach(rowKey -> other.columnKeys().forEach(columnKey -> {
            V value = other.get(rowKey, columnKey);
            if (value != null) {
                this.push(rowKey, columnKey, value);
            }
        }));
        return this;
    }

    /**
     * Apply filters on the current container. If no filter is applied on the
     * column or the row, pass <code>null</code> to this method.
     *
     * @param rowFilter    the filter applied for row keys
     * @param columnFilter the filter applied for column keys
     * @return current container
     */
    @CanIgnoreReturnValue
    public TableContainer<R, C, V> filter(@Nullable Predicate<R> rowFilter, @Nullable Predicate<C> columnFilter) {
        final Predicate<R> finalRowFilter = rowFilter != null ? rowFilter : (rowKey -> true);
        final Predicate<C> finalColumnFilter = columnFilter != null ? columnFilter : (columnKey -> true);
        TableContainer<R, C, V> other = this.clone();
        this.clean();
        other.rowKeys().stream().filter(finalRowFilter).forEach(rowKey
                -> other.columnKeys().stream().filter(finalColumnFilter).forEach(columnKey
                -> this.push(rowKey, columnKey, other.get(rowKey, columnKey))));
        return this;
    }

    /**
     * Sort current container based on the dictionary order of row and column keys.
     *
     * @return sorted container
     */
    public TableContainer<R, C, V> sort() {
        return sort(Comparator.comparing(Object::toString), Comparator.comparing(Object::toString));
    }

    /**
     * Sort current container based on the given comparators of row and column keys.
     * Comparator of <code>Null</code> value means not to sort.
     *
     * @param rowComparator    the comparator applied for row keys
     * @param columnComparator the comparator applied for column keys
     * @return sorted container
     * @since 1.3.0
     */
    public TableContainer<R, C, V> sort(@Nullable Comparator<R> rowComparator, @Nullable Comparator<C> columnComparator) {
        TableContainer<R, C, V> other = this.clone();
        this.clean();
        List<R> orderedRowKeys = other.rowKeys();
        List<C> orderedColumnKeys = other.columnKeys();
        if (rowComparator != null) {
            orderedRowKeys.sort(rowComparator);
        }
        if (columnComparator != null) {
            orderedColumnKeys.sort(columnComparator);
        }
        orderedRowKeys.forEach(rowKey
                -> orderedColumnKeys.forEach(columnKey
                -> push(rowKey, columnKey, other.get(rowKey, columnKey))));
        return this;
    }

    /**
     * Print the container to the console.
     */
    public void print() {
        print(System.out);
    }

    /**
     * Print the container to the specified print stream.
     *
     * @param printStream the stream to receive output
     */
    public void print(PrintStream printStream) {
        printStream.println(toString());
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TableContainer<R, C, V> clone() {
        try {
            return (TableContainer<R, C, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        throw new ContainerRuntimeException("the types of table don't support to clone");
    }

    @Override
    public String toString() {
        AsciiTable asciiTable = new AsciiTable();
        List<String> columns = Lists.newLinkedList();
        columns.add(getHeaderName());
        columns.addAll(columnKeys().stream().map(Object::toString).collect(Collectors.toList()));
        asciiTable.addRule();
        asciiTable.addRow(columns);
        rowKeys().forEach(rowKey -> {
            List<String> cells = Lists.newLinkedList();
            cells.add(rowKey.toString());
            columnKeys().forEach(columnKey -> {
                V cell = get(rowKey, columnKey);
                cells.add(cell != null ? cell.toString() : NULL_CHARACTER_DISPLAY());
            });
            asciiTable.addRule();
            asciiTable.addRow(cells);
        });
        asciiTable.addRule();
        asciiTable.getRenderer().setCWC(new CWC_LongestLine());
        return asciiTable.render();
    }
}