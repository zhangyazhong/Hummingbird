package cn.sissors.hummingbird.exceptions;

/**
 * This exception indicates that error appearance in loading data from external storage process.
 *
 * <p>Reasons are as follows:
 *
 * <p>1. IOException, such as illegal file path;
 * <p>2. row, column or value type not supported;
 * <p>...
 *
 * @author zyz
 * @version 2018-10-16
 */
public class DataLoadingException extends Exception {
    public DataLoadingException(String message) {
        super(message);
    }
}
