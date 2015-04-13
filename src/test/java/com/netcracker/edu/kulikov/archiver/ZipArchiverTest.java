package com.netcracker.edu.kulikov.archiver;

import com.netcracker.edu.kulikov.parsingcmd.CmdParserSettings;
import com.netcracker.edu.kulikov.parsingcmd.SettingsArchiver;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ZipArchiverTest {

    private Archiver archiver;

    private SettingsArchiver settings = new SettingsArchiver();


    @Before
    public void setUp() throws Exception {
        archiver = new ZipArchiver();
    }

    @Ignore
    public void testAddFilesToArchive() throws Exception {
        CmdParserSettings parser = new CmdParserSettings(settings);

        String[] parameters = new String[]{"-a", "src\\test\\resources\\addTestArchive.zip", "-f", "src\\test\\resources\\dir1_add", "src\\test\\resources\\dir2_add"};
        settings = parser.getParsedSettings(parameters);

        int expected = 2;
        int actual = archiver.addFilesToArchive(new File(settings.getZipForAddFiles()), settings.getListFiles());

        assertEquals(expected, actual);
    }

    @Test
    public void testGetArchiveComment() throws Exception {
        CmdParserSettings parser = new CmdParserSettings(settings);

        String[] parameters = new String[]{"-r", "src\\test\\resources\\commentArchive.zip"};
        settings = parser.getParsedSettings(parameters);

        String expected = "archive's comment";
        String actual = archiver.getArchiveComment(new File(settings.getZipForReadComment()));

        assertEquals(expected, actual);
    }

    @Test
    public void testSetArchiveComment() throws Exception {
        CmdParserSettings parser = new CmdParserSettings(settings);

        String[] parameters = new String[]{"-w", "src\\test\\resources\\getCommentArchive.zip", "-c", "new archive's comment"};
        settings = parser.getParsedSettings(parameters);

        String expected = "new archive's comment";
        String actual = archiver.setArchiveComment(new File(settings.getZipForWriteComment()), settings.getComment());

        assertEquals(expected, actual);
    }

}
