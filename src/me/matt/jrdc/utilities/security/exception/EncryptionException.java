package me.matt.jrdc.utilities.security.exception;

public class EncryptionException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8733259553396042630L;

    /**
     * Encrpytion exception
     * 
     * @param error
     *            Error code
     */
    public EncryptionException(final String error) {
        super(error);
    }

}
