package cn.sissors.hummingbird.runtime.namespace;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
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
        assertTrue(StringUtils.isAlphanumeric(defaultName));
    }

    @Test
    public void test1UniqueName() {
        System.out.println(mainName = NameManager.uniqueName("main"));
        assertTrue(mainName.contains("main."));
    }

    @Test
    public void test2Release() {
        defaultName = NameManager.uniqueName();
        NameManager.release(defaultName);
        assertTrue(StringUtils.isAlphanumeric(defaultName));
    }

    @Test
    public void test3release() {
        mainName = NameManager.uniqueName("main");
        NameManager.release("main", mainName);
        assertTrue(mainName.contains("main."));
    }
}