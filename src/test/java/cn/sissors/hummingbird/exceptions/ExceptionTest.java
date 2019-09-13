package cn.sissors.hummingbird.exceptions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author yazhong.zyz
 * @version 2019-08-21
 */
public class ExceptionTest {
    @Test
    public void testContainerRuntimeException() {
        ContainerRuntimeException e = new ContainerRuntimeException("test");
        System.out.println("ContainerRuntimeException: " + e.getMessage());
        assertEquals("test", e.getMessage());
    }

    @Test
    public void testDataLoadingException() {
        Exception e = new DataLoadingException("test");
        System.out.println("DataLoadingException: " + e.getMessage());
        assertEquals("test", e.getMessage());
    }

    @Test
    public void testDataPersistenceException() {
        Exception e = new DataPersistenceException("test");
        System.out.println("DataPersistenceException: " + e.getMessage());
        assertEquals("test", e.getMessage());
    }

    @Test
    public void testIllegalValueTypeException() {
        Exception e = new IllegalValueTypeException("test");
        System.out.println("IllegalValueTypeException: " + e.getMessage());
        assertEquals("test", e.getMessage());
    }

    @Test
    public void testNetworkTransferException() {
        Exception e = new NetworkTransferException("test");
        System.out.println("NetworkTransferException: " + e.getMessage());
        assertEquals("test", e.getMessage());
    }
}