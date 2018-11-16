package cn.sissors.hummingbird.collect.feature;

/**
 * Sub-classes of this interface indicates that they can be parsed by text string.
 *
 * <p>It's a little similar to {@link java.io.Serializable}.
 * But this interface requests that text string dumped by objects are in human readable format.
 *
 * @author zyz
 * @version 2018-10-13
 */
public interface Parsable<T> {
    T parse(String text);
    @Override
    String toString();
}
