package com.netcracker.edu.kulikov.archiver;

import com.netcracker.edu.kulikov.parsingcmd.SettingsArchiver;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * упаковки файлов в zip архив и распаковки их из архива
 *
 * @author Kulikov Kirill
 */
public class ZipArchiver implements Archiver {

    private static final Logger log = Logger.getLogger(ZipArchiver.class);
    private static final int DEFAULT_BUFFER_SIZE = 8192;
//    private String parentCurrentFile;

    public ZipArchiver() {
    }

    public int packInArchive(SettingsArchiver settings) throws IOException {
//        if (settings.getComments() != null) {
//            return packInArchive(settings.getNameArchive(), settings.getNameFiles(), settings.getComments());
//        }
//        return packInArchive(settings.getNameArchive(), settings.getNameFiles());
        return 0;
    }

    @Override
    public int packInArchive(String nameArchive, List<File> listNameFiles) throws IOException {
        return packInArchive(nameArchive, listNameFiles, null);
    }

    @Override
    public int packInArchive(String nameArchive, List<File> listNameFiles, String comment) throws IOException {
        int numberFiles = 0;
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(nameArchive))) {

            log.info("Start packing to archive. Output to zip archive: " + nameArchive);
            for (File file : listNameFiles) {
                File fileAbsolutePath = file.getAbsoluteFile();
//                parentCurrentFile = fileAbsolutePath.getParent();

                writeFileToZos(zos, fileAbsolutePath);
                zos.setComment(comment);
                numberFiles++;
            }
            log.info("Done pack files in archive: " + nameArchive);
        }

        return numberFiles;
    }

    private void writeFileToZos(ZipOutputStream zos, File file) throws IOException {
        List<String> listForZip = generateFileList(file, file.getParent());
        for (String nameFile : listForZip) {
            zos.putNextEntry(new ZipEntry(nameFile));

            try (InputStream in = new BufferedInputStream(new FileInputStream(
                    new File(file.getParent().replace(File.separatorChar, '/'), nameFile)))) {
                writeFromFisToZos(in, zos);
            }
        }
        zos.closeEntry();
    }

    private void writeFromFisToZos(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    }

    public List<String> generateFileList(File node, String parent) throws IOException {
        List<String> list = new ArrayList<>();
        if (node.isFile()) {
//            System.out.println(node.toString());
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

    public int unpackArchive(SettingsArchiver settings) throws IOException {
//        return unpackArchive(settings.getNameArchive(), settings.getNameDirectory());
        return 0;
    }

    @Override
    public int unpackArchive(String nameArchive, String outputFolder) throws IOException {
        int numberFiles = 0;
        checkFolder(outputFolder);

        log.info("Start unpacking archive in directory: " + outputFolder);
        try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(nameArchive)))) {
            for (ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()) {
                File newFile = new File(outputFolder + File.separator + ze.getName());

                //create all non exists folders
                Files.createDirectories(new File(newFile.getParent()).toPath());
                try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(newFile))) {
                    writeFromFisToZos(zin, fos);
                }
                numberFiles++;
            }
            zin.closeEntry();
            log.info("Done unpack to archive: " + nameArchive);
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
    public void addFilesToArchive(File source, List<File> files) throws IOException {
        String comment = getArchiveComment(source);
        File tmpZip = createTempZip(source);

        log.info("Add files "+ Arrays.toString(files.toArray()) + " to zip: " + source.getName());
        try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(tmpZip)));
             ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(source)))) {

            for (File file : files) {
                File fileAbsolutePath = new File(file.getAbsolutePath());
//                parentCurrentFile = fileAbsolutePath.getParent();

                writeFileToZos(zos, fileAbsolutePath);
            }

            copyZipToZip(zin, zos);
            zos.setComment(comment);
            log.info("Done adds files to zip");
        }
        Files.delete(tmpZip.toPath());
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
            writeFromFisToZos(zin, zos);
            zos.closeEntry();
        }
    }

    @Override
    public String getArchiveComment(File zipFile) throws IOException {
        String comment;
        try (ZipFile zf = new ZipFile(zipFile)) {
            comment = zf.getComment();
        }
        log.info("In the archive " + zipFile.getName() + " read the comments: " + comment);
        return comment;
    }

    @Override
    public void setArchiveComment(File zipFile, String comment) throws IOException {

        File tmpZip = createTempZip(zipFile);
        try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(tmpZip)));
             ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {

            copyZipToZip(zin, zos);
            zos.setComment(comment);
        }
        Files.delete(tmpZip.toPath());
        log.info("In the archive " + zipFile.getName() + " set archive's comment: " + comment);
    }
}
