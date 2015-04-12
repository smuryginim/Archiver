package com.netcracker.edu.kulikov.parsingcmd;

import com.netcracker.edu.kulikov.exceptions.SettingsArchiverException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.util.List;

public class CmdParserSettings {

    private SettingsArchiver settings;

    public CmdParserSettings() {

    }

    public CmdParserSettings(SettingsArchiver settings) {
        this.settings = settings;
    }

    public void setSettings(SettingsArchiver settings) {
        this.settings = settings;
    }

    public SettingsArchiver getSettings() {
        return settings;
    }

    public SettingsArchiver getParsedSettings(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(settings);
        parser.parseArgument(args);

        return settings;
    }

    public OperationType getType() throws SettingsArchiverException {
        if (settings == null) {
            throw new SettingsArchiverException("Incorrect settings command line. Settings should not be equal to null.");
        } else if (!settings.getZipForPack().equals("")) {
            checkFileExistence(settings.getListFiles());
            return OperationType.PACK;
        } else if (!settings.getZipForUnpack().equals("")) {
            checkArchiveExistence(settings.getZipForUnpack());
            return OperationType.UNPACK;
        } else if (!settings.getZipForAddFiles().equals("")) {
            checkArchiveExistence(settings.getZipForAddFiles());
            checkFileExistence(settings.getListFiles());
            return  OperationType.ADD_FILES;
        } else if (!settings.getZipForReadComment().equals("")) {
            checkArchiveExistence(settings.getZipForReadComment());
            return  OperationType.READE_COMMENT;
        } else if (!settings.getZipForWriteComment().equals("")) {
            checkArchiveExistence(settings.getZipForWriteComment());
            return  OperationType.WRITE_COMMENT;
        } else {
            throw new SettingsArchiverException("Incorrect settings command line");
        }
    }

    private void checkArchiveExistence(String archive) throws SettingsArchiverException {
        if (archive == null || archive.equals("")) {
            throw new SettingsArchiverException("Archive for this operation is not specified in the parameters");
        } else if (!new File(archive).exists()) {
            throw new SettingsArchiverException("Archive " + archive + " is not exists!");
        }
    }

    private void checkFileExistence(List<File> files) throws SettingsArchiverException {
        for (File file : files) {
            if (!file.exists()) {
                throw new SettingsArchiverException("File " + file.getName() + " is not exists!");
            }
        }
    }
}
