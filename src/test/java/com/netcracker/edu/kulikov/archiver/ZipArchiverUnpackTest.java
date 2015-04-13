package com.netcracker.edu.kulikov.archiver;

import com.netcracker.edu.kulikov.parsingcmd.CmdParserSettings;
import com.netcracker.edu.kulikov.parsingcmd.SettingsArchiver;
import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineParser;

import static org.junit.Assert.assertEquals;

public class ZipArchiverUnpackTest {

    private Archiver archiver;

    private SettingsArchiver settings = new SettingsArchiver();


    @Before
    public void setUp() throws Exception {
        archiver = new ZipArchiver();
    }

    @Test
    public void testUnpackArchiveThroughSettings() throws Exception {
        String[] parameters = new String[]{"-u", "src\\test\\resources\\unpackArchive.zip", "-d", "src\\test\\resources\\unpack_dir" };
        CmdLineParser parser = new CmdLineParser(settings);

        parser.parseArgument(parameters);

        int expected = 45;
        int actual = archiver.unpackArchive(settings);

        assertEquals(expected, actual);
    }

    @Test
    public void testUnpackArchive1() throws Exception {
        CmdParserSettings parser = new CmdParserSettings(settings);

        String[] parameters = new String[]{"-u", "src\\test\\resources\\unpackArchive.zip", "-d", "src\\test\\resources\\unpack_dir" };
        settings = parser.getParsedSettings(parameters);

        int expected = 45;
        int actual = archiver.unpackArchive(settings.getZipForUnpack(), settings.getDirForUnpack());

        assertEquals(expected, actual);
    }
}
