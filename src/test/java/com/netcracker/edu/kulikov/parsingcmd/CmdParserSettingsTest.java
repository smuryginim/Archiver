package com.netcracker.edu.kulikov.parsingcmd;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CmdParserSettingsTest {

    private CmdParserSettings parser;

    @Before
    public void setUp() throws Exception {
        parser = new CmdParserSettings();
    }

    @After
    public void tearDown() throws Exception {
        parser = null;

    }

    @Test
    public void testSetGetSettings() throws Exception {
        SettingsArchiver settings = SettingsArchiver.getInstance();
        parser.setSettings(settings);
        CmdParserSettings parserSettings = new CmdParserSettings(settings);

        SettingsArchiver expected = parserSettings.getSettings();
        SettingsArchiver actual = parser.getSettings();

        assertThat(expected, is(allOf(sameInstance(actual), any(SettingsArchiver.class))));
    }

    @Test
    public void testGetTypePack() throws Exception {
        String[] parameters = new String[]{"-p", "src\\test\\resources\\cmdParserTest.zip", "-f", "src\\test\\resources\\dir1"};

        SettingsArchiver settings = SettingsArchiver.getInstance();
        parser.setSettings(settings);
        parser.getParsedSettings(parameters);

        OperationType expected = OperationType.PACK;
        OperationType actual = parser.getType();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeUnpack() throws Exception {
        String[] parameters = new String[]{"-u", "src\\test\\resources\\cmdParserTest.zip", "-d", "src\\test\\resources\\unpack_dir"};

        SettingsArchiver settings = SettingsArchiver.getInstance();
        parser.setSettings(settings);
        parser.getParsedSettings(parameters);

        OperationType expected = OperationType.UNPACK;
        OperationType actual = parser.getType();

        assertEquals(expected, actual);
    }

    @Ignore
    public void testGetTypeAddFiles() throws Exception {
        String[] parameters = new String[]{"-a", "src\\test\\resources\\cmdParserTest.zip", "-f", "src\\test\\resources\\dir1_add"};

        SettingsArchiver settings = SettingsArchiver.getInstance();
        parser.setSettings(settings);
        parser.getParsedSettings(parameters);

        OperationType expected = OperationType.ADD_FILES;
        OperationType actual = parser.getType();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeReadComment() throws Exception {
        String[] parameters = new String[]{"-r", "src\\test\\resources\\cmdParserTest.zip"};

        SettingsArchiver settings = SettingsArchiver.getInstance();
        parser.setSettings(settings);
        parser.getParsedSettings(parameters);

        OperationType expected = OperationType.READE_COMMENT;
        OperationType actual = parser.getType();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeWriteComment() throws Exception {
        String[] parameters = new String[]{"-w", "src\\test\\resources\\cmdParserTest.zip", "-c", "new archive's comment"};

        SettingsArchiver settings = SettingsArchiver.getInstance();
        parser.setSettings(settings);
        parser.getParsedSettings(parameters);

        OperationType expected = OperationType.WRITE_COMMENT;
        OperationType actual = parser.getType();

        assertEquals(expected, actual);
    }

    @Test(expected = CmdLineException.class)
    public void testDiscrepancyParametersForMainOperation() throws Exception {
        String[] parameters = new String[]{"-p", "src\\test\\resources\\cmdParserTest.zip", "-u", "src\\test\\resources\\cmdParserTest.zip", "-f", "src\\test\\resources\\dir1_add"};
        parser.getParsedSettings(parameters);
    }

    @Test(expected = CmdLineException.class)
    public void testDiscrepancyParameters() throws Exception {
        String[] parameters = new String[]{"-u", "src\\test\\resources\\cmdParserTest.zip", "-c", "src\\test\\resources\\cmdParserTest.zip", "-f", "src\\test\\resources\\dir1_add"};
        parser.getParsedSettings(parameters);
    }


}