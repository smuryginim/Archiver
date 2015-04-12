package com.netcracker.edu.kulikov.exceptions;

import com.netcracker.edu.kulikov.archiver.Archiver;

/**
 * @author Kulikov Kirill
 */
public class ArchiverException extends Exception {

    private Archiver archiver;

    public ArchiverException() {
        super();
    }

    public ArchiverException(String message) {
        super(message);
    }

    public ArchiverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArchiverException(Archiver archiver, String message, Throwable cause) {
        super(message, cause);
        this.archiver = archiver;
    }

    public ArchiverException(Throwable cause) {
        super(cause);
    }

    public Archiver getArchiver() {
        return archiver;
    }
}
