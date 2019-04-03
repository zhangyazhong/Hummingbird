package cn.sissors.hummingbird.runtime.timer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TimerRecord is a tool to record time cost for a method through annotations.
 * It takes advantage of AOP (Aspect Oriented Programming) to increase modularity
 * by adding additional behavior to existing code (an advice) without modifying
 * the code itself.
 *
 * @author zyz
 * @version 2018-11-18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimerRecord {
    /**
     * The timer name to save the time cost.
     *
     * @return timer name, default {@code Timer.EMPTY_NAME}
     */
    String value() default TimerManager.EMPTY_NAME;
}
