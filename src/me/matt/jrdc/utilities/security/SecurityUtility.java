package me.matt.jrdc.utilities.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.matt.jrdc.utilities.security.exception.DecryptionException;
import me.matt.jrdc.utilities.security.exception.EncryptionException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SecurityUtility {
    /**
     *
     * @param text
     *            The input to decrypt
     * @param salt
     *            The salt
     * @param iterations
     *            The amount of iterations
     * @return The decrypted text
     * @throws Exception
     *             Error decrypting the input
     */
    public static String decrypt(final String text, final String salt,
            final int iterations) throws Exception {
        final BASE64Decoder decoder = new BASE64Decoder();
        final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        final Key key = SecurityUtility.generateKey(salt);
        final Cipher cipher = Cipher
                .getInstance(SecurityUtility.CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        String current = text;
        String decryption = null;
        if (iterations < 1) {
            throw new DecryptionException(
                    "Invalid amount of iterations. Must be > 1");
        }
        for (int i = 0; i < iterations; i++) {
            byte[] passwordBytes = decoder.decodeBuffer(current);
            passwordBytes = cipher.doFinal(passwordBytes);
            current = new String(passwordBytes, "UTF-8").substring(salt
                    .length());
            decryption = current;
        }
        return decryption;

    }

    /**
     *
     * @param text
     *            The input to encrypt
     * @param salt
     *            The salt
     * @param iterations
     *            The amount of iterations
     * @return The encrypted text
     * @throws Exception
     *             Error decrypting the input
     */
    public static String encrypt(final String text, final String salt,
            final int iterations) throws Exception {
        final BASE64Encoder encoder = new BASE64Encoder();
        final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        final Key key = SecurityUtility.generateKey(salt);
        final Cipher cipher = Cipher
                .getInstance(SecurityUtility.CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        String current = null;
        String encryption = text;
        if (iterations < 1) {
            throw new EncryptionException(
                    "Invalid amount of iterations. Must be > 1");
        }
        for (int i = 0; i < iterations; i++) {
            current = encryption;
            final byte[] cleartext = current.getBytes("UTF-8");
            encryption = encoder.encode(cipher.doFinal(cleartext));
        }
        return encryption;
    }

    /**
     * Generate the key
     *
     * @param salt
     *            Salt value to generate with
     * @return The generated secret key
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static SecretKey generateKey(final String salt)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        return new SecretKeySpec(SecurityUtility.saltedKeyValue(salt),
                SecurityUtility.KEY_ALGORITHM);
    }

    /**
     * Create a key based on a random salt
     *
     * @param input
     *            The random salt
     * @return The bytes of the key
     */
    private static byte[] saltedKeyValue(final String input) {
        String salt = input;
        if (salt == null) {
            salt = SecurityUtility.SALT;
        }
        try {
            final byte[] saltArray = salt.getBytes("UTF-8");
            if (saltArray.length > 0) {
                if (saltArray.length == 16) {
                    return saltArray;
                } else if (saltArray.length > 16) {
                    final byte[] byteArray = new byte[16];
                    for (int i = 0; i < byteArray.length; i++) {
                        byteArray[i] = saltArray[i];
                    }
                    return byteArray;
                } else if (saltArray.length < 16) {
                    final byte[] byteArray = new byte[16];
                    for (int i = 0; i < byteArray.length; i++) {
                        if (i < saltArray.length) {
                            byteArray[i] = saltArray[i];
                        } else {
                            byteArray[i] = SecurityUtility.KEY_VALUE[i];
                        }
                    }
                    return byteArray;
                }
            }
        } catch (final Exception e) {
            return SecurityUtility.KEY_VALUE;
        }
        return SecurityUtility.KEY_VALUE;
    }

    private static final String KEY_ALGORITHM = "AES";

    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private static final byte[] KEY_VALUE = new byte[] { 'T', 'h', 'i', 's',
            'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    private static final String SALT = "$1%Ad#56IyqGh7p*";
}
