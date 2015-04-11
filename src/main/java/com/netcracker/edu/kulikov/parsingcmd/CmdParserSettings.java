package com.netcracker.edu.kulikov.parsingcmd;

import com.netcracker.edu.kulikov.exceptions.SettingsArchiverException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.util.List;

public class CmdParserSettings {

//    private SettingsArchiver settings;

    public static SettingsArchiver getSettings(String[] args) throws CmdLineException {
        SettingsArchiver settings = new SettingsArchiver();
        CmdLineParser parser = new CmdLineParser(settings);

        parser.parseArgument(args);

        return settings;
    }

    public OperationType getType(SettingsArchiver settings) {
        if (settings == null) {
            throw new SettingsArchiverException("Incorrect settings command line. Settings should not be equal to null.");
        } else if (!settings.getZipForPack().equals("")) {
            checkArchiveName(settings.getZipForPack());
            checkFileExistence(settings.getListFiles());

            return OperationType.PACK;
        } else if (!settings.getZipForUnpack().equals("")) {
            checkArchiveName(settings.getZipForUnpack());
            checkArchiveExistence(settings.getZipForUnpack());

            return OperationType.UNPACK;
        } else if (!settings.getZipForAddFiles().equals("")) {
            checkArchiveName(settings.getZipForAddFiles());
            checkArchiveExistence(settings.getZipForAddFiles());
            checkFileExistence(settings.getListFiles());

            return  OperationType.ADD_FILES;
        } else if (!settings.getZipForReadComment().equals("")) {
            checkArchiveName(settings.getZipForReadComment());
            checkArchiveExistence(settings.getZipForReadComment());

            return  OperationType.READE_COMMENT;
        } else if (!settings.getZipForWriteComment().equals("")) {
            checkArchiveName(settings.getZipForWriteComment());
            checkArchiveExistence(settings.getZipForWriteComment());

            return  OperationType.WRITE_COMMENT;
        }
        return null;
    }

    private void checkArchiveName(String archive) {
        if (archive == null || archive.equals("")) {
            throw new SettingsArchiverException("Archive for this operation is not specified in the parameters");
        }
    }

    private void checkArchiveExistence(String archive) {
        if (!new File(archive).exists()) {
            throw new SettingsArchiverException("Archive " + archive + " is not exists!");
        }
    }

    private void checkFileExistence(List<File> files) {
        for (File file : files) {
            if (!file.exists()) {
                throw new SettingsArchiverException("File " + file.getName() + " is not exists!");
            }
        }
    }
}
