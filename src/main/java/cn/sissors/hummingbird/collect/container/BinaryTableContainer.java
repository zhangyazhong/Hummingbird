package cn.sissors.hummingbird.collect.container;

import cn.sissors.hummingbird.collect.TableContainer;
import cn.sissors.hummingbird.exceptions.DataLoadingException;
import cn.sissors.hummingbird.exceptions.DataPersistenceException;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * This class is an implementation for {@link cn.sissors.hummingbird.collect.TableContainer},
 * using {@link java.io.Serializable} to serialize and deserialize table container object.
 *
 * @author zyz
 * @version 2018-10-25
 */
public class BinaryTableContainer<R, C, V> extends TableContainer<R, C, V> {
    public BinaryTableContainer(String headerName) {
        super(headerName);
    }

    /**
     * Persist container data to external permanent storage.
     *
     * @param path external storage path
     * @throws DataPersistenceException error appearance such as {@link java.io.IOException} and so on
     */
    @Override
    public void persist(@NotNull String path) throws DataPersistenceException {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new DataPersistenceException(e.getMessage());
        }
    }

    /**
     * Load data from external storage into container.
     *
     * @param path external storage path
     * @return the container that has been loaded
     * @throws DataLoadingException error appearance such as {@link java.lang.ClassNotFoundException}, {@link java.lang.ClassCastException} and so on
     */
    @SuppressWarnings("unchecked")
    @Override
    public BinaryTableContainer<R, C, V> load(@NotNull String path) throws DataLoadingException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
            BinaryTableContainer<R, C, V> binaryTableContainer = this.getClass().cast(objectInputStream.readObject());
            objectInputStream.close();
            return binaryTableContainer;
        } catch (IOException | ClassNotFoundException e) {
            throw new DataLoadingException(e.getMessage());
        }
    }
}
