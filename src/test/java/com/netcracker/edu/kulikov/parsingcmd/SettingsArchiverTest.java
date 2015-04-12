package com.netcracker.edu.kulikov.parsingcmd;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.*;

public class SettingsArchiverTest {

    private static SettingsArchiver settings;
    private CmdLineParser parser;

    @BeforeClass
    public static void setUpClass() throws Exception {
        settings = SettingsArchiver.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        parser = new CmdLineParser(settings);
    }

    @Test
    public void testGetInstance() throws Exception {
        assertThat(settings, is(allOf(sameInstance(SettingsArchiver.getInstance()), any(SettingsArchiver.class))));
    }

    @Test
    public void testGetZipForPack() throws Exception {
        String[] parameters = new String[]{"-p", "src\\test\\resources\\dir.zip", "-f", "src\\test\\resources\\dir1"};
        parser.parseArgument(parameters);

        String expected = "src\\test\\resources\\dir.zip";
        String actual = settings.getZipForPack();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetZipForUnpack() throws Exception {
        String[] parameters = new String[]{"-u", "src\\test\\resources\\dir.zip", "-d", "src\\test\\resources\\unpack_dir"};
        parser.parseArgument(parameters);

        String expected = "src\\test\\resources\\dir.zip";
        String actual = settings.getZipForUnpack();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetZipForAddFiles() throws Exception {
        String[] parameters = new String[]{"-a", "src\\test\\resources\\dir.zip", "-f", "src\\test\\resources\\dir1_add"};
        parser.parseArgument(parameters);

        String expected = "src\\test\\resources\\dir.zip";
        String actual = settings.getZipForAddFiles();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetZipForReadComment() throws Exception {
        String[] parameters = new String[]{"-r", "src\\test\\resources\\dir.zip"};
        parser.parseArgument(parameters);

        String expected = "src\\test\\resources\\dir.zip";
        String actual = settings.getZipForAddFiles();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetZipForWriteComment() throws Exception {
        String[] parameters = new String[]{"-w", "src\\test\\resources\\dir.zip", "-c", "new archive's comment"};
        parser.parseArgument(parameters);

        String expected = "src\\test\\resources\\dir.zip";
        String actual = settings.getZipForAddFiles();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetDirForUnpack() throws Exception {
        String[] parameters = new String[]{"-u", "src\\test\\resources\\dir.zip", "-d", "src\\test\\resources\\unpack_dir"};
        parser.parseArgument(parameters);

        String expected = "src\\test\\resources\\unpack_dir";
        String actual = settings.getDirForUnpack();

        assertThat(expected, is(actual));
    }

    @Test
    public void testGetComment() throws Exception {
        String[] parameters = new String[]{"-p", "src\\test\\resources\\dir.zip", "-f", "src\\test\\resources\\dir1", "src\\test\\resources\\dir2", "-c", "archive's comment"};
        parser.parseArgument(parameters);

        String expected = "archive's comment";
        String actual = settings.getComment();

        assertThat(expected, is(allOf(equalTo(actual), notNullValue())));
    }

    @Test(expected = CmdLineException.class)
    public void testDiscrepancyParametersForMainOperation() throws Exception {
        String[] parameters = new String[]{"-p", "src\\main\\resources\\dir.zip", "-u", "src\\main\\resources\\dir.zip", "-f", "src\\main\\resources\\dir1"};
        parser.parseArgument(parameters);
    }

    @Test(expected = CmdLineException.class)
    public void testDiscrepancyParameters() throws Exception {
        String[] parameters = new String[]{"-u", "src\\main\\resources\\dir.zip", "-c", "src\\main\\resources\\dir.zip", "-f", "src\\main\\resources\\dir1"};
        parser.parseArgument(parameters);
    }
}