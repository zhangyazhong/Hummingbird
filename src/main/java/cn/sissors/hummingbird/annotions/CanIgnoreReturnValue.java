package cn.sissors.hummingbird.annotions;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Indicates that the return value of the annotated method can be safely ignored.
 *
 * <p>This is the opposite of {@link javax.annotation.CheckReturnValue}. It can be used inside
 * classes or packages annotated with {@link javax.annotation.CheckReturnValue} to exempt specific methods from
 * the default.
 *
 * @author zyz
 * @version 2018-10-12
 */
@Target({METHOD, TYPE})
@Retention(CLASS)
public @interface CanIgnoreReturnValue {
}
