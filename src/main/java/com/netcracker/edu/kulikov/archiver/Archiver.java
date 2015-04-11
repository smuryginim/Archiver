package com.netcracker.edu.kulikov.archiver;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * упаковки файлов в архив и распаковки их из архива
 *
 * @author Kulikov Kirill
 */
public interface Archiver {

    /**
     * упаковывает указанные файлы в <code>listNameFiles</code> в архив <code>nameArchive</code>.
     * Если вместо входного файла указана директория,
     * ее содержимое упаковывается рекурсивно
     *
     * @param nameArchive
     * @param listNameFiles
     * @return
     */
//    int packInArchive(String nameArchive, List<String> listNameFiles) throws IOException;

    int packInArchive(String nameArchive, List<File> listNameFiles) throws IOException;

    /**
     * упаковывает указанные файлы в <code>listNameFiles</code> в архив <code>nameArchive</code> с клмментариями.
     *
     * @param nameArchive
     * @param listNameFiles
     * @param comment
     * @return
     */
//    int packInArchive(String nameArchive, List<String> listNameFiles, String comment) throws IOException;

    int packInArchive(String nameArchive, List<File> listNameFiles, String comment) throws IOException;

    /**
     * распаковывает архив в указанную дирректорию
     *
     * @return
     */
    int unpackArchive(String nameArchive, String saveDirectory) throws IOException;

    void addFilesToArchive(File source, List<File> files) throws IOException;

    public String getArchiveComment(File zipFile) throws IOException;

    public void setArchiveComment(File zipFile, String comment) throws IOException;

}
