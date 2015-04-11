package com.netcracker.edu.kulikov.archiver;

import com.netcracker.edu.kulikov.parsingcmd.SettingsArchiver;

import java.io.File;
import java.util.List;

/**
 * упаковки файлов в архив и распаковки их из архива
 *
 * @author Kulikov Kirill
 */
public interface Archiver {

    public int packInArchive(SettingsArchiver settings);

    public int packInArchive(String nameArchive, List<File> listNameFiles);

    public int packInArchive(String nameArchive, List<File> listNameFiles, String comment);

    public int unpackArchive(SettingsArchiver settings);

    public int unpackArchive(String nameArchive, String saveDirectory);

    public int addFilesToArchive(File source, List<File> files);

    public String getArchiveComment(File zipFile);

    public void setArchiveComment(File zipFile, String comment);
}
