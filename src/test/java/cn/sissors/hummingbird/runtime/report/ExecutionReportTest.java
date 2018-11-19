package cn.sissors.hummingbird.runtime.report;

import cn.sissors.hummingbird.bean.ResultUnit;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * @author zyz
 * @version 2018-11-16
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExecutionReportTest {
    private ExecutionReport report;

    @Before
    public void init() {
        report = ExecutionReport.create();
        report.put("round0.partA.result", new ResultUnit(5, 3));
        report.put("round0.partB.result", new ResultUnit(4, 0.5));
        report.put("round1.partA.result", new ResultUnit(2, 0));
        report.put("round1.partB.result", new ResultUnit(1, 1));
    }

    @Test
    public void test1Print() {
        report.print();
        assertEquals(report.get("round0.partA.result", ResultUnit.class).toString(), "r=5.0, e=3.0");
    }

    @Test
    public void test1Search() {
        report.search(".*\\.partA\\..*").print();
        report.search("round1\\..*").print();
        assertEquals(report.search("round1\\..*").get("round1.partA.result", ResultUnit.class).toString(), "r=2.0, e=0.0");
    }

    @Test
    public void test1Category() {
        report.getCategory("round0").print();
        assertEquals(report.getCategory("round0").get("round0.partA.result", ResultUnit.class).toString(), "r=5.0, e=3.0");
    }

    @Test
    public void test1Merge() {
        ExecutionReport report2 = ExecutionReport.create(ImmutableMap.of(
                "round2.partA.result", new ResultUnit(4, 2),
                "round2.partB.result", new ResultUnit(9, 4.5),
                "round1.partB.result", new ResultUnit(3, 1)
        ));
        report.merge(report2).print();
        assertEquals(report.get("round1.partB.result", ResultUnit.class).toString(), "r=3.0, e=1.0");
    }

    @Test
    public void test0ForEach() {
        AtomicInteger count = new AtomicInteger(0);
        report.forEach((key, value) -> {
            System.out.println(String.format("%s -> %s", key, value.toString()));
            count.getAndIncrement();
        });
        assertEquals(4, count.get());
    }

    @Test
    public void test2Get() {
        ExecutionReport _report = ExecutionReport.create();
        _report.put("int", 1);
        _report.put("double", 2.5);
        _report.put("long", 3L);
        _report.put("string", "hello");
        _report.put("result", new ResultUnit(4, 2));
        _report.print();
        System.out.println(_report.getInt("int"));
        System.out.println(_report.getDouble("double"));
        System.out.println(_report.getLong("long"));
        System.out.println(_report.getString("string"));
        System.out.println(_report.get("result", ResultUnit.class));
        assertEquals(3L, (long) _report.getLong("long"));
    }

    @Test
    public void test2Put() {
        ExecutionReport _report = ExecutionReport.create();
        _report.put(ImmutableMap.of("int", 1, "double", 2.5));
        _report.put("long", 3L);
        _report.print();
        assertEquals(3L, (long) _report.getLong("long"));
    }
}