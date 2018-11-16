package cn.sissors.hummingbird.collect.container;

import cn.sissors.hummingbird.collect.TableContainer;
import cn.sissors.hummingbird.exceptions.DataLoadingException;
import cn.sissors.hummingbird.exceptions.DataPersistenceException;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.junit.runners.MethodSorters.NAME_ASCENDING;

/**
 * @author zyz
 * @version 2018-10-25
 */
@FixMethodOrder(NAME_ASCENDING)
public class BinaryTableContainerTest {
    @Test
    public void test0Persist() throws DataPersistenceException {
        TableContainer<String, String, String> binaryTableContainer = new BinaryTableContainer<>("time");
        binaryTableContainer.push("1:00", "cost", "10ms");
        binaryTableContainer.push("1:00", "count", "100");
        binaryTableContainer.push("2:00", "cost", "20ms");
        binaryTableContainer.push("2:00", "count", "200");
        binaryTableContainer.push("3:00", "count", "300");
        binaryTableContainer.print();
        binaryTableContainer.persist("./persistence/binary-container");
    }

    @Test
    public void test1Load() throws DataLoadingException {
        TableContainer<String, String, String> binaryTableContainer =
                new BinaryTableContainer<String, String, String>("time").load("./persistence/binary-container");
        binaryTableContainer.print();
    }

}