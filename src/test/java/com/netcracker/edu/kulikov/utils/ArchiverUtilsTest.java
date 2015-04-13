package com.netcracker.edu.kulikov.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class ArchiverUtilsTest {

    @Test
    public void testWriteFromIsToOs() throws Exception {
        String expected = "123,456,789,123,456,789";
        byte[] data = expected.getBytes();

        InputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ArchiverUtils.writeFromIsToOs(input, output);
        String actual = new String(output.toByteArray());

        assertEquals(expected, actual);
    }

    @Test
    public void testCheckFolderTrue() throws Exception {
        boolean actual = ArchiverUtils.checkFolder("src\\test\\resources\\checkFolderTest\\checkFolderTest_false\\checkFolderTest");

        assertTrue(actual);

        Files.delete(new File("src\\test\\resources\\checkFolderTest\\checkFolderTest_false\\checkFolderTest").toPath());
}

    @Test
    public void testCheckFolderFalse() throws Exception {
        boolean actual = ArchiverUtils.checkFolder("src\\test\\resources\\checkFolderTest\\checkFolderTest2\\checkFolderTest3");

        assertFalse(actual);
    }

    @Test
    public void testCreateTempFile() throws Exception {
        File file = new File("src\\test\\resources\\check.txt");
        File tempFile = ArchiverUtils.createTempFile(file);

        assertFalse(file.renameTo(tempFile));

        Files.createFile(file.toPath());
    }
}