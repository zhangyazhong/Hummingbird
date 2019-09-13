package cn.sissors.hummingbird.collect.container;

import cn.sissors.hummingbird.bean.ResultTimeline;
import cn.sissors.hummingbird.bean.ResultUnit;
import cn.sissors.hummingbird.collect.TableContainer;
import cn.sissors.hummingbird.exceptions.DataLoadingException;
import cn.sissors.hummingbird.exceptions.DataPersistenceException;
import cn.sissors.hummingbird.exceptions.NetworkTransferException;
import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

/**
 * @author zyz
 * @version 2018-10-13
 */
@FixMethodOrder(NAME_ASCENDING)
public class CSVTableContainerTest {
    private CSVTableContainer<String, String, String> csvTableContainer;

    @Before
    public void init() {
        csvTableContainer = new CSVTableContainer<>("time");
        csvTableContainer.push("1:00", "cost", "10ms");
        csvTableContainer.push("1:00", "count", "100");
        csvTableContainer.push("2:00", "cost", "20ms");
        csvTableContainer.push("2:00", "count", "200");
        csvTableContainer.push("3:00", "count", "300");
    }

    @Test
    public void test0SimplePrint() {
        csvTableContainer.print();
        assertTrue(true);
    }

    @Test
    public void test1SimplePersist() throws DataPersistenceException {
        csvTableContainer.persist("./persistence/simple-container.csv");
        assertTrue(true);
    }

    @Test
    public void test2SimpleLoad() throws DataLoadingException {
        csvTableContainer = new CSVTableContainer<>(String.class, String.class, String.class).load("./persistence/simple-container.csv");
        csvTableContainer.print();
        assertTrue(true);
    }

    @Test
    public void test0ComplicatedPrint() {
        CSVTableContainer<String, String, JSONObject> csvTableContainer =
                new CSVTableContainer<>("time", String.class, String.class, JSONObject.class);
        JSONObject result = new JSONObject(ImmutableMap.of(
                "time", "10ms",
                "memory", "15MB"
        ));
        csvTableContainer.push("1:00", "cost", result);
        csvTableContainer.push("1:00", "count", result);
        csvTableContainer.push("2:00", "cost", result);
        csvTableContainer.push("2:00", "count", result);
        csvTableContainer.push("3:00", "cost", result);
        csvTableContainer.print();
        assertTrue(true);
    }

    @Test
    public void test1ComplicatedPersist() throws DataPersistenceException {
        CSVTableContainer<String, String, JSONObject> csvTableContainer =
                new CSVTableContainer<>("time", String.class, String.class, JSONObject.class);
        JSONObject result = new JSONObject(ImmutableMap.of(
                "time", "10ms",
                "memory", "15MB"
        ));
        csvTableContainer.push("1:00", "cost", result);
        csvTableContainer.push("1:00", "count", result);
        csvTableContainer.push("2:00", "cost", result);
        csvTableContainer.push("2:00", "count", result);
        csvTableContainer.push("3:00", "cost", result);
        csvTableContainer.persist("./persistence/complicated-container.csv");
        assertTrue(true);
    }

    @Test
    public void test2ComplicatedLoad() throws DataLoadingException {
        CSVTableContainer<String, String, JSONObject> csvTableContainer =
                new CSVTableContainer<>(String.class, String.class, JSONObject.class).load("./persistence/complicated-container.csv");
        csvTableContainer.print();
        assertTrue(true);
    }

    @Test
    public void test1CustomizedPersist() throws DataPersistenceException {
        CSVTableContainer<String, String, ResultUnit> csvTableContainer =
                new CSVTableContainer<>("data-scale", String.class, String.class, ResultUnit.class);
        csvTableContainer.push("1:00", "cost", new ResultUnit(50, 20));
        csvTableContainer.push("1:30", "count", new ResultUnit(70, 15));
        csvTableContainer.push("2:00", "cost", new ResultUnit(80, 30));
        csvTableContainer.push("2:00", "count", new ResultUnit(100, 25));
        csvTableContainer.push("3:00", "cost", new ResultUnit(120, 35));
        csvTableContainer.print();
        csvTableContainer.persist("./persistence/customized-container.csv");
        assertTrue(true);
    }

    @Test
    public void test2CustomizedLoad() throws DataLoadingException {
        CSVTableContainer<String, String, ResultUnit> csvTableContainer =
                new CSVTableContainer<>(String.class, String.class, ResultUnit.class).load("./persistence/customized-container.csv");
        csvTableContainer.print();
        assertTrue(true);
    }

    @Test
    public void test1CustomizedPersist2() throws DataPersistenceException {
        CSVTableContainer<ResultTimeline, String, ResultUnit> csvTableContainer =
                new CSVTableContainer<>(ResultTimeline.class, String.class, ResultUnit.class);
        csvTableContainer.push(new ResultTimeline(1995, 7, 12), "cost", new ResultUnit(50, 20));
        csvTableContainer.push(new ResultTimeline(1995, 8, 17), "count", new ResultUnit(70, 15));
        csvTableContainer.push(new ResultTimeline(1995, 9, 8), "cost", new ResultUnit(80, 30));
        csvTableContainer.push(new ResultTimeline(1996, 2, 23), "count", new ResultUnit(100, 25));
        csvTableContainer.push(new ResultTimeline(1997, 5, 1), "cost", new ResultUnit(120, 35));
        csvTableContainer.print();
        csvTableContainer.persist("./persistence/customized-container2.csv");
    }

    @Test
    public void test2CustomizedLoad2() throws DataLoadingException {
        CSVTableContainer<ResultTimeline, String, ResultUnit> csvTableContainer =
                new CSVTableContainer<>(ResultTimeline.class, String.class, ResultUnit.class).load("./persistence/customized-container2.csv");
        csvTableContainer.print();
        assertTrue(true);
    }

    @Test
    public void test0Sort() {
        CSVTableContainer<String, String, ResultUnit> csvTableContainer =
                new CSVTableContainer<>("time", String.class, String.class, ResultUnit.class);
        csvTableContainer.push("2:00", "cost", new ResultUnit(2, 0));
        csvTableContainer.push("2:00", "count", new ResultUnit(2, 1));
        csvTableContainer.push("1:00", "cost", new ResultUnit(1, 0));
        csvTableContainer.push("1:00", "count", new ResultUnit(1, 1));
        csvTableContainer.push("3:00", "cost", new ResultUnit(3, 0));
        csvTableContainer.print();
        csvTableContainer.sort().print();
        assertTrue(true);
    }

    @Test
    public void test0Sort2() {
        CSVTableContainer<String, String, ResultUnit> csvTableContainer =
                new CSVTableContainer<>("time", String.class, String.class, ResultUnit.class);
        csvTableContainer.push("2:00", "cost", new ResultUnit(2, 0));
        csvTableContainer.push("2:00", "count", new ResultUnit(2, 1));
        csvTableContainer.push("1:00", "cost", new ResultUnit(1, 0));
        csvTableContainer.push("1:00", "count", new ResultUnit(1, 1));
        csvTableContainer.push("3:00", "cost", new ResultUnit(3, 0));
        csvTableContainer.print();
        csvTableContainer.sort((s1, s2) -> -s1.compareTo(s2), null).print();
        csvTableContainer.sort(null, (s1, s2) -> -s1.compareTo(s2)).print();
        csvTableContainer.sort(String::compareTo, Comparator.naturalOrder()).print();
        assertTrue(true);
    }

    @Test
    public void test0Merge() {
        CSVTableContainer<String, String, ResultUnit> csvTableContainer =
                new CSVTableContainer<>("time", String.class, String.class, ResultUnit.class);
        csvTableContainer.push("2:00", "result_1", new ResultUnit(2.5, 0.3));
        csvTableContainer.push("2:00", "result_0", new ResultUnit(6, 1.1));
        csvTableContainer.push("1:00", "result_1", new ResultUnit(3.2, 0.4));
        csvTableContainer.push("1:00", "result_0", new ResultUnit(0.8, 0.1));
        csvTableContainer.push("3:00", "result_0", new ResultUnit(9, 1.5));
        csvTableContainer.print();
        CSVTableContainer<String, String, ResultUnit> csvTableContainer2 =
                new CSVTableContainer<>("time", String.class, String.class, ResultUnit.class);
        csvTableContainer2.push("4:00", "result_1", new ResultUnit(11, 2.4));
        csvTableContainer2.push("0:00", "result_0", new ResultUnit(8, 0.9));
        csvTableContainer2.push("0:00", "result_1", new ResultUnit(4.8, 1));
        csvTableContainer2.push("4:00", "result_0", new ResultUnit(7.5, 1.8));
        csvTableContainer2.push("5:00", "result_1", new ResultUnit(2, 0.6));
        csvTableContainer2.print();
        csvTableContainer.merge(csvTableContainer2);
        csvTableContainer.sort().print();
        assertTrue(true);
    }

    @Test
    public void test0Filter() {
        CSVTableContainer<String, String, ResultUnit> csvTableContainer =
                new CSVTableContainer<>("time", String.class, String.class, ResultUnit.class);
        csvTableContainer.push("2:00", "result_1", new ResultUnit(2.5, 0.3));
        csvTableContainer.push("2:00", "result_0", new ResultUnit(6, 1.1));
        csvTableContainer.push("1:00", "result_1", new ResultUnit(3.2, 0.4));
        csvTableContainer.push("1:00", "result_0", new ResultUnit(0.8, 0.1));
        csvTableContainer.push("3:00", "result_0", new ResultUnit(9, 1.5));
        csvTableContainer.print();
        CSVTableContainer<String, String, ResultUnit> csvTableContainer2 =
                new CSVTableContainer<>("time", String.class, String.class, ResultUnit.class);
        csvTableContainer2.push("4:00", "result_1", new ResultUnit(11, 2.4));
        csvTableContainer2.push("0:00", "result_0", new ResultUnit(8, 0.9));
        csvTableContainer2.push("0:00", "result_1", new ResultUnit(4.8, 1));
        csvTableContainer2.push("4:00", "result_0", new ResultUnit(7.5, 1.8));
        csvTableContainer2.push("5:00", "result_1", new ResultUnit(2, 0.6));
        csvTableContainer2.print();
        csvTableContainer.merge(csvTableContainer2);
        csvTableContainer.filter(rowKey -> rowKey.compareTo("4:00") < 0, columnKey -> true).print();
        assertTrue(true);
    }

    @Test
    public void testContainerMisc() {
        CSVTableContainer<String, String, String> csvTableContainer = new CSVTableContainer<>();
        System.out.println("local directory: " + csvTableContainer.localStorageDir());
        System.out.println(csvTableContainer.newLine("\n").newLine());
        System.out.println("separator: " + csvTableContainer.separator(",").separator());
        System.out.println("empty character: " + csvTableContainer.NULL_CHARACTER_DISPLAY("(empty)").NULL_CHARACTER_DISPLAY());
        System.out.println("container count: " + TableContainer.CONTAINER_COUNT());
        assertTrue(true);
    }

    @Test
    public void testSetCellType() {
        CSVTableContainer<String, String, String> csvTableContainer = new CSVTableContainer<>();
        csvTableContainer.setColumnType(String.class);
        csvTableContainer.setRowType(String.class);
        csvTableContainer.setValueType(String.class);
        assertTrue(true);
    }

    @Test
    public void testGetContainerMap() {
        CSVTableContainer<String, String, String> csvTableContainer = new CSVTableContainer<>();
        csvTableContainer.push("row", "column", "value");
        csvTableContainer.rowMap();
        csvTableContainer.columnMap();
        csvTableContainer.singleRow("column");
        csvTableContainer.singleColumn("row");
        assertTrue(true);
    }

    @Test
    public void testRemoteProfile() {
        try {
            CSVTableContainer.RemoteProfile legalRemoteProfile
                    = CSVTableContainer.RemoteProfile.resolve("hadoop.123@master:22:~/csv-data/result.csv");
            System.out.println(legalRemoteProfile);
            CSVTableContainer.RemoteProfile illegalRemoteProfile
                    = CSVTableContainer.RemoteProfile.resolve("hadoop.123@@master:22:~/csv-data/result.csv");
            System.out.println(illegalRemoteProfile);
        } catch (NetworkTransferException ignored) {
        }
        assertTrue(true);
    }

    @Test
    @Ignore
    public void testRemoteLoad() throws DataLoadingException {
        CSVTableContainer<String, String, String> csvTableContainer =
                new CSVTableContainer<>("time", String.class, String.class, String.class);
        csvTableContainer.load("scidb.he@10.141.211.91:22:/tmp/ada/exp/exp19/resample_result_20.csv").print();
        csvTableContainer.load("./persistence/simple-container.csv").print();
        assertTrue(true);
    }

    @Test
    @Ignore
    public void testRemoteCache() throws DataLoadingException, InterruptedException {
        CSVTableContainer<String, String, String> csvTableContainer =
                new CSVTableContainer<>("time", String.class, String.class, String.class);
        csvTableContainer.load("scidb.he@10.141.211.91:22:/tmp/ada/exp/exp20/accurate_result_24_168_6.csv").print();
        Thread.sleep(1000);
        csvTableContainer.load("scidb.he@10.141.211.91:22:/tmp/ada/exp/exp20/accurate_result_24_312_12.csv").print();
        Thread.sleep(1000);
        csvTableContainer.load("scidb.he@10.141.211.91:22:/tmp/ada/exp/exp20/accurate_result_48_72_1.csv").print();
        Thread.sleep(1000);
        csvTableContainer.load("scidb.he@10.141.211.91:22:/tmp/ada/exp/exp20/accurate_result_168_192_1.csv").print();
        Thread.sleep(1000);
        csvTableContainer.load("scidb.he@10.141.211.91:22:/tmp/ada/exp/exp20/accurate_result_168_312_6.csv").print();
        Thread.sleep(1000);
        csvTableContainer.load("scidb.he@10.141.211.91:22:/tmp/ada/exp/exp20/accurate_result_168_456_12.csv").print();
        Thread.sleep(1000);
        csvTableContainer.load("scidb.he@10.141.211.91:22:/tmp/ada/exp/exp20/accurate_result_168_504_24.csv").print();
        assertTrue(true);
    }
}