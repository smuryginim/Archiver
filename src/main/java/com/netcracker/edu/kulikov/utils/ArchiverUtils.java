package com.netcracker.edu.kulikov.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ArchiverUtils {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void writeFromIsToOs(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    }

    public static boolean checkFolder(String outputFolder) throws IOException {
        File folder = new File(new File(outputFolder).getAbsolutePath());
        if (!folder.exists()) {
            Files.createDirectories(folder.toPath());
            return true;
        }
        return false;
    }

    public static File createTempFile(File file) throws IOException {
        File tempFile = File.createTempFile(file.getName(), null);
        Files.delete(tempFile.toPath());
        if (!file.renameTo(tempFile)) {
            throw new IOException("Could not make temp file (" + file.getName() + ")");
        }
        return tempFile;
    }

    public static void copyZipToZip(ZipInputStream zin, ZipOutputStream zos) throws IOException {
        for (ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()) {
            zos.putNextEntry(ze);
            writeFromIsToOs(zin, zos);
            zos.closeEntry();
        }
    }
}
