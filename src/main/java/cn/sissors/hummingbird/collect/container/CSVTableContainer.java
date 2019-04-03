package cn.sissors.hummingbird.collect.container;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import cn.sissors.hummingbird.annotions.CanIgnoreReturnValue;
import cn.sissors.hummingbird.collect.TableContainer;
import cn.sissors.hummingbird.collect.feature.Parsable;
import cn.sissors.hummingbird.exceptions.DataLoadingException;
import cn.sissors.hummingbird.exceptions.DataPersistenceException;
import cn.sissors.hummingbird.exceptions.IllegalValueTypeException;
import cn.sissors.hummingbird.exceptions.NetworkTransferException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * The table container organized under csv format which can be persisted to a csv table and loaded data from csv file.
 *
 * @author zyz
 * @version 2018-10-12
 */
public class CSVTableContainer<R, C, V> extends TableContainer<R, C, V> {
    private final static String LOCAL_STORAGE_DIR = "./persistence/";
    private final static Map<RemoteProfile.RemoteServer, Connection> remoteConnectionCache;

    private String SEPARATOR = "|";
    private String NEW_LINE = "\r\n";
    private Class<R> ROW_TYPE;
    private Class<C> COLUMN_TYPE;
    private Class<V> VALUE_TYPE;

    private final static Map<Class, Function<String, ?>> PARSERS = new ImmutableMap.Builder<Class, Function<String, ?>>()
            .put(Integer.class, Integer::valueOf)
            .put(Long.class, Long::valueOf)
            .put(String.class, String::toString)
            .put(Double.class, Double::valueOf)
            .put(JSONObject.class, JSONObject::new)
            .put(JSONArray.class, JSONArray::new)
            .build();

    static {
        File storageDir = new File(LOCAL_STORAGE_DIR);
        if (!storageDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            storageDir.mkdirs();
        }
        remoteConnectionCache = Maps.newConcurrentMap();
    }

    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public CSVTableContainer() {
        super("");
        TypeToken<R> rTypeToken = new TypeToken<R>(getClass()) {};
        TypeToken<C> cTypeToken = new TypeToken<C>(getClass()) {};
        TypeToken<V> vTypeToken = new TypeToken<V>(getClass()) {};
        this.ROW_TYPE = rTypeToken.getType().getClass().isAssignableFrom(Class.class) ? (Class<R>) rTypeToken.getType() : (Class<R>) String.class;
        this.COLUMN_TYPE = cTypeToken.getType().getClass().isAssignableFrom(Class.class) ? (Class<C>) cTypeToken.getType() : (Class<C>) String.class;
        this.VALUE_TYPE = vTypeToken.getType().getClass().isAssignableFrom(Class.class) ? (Class<V>) vTypeToken.getType() : (Class<V>) String.class;
    }

    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public CSVTableContainer(String headerName) {
        super(headerName);
        TypeToken<R> rTypeToken = new TypeToken<R>(getClass()) {};
        TypeToken<C> cTypeToken = new TypeToken<C>(getClass()) {};
        TypeToken<V> vTypeToken = new TypeToken<V>(getClass()) {};
        this.ROW_TYPE = rTypeToken.getType().getClass().isAssignableFrom(Class.class) ? (Class<R>) rTypeToken.getType() : (Class<R>) String.class;
        this.COLUMN_TYPE = cTypeToken.getType().getClass().isAssignableFrom(Class.class) ? (Class<C>) cTypeToken.getType() : (Class<C>) String.class;
        this.VALUE_TYPE = vTypeToken.getType().getClass().isAssignableFrom(Class.class) ? (Class<V>) vTypeToken.getType() : (Class<V>) String.class;
    }

    public CSVTableContainer(Class<R> ROW_TYPE, Class<C> COLUMN_TYPE, Class<V> VALUE_TYPE) {
        super("");
        this.ROW_TYPE = ROW_TYPE;
        this.COLUMN_TYPE = COLUMN_TYPE;
        this.VALUE_TYPE = VALUE_TYPE;
    }

    public CSVTableContainer(String headerName, Class<R> ROW_TYPE, Class<C> COLUMN_TYPE, Class<V> VALUE_TYPE) {
        super(headerName);
        this.ROW_TYPE = ROW_TYPE;
        this.COLUMN_TYPE = COLUMN_TYPE;
        this.VALUE_TYPE = VALUE_TYPE;
    }

    /**
     * Get the separator used in csv format file.
     *
     * <p>DEFAULT: <b>|</b>
     *
     * @return separator
     */
    @Contract(pure = true)
    public String SEPARATOR() {
        return SEPARATOR;
    }

    /**
     * Get the new line character used in csv format file.
     *
     * <p>DEFAULT: <b>\r\n</b>
     *
     * @return new line character
     */
    @Contract(pure = true)
    public String NEW_LINE() {
        return NEW_LINE;
    }

    /**
     * Get local directory to store container.
     *
     * @return local directory
     */
    @Contract(pure = true)
    public String LOCAL_STORAGE_DIR() {
        return LOCAL_STORAGE_DIR;
    }

    /**
     * Persist container data to external permanent storage.
     *
     * <p>For different separators, set it through SEPARATOR(String).
     *
     * <p>For different new line character under different systems, set it through NEW_LINE(String).
     *
     * <p>The pattern for remote path is <b>"user.password@host:port:path-to-file.csv"</b>.
     *
     * <p>e.g.
     * <p>1. hadoop.123@master:22:~/csv-data/result.csv
     * <p>2. hadoop@master:~/csv-data/result.csv (with no password and default port 22)
     *
     * @param path external storage path
     * @throws DataPersistenceException error appearance such as {@link java.io.IOException} and so on
     */
    @Override
    public void persist(@NotNull String path) throws DataPersistenceException {
        try {
            if (RemoteProfile.isLegal(path)) {
                persistToRemote(path);
                return;
            }
            File outputFile = new File(path);
            if (outputFile.getParentFile().exists() || outputFile.getParentFile().mkdirs()) {
                FileWriter outputWriter = new FileWriter(outputFile);
                final StringBuilder outputBuilder = new StringBuilder()
                        .append(getHeaderName())
                        .append(SEPARATOR())
                        .append(StringUtils.join(this.columnMap().keySet().toArray(), SEPARATOR()))
                        .append(NEW_LINE());
                this.rowKeys().forEach(rowKey -> outputBuilder.append(rowKey)
                        .append(SEPARATOR())
                        .append(StringUtils.join(this.columnKeys().stream().map(columnKey -> get(rowKey, columnKey)).toArray(), SEPARATOR()))
                        .append(NEW_LINE()));
                outputWriter.write(outputBuilder.toString());
                outputWriter.flush();
                outputWriter.close();
            }
        } catch (IOException e) {
            throw new DataPersistenceException(e.getMessage());
        }
    }

    /**
     * Load data from external storage into container.
     *
     * <p><b>Notice:</b> set row, column and value types first before calling this method.
     *
     * <p>The three types of row, column and value, abbreviated as R, C and V,
     * are requested for supporting by PARSERS or implementing the
     * {@link cn.sissors.hummingbird.collect.feature.Parsable} interface.
     *
     * <p>The pattern for remote path is <b>"user.password@host:port:path-to-file.csv"</b>.
     *
     * <p>e.g.
     * <p>1. hadoop.123@master:22:~/csv-data/result.csv
     * <p>2. hadoop@master:~/csv-data/result.csv (with no password and default port 22)
     *
     * @param path external storage path
     * @return the container that has been loaded
     * @throws DataLoadingException error appearance such as {@link java.io.IOException},
     *                              {@link cn.sissors.hummingbird.exceptions.IllegalValueTypeException} and so on
     */
    @SuppressWarnings("unchecked")
    @Override
    public CSVTableContainer<R, C, V> load(@NotNull String path) throws DataLoadingException {
        try {
            if (RemoteProfile.isLegal(path)) {
                return loadFromRemote(path);
            }
            String SEPARATOR_REGEX = SEPARATOR().replaceAll("\\|", "\\\\|");
            Scanner inputScanner = new Scanner(new File(path));
            String[] headerUnits = inputScanner.nextLine().split(SEPARATOR_REGEX);
            this.clean().setHeaderName(headerUnits[0]);
            C[] columnKeys = (C[]) Arrays
                    .stream(ArrayUtils.subarray(headerUnits, 1, headerUnits.length))
                    .map(column -> {
                        try {
                            return parse(column, getColumnType());
                        } catch (ClassNotFoundException | IllegalAccessException | IllegalValueTypeException
                                | InstantiationException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).toArray();
            String rowLine;
            while (inputScanner.hasNextLine() && (rowLine = inputScanner.nextLine()).length() > 0) {
                String[] rowUnits = rowLine.split(SEPARATOR_REGEX);
                R rowKey = parse(rowUnits[0], getRowType());
                for (int i = 1; i < rowUnits.length; i++) {
                    C columnKey = columnKeys[i - 1];
                    V value = parse(rowUnits[i], getValueType());
                    push(rowKey, columnKey, value);
                }
            }
            inputScanner.close();
            return this;
        } catch (FileNotFoundException | IllegalAccessException | InstantiationException | IllegalValueTypeException
                | ClassNotFoundException e) {
            throw new DataLoadingException("Data loading failed. Please check external file path and value type");
        }
    }

    @Contract(pure = true)
    private Class<R> getRowType() {
        return ROW_TYPE;
    }

    @Contract(pure = true)
    private Class<C> getColumnType() {
        return COLUMN_TYPE;
    }

    @Contract(pure = true)
    private Class<V> getValueType() {
        return VALUE_TYPE;
    }

    public CSVTableContainer<R, C, V> setRowType(Class<R> ROW_TYPE) {
        this.ROW_TYPE = ROW_TYPE;
        return this;
    }

    public CSVTableContainer<R, C, V> setColumnType(Class<C> COLUMN_TYPE) {
        this.COLUMN_TYPE = COLUMN_TYPE;
        return this;
    }

    public CSVTableContainer<R, C, V> setValueType(Class<V> VALUE_TYPE) {
        this.VALUE_TYPE = VALUE_TYPE;
        return this;
    }

    /**
     * Parse a text string into target class.
     *
     * <p>It's similar to Integer.parseInt(String) or Double.parseDouble(String).
     *
     * <p>But it's more flexible which can convert string into any types which are supported.
     * Support types of this method:
     * <p>1. types listed in PARSERS map;
     * <p>2. types that implement {@link cn.sissors.hummingbird.collect.feature.Parsable} interface.
     *
     * @param text  text string
     * @param clazz target object class
     * @param <T>   target object type
     * @return a target class object
     * @throws ClassNotFoundException    class name not illegal
     * @throws IllegalAccessException    method access failed in target class
     * @throws InstantiationException    no empty constructor found in target class
     * @throws IllegalValueTypeException not supported for target type
     */
    @SuppressWarnings("unchecked")
    private <T> T parse(String text, Class<T> clazz)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, IllegalValueTypeException {
        if (PARSERS.containsKey(clazz)) {
            return (T) PARSERS.get(clazz).apply(text);
        }
        if (Parsable.class.isAssignableFrom(clazz)) {
            Parsable<T> t = (Parsable<T>) Class.forName(clazz.getName()).newInstance();
            return t.parse(text);
        }
        throw new IllegalValueTypeException(String.format("%s is not supported for loading from local file", clazz.getCanonicalName()));
    }

    /**
     * Set separator character.
     *
     * @param SEPARATOR separator character
     * @return current container
     */
    @CanIgnoreReturnValue
    public CSVTableContainer<R, C, V> SEPARATOR(String SEPARATOR) {
        this.SEPARATOR = SEPARATOR;
        return this;
    }

    /**
     * Set new line character.
     *
     * @param NEW_LINE new line character
     * @return current container
     */
    @CanIgnoreReturnValue
    public CSVTableContainer<R, C, V> NEW_LINE(String NEW_LINE) {
        this.NEW_LINE = NEW_LINE;
        return this;
    }

    private void persistToRemote(String path) throws DataPersistenceException {
        File csvFile = null;
        try {
            RemoteProfile profile = RemoteProfile.resolve(path);
            csvFile = new File(LOCAL_STORAGE_DIR() + profile.fileName);
            this.persist(csvFile.getAbsolutePath());
            this.upload(profile);
        } catch (NetworkTransferException e) {
            throw new DataPersistenceException(e.getMessage());
        } finally {
            if (csvFile != null) {
                csvFile.deleteOnExit();
            }
        }
    }

    private CSVTableContainer<R, C, V> loadFromRemote(String path) throws DataLoadingException {
        try {
            RemoteProfile profile = RemoteProfile.resolve(path);
            File csvFile = download(profile);
            CSVTableContainer<R, C, V> container = this.load(csvFile.getAbsolutePath());
            csvFile.deleteOnExit();
            return container;
        } catch (NetworkTransferException e) {
            throw new DataLoadingException(e.getMessage());
        }
    }

    private void upload(RemoteProfile profile) throws NetworkTransferException {
        try {
            SCPClient scpClient = getSCPClient(profile);
            scpClient.put(LOCAL_STORAGE_DIR() + profile.fileName, profile.fileDir);
        } catch (IOException e) {
            throw new NetworkTransferException(e.getMessage());
        }
    }

    private File download(RemoteProfile profile) throws NetworkTransferException {
        try {
            SCPClient scpClient = getSCPClient(profile);
            scpClient.get(profile.path, LOCAL_STORAGE_DIR());
            return new File(LOCAL_STORAGE_DIR() + profile.fileName);
        } catch (IOException e) {
            throw new NetworkTransferException(e.getMessage());
        }
    }

    private SCPClient getSCPClient(RemoteProfile profile) throws NetworkTransferException {
        Exception _e = new Exception("server connection failed");
        for (int k = 0; k < 2; k++) {
            try {
                if (!remoteConnectionCache.containsKey(profile.remoteServer)) {
                    Connection connection = createConnection(profile);
                    remoteConnectionCache.put(profile.remoteServer, connection);
                }
                Connection connection = remoteConnectionCache.get(profile.remoteServer);
                return connection.createSCPClient();
            } catch (IOException | NetworkTransferException e) {
                _e = e;
            }
        }
        throw new NetworkTransferException(_e.getMessage());
    }

    private Connection createConnection(RemoteProfile profile) throws IOException, NetworkTransferException {
        Connection connection = new Connection(profile.host, profile.port);
        connection.connect();
        if (!connection.authenticateWithPassword(profile.user, profile.password)) {
            throw new NetworkTransferException("authentication failed");
        }
        return connection;
    }

    @SuppressWarnings("WeakerAccess")
    static class RemoteProfile {
        static class RemoteServer {
            protected String host;
            protected int port;
            protected String user;
            protected String password;

            private RemoteServer(String host, int port, String user, String password) {
                this.host = host;
                this.port = port;
                this.user = user;
                this.password = password;
            }

            protected static RemoteServer fromProfile(RemoteProfile profile) {
                return new RemoteServer(profile.host, profile.port, profile.user, profile.password);
            }
        }

        protected String user;
        protected String password;
        protected String host;
        protected int port;
        protected String path;
        protected String fileDir;
        protected String fileName;
        protected RemoteServer remoteServer;

        private RemoteProfile(
                String user, String password, String host, int port, String path, String fileDir, String fileName) {
            this.user = user;
            this.password = password;
            this.host = host;
            this.port = port;
            this.path = path;
            this.fileDir = fileDir;
            this.fileName = fileName;
            this.remoteServer = RemoteServer.fromProfile(this);
        }

        /**
         * Check whether the url is a legal profile referring remote host.
         *
         * <p>The legal pattern of remote path is <b>"user.password@host:port:path-to-file.csv"</b>,
         * which password and port can be omitted with default empty and 22, respectively.
         *
         * @param url url remote address
         * @return true or false
         */
        protected static boolean isLegal(String url) {
            String legalPattern = ".+(\\..+)?@.+(:(\\d)+)?:.+";
            Pattern pattern = Pattern.compile(legalPattern);
            return pattern.matcher(url).matches();
        }

        /**
         * Resolve url to a profile with necessary attributes.
         *
         * <p>The pattern of remote path is <b>"user.password@host:port:path-to-file.csv"</b>.
         *
         * <p>e.g.
         * <p>1. hadoop.123@master:22:~/csv-data/result.csv
         * <p>2. hadoop@master:~/csv-data/result.csv (with no password and default port 22)
         *
         * @param url remote address in the form of <b>"user.password@host:port:path-to-file.csv"</b>
         * @return a remote profile to describe file location
         */
        protected static RemoteProfile resolve(String url) throws NetworkTransferException {
            if (!RemoteProfile.isLegal(url)) {
                throw new NetworkTransferException("illegal remote address: " + url);
            }
            String userWithPassword = url.split("@")[0].trim();
            String user = userWithPassword.split("\\.")[0].trim();
            String password = userWithPassword.split("\\.").length > 1 ?
                    StringUtils.substringAfter(userWithPassword, user + ".").trim() : "";
            String hostWithPortAndPath = url.split("@")[1].trim();
            String host = hostWithPortAndPath.split(":")[0].trim();
            int port = hostWithPortAndPath.split(":").length > 2 ?
                    Integer.valueOf(hostWithPortAndPath.split(":")[1]) : 22;
            String path = hostWithPortAndPath.split(":").length > 2 ?
                    hostWithPortAndPath.split(":")[2] : hostWithPortAndPath.split(":")[1];
            String fileDir = StringUtils.substringBeforeLast(path, "/");
            String fileName = StringUtils.substringAfterLast(path, "/");
            return new RemoteProfile(user, password, host, port, path, fileDir, fileName);
        }
    }
}
