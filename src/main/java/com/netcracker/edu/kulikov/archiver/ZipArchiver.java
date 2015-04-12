package com.netcracker.edu.kulikov.archiver;

import com.netcracker.edu.kulikov.exceptions.ArchiverException;
import com.netcracker.edu.kulikov.exceptions.SettingsArchiverException;
import com.netcracker.edu.kulikov.parsingcmd.CmdParserSettings;
import com.netcracker.edu.kulikov.parsingcmd.OperationType;
import com.netcracker.edu.kulikov.parsingcmd.SettingsArchiver;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.*;

/**
 * упаковки файлов в zip архив и распаковки их из архива
 *
 * @author Kulikov Kirill
 */
public class ZipArchiver implements Archiver {

    private static final Logger log = Logger.getLogger(ZipArchiver.class);

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public ZipArchiver() {
    }

    @Override
    public int packInArchive(SettingsArchiver settings) throws ArchiverException {
        CmdParserSettings parser = new CmdParserSettings(settings);
        if (parser.getType() != OperationType.PACK) {
            throw new SettingsArchiverException("Incorrect parameters for packing to the archive");
        }

        if (settings.getComment() != null) {
            return packInArchive(settings.getZipForPack(), settings.getListFiles(), settings.getComment());
        }
        return packInArchive(settings.getZipForPack(), settings.getListFiles());
    }

    @Override
    public int packInArchive(String nameArchive, List<File> listNameFiles) throws ArchiverException {
        return packInArchive(nameArchive, listNameFiles, null);
    }

    @Override
    public int packInArchive(String nameArchive, List<File> listNameFiles, String comment) throws ArchiverException {
        int numberFiles = 0;

        log.info("Start packing to archive. Output to zip archive: " + nameArchive);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(nameArchive))) {
            for (File file : listNameFiles) {
                File fileAbsolutePath = file.getAbsoluteFile();

                writeFileToZos(zos, fileAbsolutePath);
                zos.setComment(comment);
                numberFiles++;
            }
            log.info("Done pack files in archive: " + nameArchive);
        } catch (IOException e) {
            log.error("Failed to pack the files in the archive: " + nameArchive, e);
            throw new ArchiverException(this, "Failed to pack the files in the archive", e);
        }
        return numberFiles;
    }

    private void writeFileToZos(ZipOutputStream zos, File file) throws IOException {
        List<String> listForZip = generateFileList(file, file.getParent());
        for (String nameFile : listForZip) {
            zos.putNextEntry(new ZipEntry(nameFile));

            try (InputStream in = new BufferedInputStream(new FileInputStream(
                    new File(file.getParent().replace(File.separatorChar, '/'), nameFile)))) {
                writeFromIsToOs(in, zos);
            }
        }
        zos.closeEntry();
    }

    private void writeFromIsToOs(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    }

    private List<String> generateFileList(File node, String parent) throws IOException {
        List<String> list = new ArrayList<>();
        if (node.isFile()) {
            list.add(generateZipEntry(node.toString(), parent));
        }

        if (node.isDirectory()) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(node.toPath())) {
                for (Path child : directoryStream) {
                    list.addAll(generateFileList(child.toFile(), parent));
                }
            }
        }
        return list;
    }

    private String generateZipEntry(String file, String parent) {
        if (parent.endsWith(File.separator)) {
            return file.substring(parent.length(), file.length());
        }
        return file.substring(parent.length() + 1, file.length());
    }

    @Override
    public int unpackArchive(SettingsArchiver settings) throws ArchiverException {
        CmdParserSettings parser = new CmdParserSettings(settings);
        if (parser.getType() != OperationType.UNPACK) {
            throw new SettingsArchiverException("Incorrect parameters for unpacking to the archive");
        }
        return unpackArchive(settings.getZipForUnpack(), settings.getDirForUnpack());
    }

    @Override
    public int unpackArchive(String nameArchive, String outputFolder) throws ArchiverException {
        int numberFiles = 0;

        log.info("Start unpacking archive in directory: " + outputFolder);
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(nameArchive))) {
            checkFolder(outputFolder);
            for (ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()) {
                File newFile = new File(outputFolder + File.separator + ze.getName());

                //create all non exists folders
                Files.createDirectories(new File(newFile.getParent()).toPath());
                try (OutputStream fos = new FileOutputStream(newFile)) {
                    writeFromIsToOs(zin, fos);
                }
                numberFiles++;
            }
            zin.closeEntry();
            log.info("Done unpack to archive: " + nameArchive);
        } catch (IOException e) {
            log.error("Failed to unpack the files from the archive: " + nameArchive, e);
            throw new ArchiverException(this, "Failed to unpack the files from the archive: " + nameArchive, e);
        }
        return numberFiles;
    }

    private boolean checkFolder(String outputFolder) throws IOException {
        File folder = new File(new File(outputFolder).getAbsolutePath());
        if (!folder.exists()) {
            Files.createDirectories(folder.toPath());
            return true;
        }
        return false;
    }

    @Override
    public int addFilesToArchive(File source, List<File> files) throws ArchiverException {
        log.info("Add files " + Arrays.toString(files.toArray()) + " to zip: " + source.getName());
        int numberFiles = 0;
        try {
            String comment = getArchiveComment(source);
            File tmpZip = createTempZip(source);
            try (ZipInputStream zin = new ZipInputStream(new FileInputStream(tmpZip));
                 ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(source))) {

                for (File file : files) {
                    File fileAbsolutePath = new File(file.getAbsolutePath());
                    writeFileToZos(zos, fileAbsolutePath);
                    numberFiles++;
                }
                copyZipToZip(zin, zos);

                zos.setComment(comment);
                log.info("Done adds files to zip");
            }
            Files.delete(tmpZip.toPath());
        } catch (IOException e) {
            log.error("Failed to add the files in archive: " + source.getName(), e);
            throw new ArchiverException(this, "Failed to add the files in archive: " + source.getName(), e);
        }
        return numberFiles;
    }

    private File createTempZip(File zipFile) throws IOException {
        File tmpZip = File.createTempFile(zipFile.getName(), null);
        Files.delete(tmpZip.toPath());
        if (!zipFile.renameTo(tmpZip)) {
            throw new IOException("Could not make temp file (" + zipFile.getName() + ")");
        }
        return tmpZip;
    }

    private void copyZipToZip(ZipInputStream zin, ZipOutputStream zos) throws IOException {
        for (ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()) {
            zos.putNextEntry(ze);
            writeFromIsToOs(zin, zos);
            zos.closeEntry();
        }
    }

    @Override
    public String getArchiveComment(File zipFile) throws ArchiverException {
        String comment;
        try (ZipFile zf = new ZipFile(zipFile)) {
            comment = zf.getComment();
            log.info("In the archive " + zipFile.getName() + " read the comments: " + comment);
        } catch (IOException e) {
            log.error("Failed to get the comment from archive: " + zipFile.getName(), e);
            throw new ArchiverException(this, "Failed to get the comment from archive: " + zipFile.getName(), e);
        }
        return comment;
    }

    @Override
    public void setArchiveComment(File zipFile, String comment) throws ArchiverException {

        try {
            File tmpZip = createTempZip(zipFile);
            try (ZipInputStream zin = new ZipInputStream(new FileInputStream(tmpZip));
                 ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {

                copyZipToZip(zin, zos);
                zos.setComment(comment);
            }
            Files.delete(tmpZip.toPath());
            log.info("In the archive " + zipFile.getName() + " set archive's comment: " + comment);
        } catch (IOException e) {
            log.error("Failed to set the comment in archive: " + zipFile.getName(), e);
            throw new ArchiverException(this, "Failed to set the comment in archive: " + zipFile.getName(), e);
        }
    }
}
