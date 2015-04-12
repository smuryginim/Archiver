package com.netcracker.edu.kulikov.archiver;

import com.netcracker.edu.kulikov.exceptions.ArchiverException;
import com.netcracker.edu.kulikov.parsingcmd.SettingsArchiver;

import java.io.File;
import java.util.List;

/**
 * упаковки файлов в архив и распаковки их из архива
 *
 * @author Kulikov Kirill
 */
public interface Archiver {

    public int packInArchive(SettingsArchiver settings) throws ArchiverException;

    public int packInArchive(String nameArchive, List<File> listNameFiles) throws ArchiverException;

    public int packInArchive(String nameArchive, List<File> listNameFiles, String comment) throws ArchiverException;

    public int unpackArchive(SettingsArchiver settings) throws ArchiverException;

    public int unpackArchive(String nameArchive, String saveDirectory) throws ArchiverException;

    public int addFilesToArchive(File source, List<File> files) throws ArchiverException;

    public String getArchiveComment(File zipFile) throws ArchiverException;

    public void setArchiveComment(File zipFile, String comment) throws ArchiverException;
}
