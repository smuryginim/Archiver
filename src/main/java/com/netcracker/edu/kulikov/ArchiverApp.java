package com.netcracker.edu.kulikov;

import com.netcracker.edu.kulikov.archiver.Archiver;
import com.netcracker.edu.kulikov.archiver.ZipArchiver;
import com.netcracker.edu.kulikov.exceptions.ArchiverException;
import com.netcracker.edu.kulikov.parsingcmd.CmdParserSettings;
import com.netcracker.edu.kulikov.parsingcmd.SettingsArchiver;
import org.kohsuke.args4j.CmdLineException;

import java.io.File;
import java.io.IOException;

public class ArchiverApp {

    public static void main(String[] args) {

//        example:
        String[] strings1 = new String[]{"-p", "src\\main\\resources\\dirir.zip", "-f", "src\\main\\resources\\dir1", "src\\main\\resources\\dir2", "-c", "archive's comment"};
        String[] strings2 = new String[]{"-u", "src\\main\\resources\\dirir.zip", "-d", "src\\main\\resources\\unpack_dir"};
        String[] strings3 = new String[]{"-a", "src\\main\\resources\\dirir.zip", "-f", "src\\main\\resources\\dir1_add", "src\\main\\resources\\dir2_add"};
        String[] strings4 = new String[]{"-r", "src\\main\\resources\\dirir.zip"};
        String[] strings5 = new String[]{"-w", "src\\main\\resources\\dirir.zip", "-c", "new archive's comment"};
        String[] parameters = new String[]{"-a", "src\\test\\resources\\cmdParserTest.zip", "-f", "src\\test\\resources\\dir1_add"};

        Archiver archiver = new ZipArchiver();

        SettingsArchiver settings = SettingsArchiver.getInstance();
        CmdParserSettings parser = new CmdParserSettings(settings);
        try {
            settings = parser.getParsedSettings(strings4);
            switch (parser.getType()) {
                case PACK:
                    archiver.packInArchive(settings.getZipForPack(), settings.getListFiles(), settings.getComment());
                    break;
                case UNPACK:
                    archiver.unpackArchive(settings.getZipForUnpack(), settings.getDirForUnpack());
                    break;
                case ADD_FILES:
                    archiver.addFilesToArchive(new File(settings.getZipForAddFiles()), settings.getListFiles());
                    break;
                case READE_COMMENT:
                    System.out.println("Comment: " + archiver.getArchiveComment(new File(settings.getZipForReadComment())));
                    break;
                case WRITE_COMMENT:
                    archiver.setArchiveComment(new File(settings.getZipForWriteComment()), settings.getComment());
            }
        } catch (CmdLineException e) {
            e.printStackTrace();
        } catch (ArchiverException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
