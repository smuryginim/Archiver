package com.netcracker.edu.kulikov.parsingcmd;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * settings command line for archiver
 * This class handles the options command line represented as:
 * pack:           [-p, --pack]    <name_output_archive>   [-f] <names_input_file1, ...>     [-c] "<comments>"
 * unpack:         [-u, --unpack]  <name_archive>          [-d] <directory_for_unpacking>
 * add files:      [-a, --add]     <name_archive>          [-f] <names_input_file1, ...>
 * read comment:   [-r, --read]    <name_archive>
 * write comment:  [-w, --write]    <name_archive>         [-c] "<comments>"
 *
 * @author Kulikov Kirill
 */
public class SettingsArchiver {

    @Option(name = "-p", aliases = "--pack", usage = "name of the output archive",
            depends = {"-f"}, forbids = {"-u", "-d", "-a", "-w", "-r"})
    private String zipForPack = "";

    @Option(name = "-u", aliases = "--unpack", usage = "archive to the unpack",
            depends = {"-d"}, forbids = {"-p", "-f", "-a", "-w", "-r"})
    private String zipForUnpack = "";

    @Option(name = "-a", aliases = "--add", usage = "archive in which you want to add files",
            depends = "-f", forbids = {"-p", "-u", "-d", "-c", "-w", "-r"})
    private String zipForAddFiles = "";

    @Option(name = "-r", aliases = "--read", usage = "archive to read from it comments",
            forbids = {"-p", "-u", "-a", "-f", "-d", "-c", "-w"})
    private String zipForReadComment = "";

    @Option(name = "-w", aliases = "--write", usage = "archive in which to be write comment",
            forbids = {"-p", "-u", "-a", "-f", "-d", "-r"})
    private String zipForWriteComment = "";

    @Option(name = "-f", aliases = "--file", usage = "names of the input files",
            handler = StringArrayOptionHandler.class, forbids = {"-u", "-d", "-w", "-r"})
    private List<String> listFiles;

    @Option(name = "-d", aliases = "--dir", usage = "directory for unpacking",
            depends = {"-u"}, forbids = {"-p", "-f", "-a", "-w", "-r"})
    private String dirForUnpack;

    @Option(name = "-c", aliases = "--com", usage = "comments on the archive",
            forbids = {"-u", "-d", "-a", "-r"})
    private String comment;

    public SettingsArchiver() {
        listFiles = new ArrayList<>();
    }

    public String getZipForPack() {
        return zipForPack;
    }

    public String getZipForUnpack() {
        return zipForUnpack;
    }

    public String getZipForAddFiles() {
        return zipForAddFiles;
    }

    public String getZipForReadComment() {
        return zipForReadComment;
    }

    public String getZipForWriteComment() {
        return zipForWriteComment;
    }

    public List<File> getListFiles() {
        List<File> files = new ArrayList<>();
        for (String name : listFiles) {
            files.add(new File(new File(name).getAbsolutePath()));
        }
        return files;
    }

    public String getDirForUnpack() {
        return dirForUnpack;
    }

    public String getComment() {
        return comment;
    }
}
