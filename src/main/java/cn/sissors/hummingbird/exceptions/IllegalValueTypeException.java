package cn.sissors.hummingbird.exceptions;

/**
 * This exception indicates that the value is not illegal.
 *
 * <p>Reasons are as follows:
 *
 * <p>1. value format error;
 * <p>2. out of legal range;
 * <p>3. value type not supported;
 * <p>...
 *
 * @author zyz
 * @version 2018-10-14
 */
public class IllegalValueTypeException extends ReflectiveOperationException {
    private static final long serialVersionUID = 6028356102347013750L;

    public IllegalValueTypeException(String message) {
        super(message);
    }
}
