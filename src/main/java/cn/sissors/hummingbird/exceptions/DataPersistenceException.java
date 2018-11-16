package cn.sissors.hummingbird.exceptions;

/**
 * This exception indicates that error appearance in persist data to external storage process.
 *
 * <p>Reasons are as follows:
 *
 * <p>1. IOException, such as illegal file path;
 * <p>2. network transmission error when using remote server;
 * <p>...
 *
 * @author zyz
 * @version 2018-10-16
 */
public class DataPersistenceException extends Exception {
    public DataPersistenceException(String message) {
        super(message);
    }
}
