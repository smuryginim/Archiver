package com.netcracker.edu.kulikov.archiver;

import com.netcracker.edu.kulikov.parsingcmd.CmdParserSettings;
import com.netcracker.edu.kulikov.parsingcmd.SettingsArchiver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class ZipArchiverPackTest {

    private Archiver archiver;
    private CmdParserSettings parser;
    private SettingsArchiver settings = new SettingsArchiver();


    @Before
    public void setUp() throws Exception {
        archiver = new ZipArchiver();
    }

    @After
    public void tearDown() throws Exception {
        File zip = new File("src\\test\\resources\\archive.zip");
        if (zip.exists()) {
            Files.delete(zip.toPath());
        }
    }

    @Test
    public void testPackInArchiveThroughSettings() throws Exception {
        String[] parameters = new String[]{"-p", "src\\test\\resources\\archive.zip", "-f", "src\\test\\resources\\dir1", "src\\test\\resources\\dir2", "-c", "archive's comment"};
        CmdLineParser parser = new CmdLineParser(settings);
        parser.parseArgument(parameters);

        int expected = 2;
        int actual = archiver.packInArchive(settings);

        assertEquals(expected, actual);
    }

    @Test
    public void testPackInArchiveWithoutComment() throws Exception {
        parser = new CmdParserSettings(settings);

        String[] parameters = new String[]{"-p", "src\\test\\resources\\archive.zip", "-f", "src\\test\\resources\\dir1", "src\\test\\resources\\dir2"};
        settings = parser.getParsedSettings(parameters);

        int expected = 2;
        int actual = archiver.packInArchive(settings.getZipForPack(), settings.getListFiles());

        assertEquals(expected, actual);
    }

    @Test
    public void testPackInArchiveWithComment() throws Exception {
        parser = new CmdParserSettings(settings);

        String[] parameters = new String[]{"-p", "src\\test\\resources\\archive.zip", "-f", "src\\test\\resources\\dir1", "src\\test\\resources\\dir2"};
        settings = parser.getParsedSettings(parameters);

        int expected = 2;
        int actual = archiver.packInArchive(settings.getZipForPack(), settings.getListFiles(), settings.getComment());

        assertEquals(expected, actual);
    }
}
