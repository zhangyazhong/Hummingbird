package cn.sissors.hummingbird.runtime.namespace;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.junit.runners.MethodSorters.NAME_ASCENDING;

/**
 * @author zyz
 * @version 2018-10-23
 */
@FixMethodOrder(NAME_ASCENDING)
public class NameManagerTest {
    private String defaultName;
    private String mainName;

    @Test
    public void test0UniqueName() {
        System.out.println(defaultName = NameManager.uniqueName());
    }

    @Test
    public void test1UniqueName() {
        System.out.println(mainName = NameManager.uniqueName("main"));
    }

    @Test
    public void test2Release() {
        NameManager.release(defaultName);
    }

    @Test
    public void test3release() {
        NameManager.release("main", mainName);
    }
}