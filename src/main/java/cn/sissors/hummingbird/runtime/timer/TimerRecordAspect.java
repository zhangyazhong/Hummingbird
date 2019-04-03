package cn.sissors.hummingbird.runtime.timer;

import com.google.common.collect.Lists;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;
import java.util.Map;

/**
 * The aspect working for {@link TimerRecord} annotation.
 *
 * <p>It monitors all methods whose annotations contain {@link TimerRecord}.
 * Before method running, a timer will be created and after running, that
 * timer will be stopped. The timer is recorded in {@link Timer}.
 *
 * @author zyz
 * @version 2018-11-18
 */
@Aspect
public class TimerRecordAspect {
    @Pointcut("execution(* *(..)) && @annotation(timerRecord)")
    private void reportTimePointcut(TimerRecord timerRecord) {
    }

    @Before(value = "reportTimePointcut(timerRecord)", argNames = "timerRecord")
    public void monitorTimeStart(TimerRecord timerRecord) {
        TimerManager.create(timerRecord.value());
    }

    @After(value = "reportTimePointcut(timerRecord)", argNames = "timerRecord")
    public void monitorTimeStop(TimerRecord timerRecord) {
        TimerManager.stop(timerRecord.value());
    }
}
