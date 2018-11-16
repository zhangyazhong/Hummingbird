package cn.sissors.hummingbird.exceptions;

/**
 * This exception indicates that error appearance in network transmission process.
 *
 * <p>Reasons are as follows:
 *
 * <p>1. authentication error: wrong username or password;
 * <p>2. network connection failed;
 * <p>3. remote file not exists or illegal file path;
 * <p>...
 *
 * @author zyz
 * @version 2018-10-22
 */
public class NetworkTransferException extends Exception {
    public NetworkTransferException(String message) {
        super(message);
    }
}
