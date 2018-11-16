package cn.sissors.hummingbird.runtime.report;

import cn.sissors.hummingbird.bean.ResultUnit;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
    }

    @Test
    public void test1Search() {
        report.search(".*\\.partA\\..*").print();
        report.search("round1\\..*").print();
    }

    @Test
    public void test1Merge() {
        ExecutionReport report2 = ExecutionReport.create(ImmutableMap.of(
                "round2.partA.result", new ResultUnit(4, 2),
                "round2.partB.result", new ResultUnit(9, 4.5),
                "round1.partB.result", new ResultUnit(3, 1)
        ));
        report.merge(report2).print();
    }

    @Test
    public void test1ForEach() {
        report.forEach((key, value) -> System.out.println(String.format("%s -> %s", key, value.toString())));
    }
}