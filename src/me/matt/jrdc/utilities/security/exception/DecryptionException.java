package me.matt.jrdc.utilities.security.exception;

public class DecryptionException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 8733259553396042630L;

    /**
     * Decrpytion exception
     *
     * @param error
     *            error code.
     */
    public DecryptionException(final String error) {
        super(error);
    }

}
