package com.netcracker.edu.kulikov.exceptions;

/**
 * @author Kulikov Kirill
 */
public class SettingsArchiverException extends ArchiverException {

    public SettingsArchiverException() {
        super();
    }

    public SettingsArchiverException(String message) {
        super(message);
    }

    public SettingsArchiverException(String message, Throwable cause) {
        super(message, cause);
    }

    public SettingsArchiverException(Throwable cause) {
        super(cause);
    }
}
