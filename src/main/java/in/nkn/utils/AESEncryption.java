package in.nkn.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;

/**
 * This example program shows how AES encryption and decryption can be done in
 * Java. Please note that secret key and encrypted text is unreadable binary and
 * hence in the following program we display it in hexadecimal format of the
 * underlying bytes.
 *
 * @author Jayson
 */
public class AESEncryption {

    /**
     * 1. Generate a plain text for encryption 2. Get a secret key (printed in
     * hexadecimal form). In actual use this must by encrypted and kept safe.
     * The same key is required for decryption. 3.
     */
//    public static void main(String[] args) throws Exception {
//        String plainText = "Hello World Rajeev";
//        SecretKey secKey = getSecretEncryptionKey();
//        byte[] cipherText = encryptText(plainText, secKey);
//        String decryptedText = decryptText(cipherText, secKey);
//
//        System.out.println("Original Text:" + plainText);
//        System.out.println("AES Key (Hex Form):" + bytesToHex(secKey.getEncoded()));
//        System.out.println("Encrypted Text (Hex Form):" + bytesToHex(cipherText));
//        System.out.println("Descrypted Text:" + decryptedText);
//
//    }
    /**
     * gets the AES encryption key. In your actual programs, this should be
     * safely stored.
     *
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretEncryptionKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }

    /**
     * Encrypts plainText in AES using the secret key
     *
     * @param plainText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptText(String plainText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");

        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    /**
     * Decrypts encrypted byte array using the key used for encryption.
     *
     * @param byteCipherText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }

    /**
     * Convert a binary byte array into readable hex form
     *
     * @param hash
     * @return
     */
    public static String bytesToHex(byte[] hash) {

        return DatatypeConverter.printHexBinary(hash);
    }

    public static SecretKey getSecretKeyFromString(String stringKey) {

        byte[] encodedKey = stringKey.getBytes();
        //byte[] encodedKey = Base64.decodeBase64(stringKey);

        // out.print("byte[]:"+encodedKey);

        SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length,
                "AES");
        System.out.println("after encode & decode secret_key:"
                + Base64.encodeBase64String(secretKey.getEncoded()));
        return secretKey;
    }

    public static String getStringKeyFromSecretKey(SecretKey sk) {
        String stringKey = Base64.encodeBase64String(sk.getEncoded());
        System.out.println("actual secret_key in string form:" + stringKey);
        return stringKey;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String encryptIntoHex(String plainText, String stringFormOfSecretKey) throws Exception {

        SecretKey secretKeyConvertedFromStringKey = getSecretKeyFromString(stringFormOfSecretKey);
        byte[] cipherText = encryptText(plainText, secretKeyConvertedFromStringKey);
        String hexRepresentationofCipher = bytesToHex(cipherText);
        return hexRepresentationofCipher;
    }

    public static String hexToBase64(String hexString) {
        String encoded = Base64.encodeBase64String(hexStringToByteArray(hexString));
        System.out.println("ecoded to base 64 " + encoded);
        System.out.println("ecoded to base 64 " + encoded.length());
        return encoded;
    }

    public static String base64ToHex(String base64) {
        byte decoded[] = Base64.decodeBase64(base64);
        System.out.println("decoded string " + bytesToHex(decoded).length());
        return bytesToHex(decoded);
    }

    public static String decyText(String encrytext) throws Exception {
        byte[] cipherTextFromHex = hexStringToByteArray(encrytext);
        //String stringResOfSecKey = "irTLryYobR4+hk+AXaC2sQ==";
        String stringResOfSecKey = "2AAu0EQt/6hCrfx/pnc1KQ==";
        SecretKey secretKeyConvertedFromStringKey = getSecretKeyFromString(stringResOfSecKey);
        String decryptedText = decryptText(cipherTextFromHex, secretKeyConvertedFromStringKey);
        return decryptedText;
    }

    public static void main(String[] args) throws Exception {
        String plainText = "Hello World Prashant";
//        SecretKey secKey = getSecretKeyFromString("irTLryYobR4+hk+AXaC2sQ==");
//
//        byte[] cipherText = encryptText(plainText, secKey);
//        System.out.println(cipherText);
//        String decryptedText = decryptText(cipherText, secKey);
//
//        String stringFormOfSecretKey = getStringKeyFromSecretKey(secKey);
//
//        SecretKey secretKeyConvertedFromStringKey = getSecretKeyFromString(stringFormOfSecretKey);
//        String hexRepresentationofCipher = bytesToHex(cipherText);
//        System.out.println("Original Text:" + plainText);
//        System.out.println("AES Key (Hex Form):" + bytesToHex(secKey.getEncoded()));
//        System.out.println("AES Key (String Form):" + stringFormOfSecretKey);
//        System.out.println("AES Key (SecretKey From String and converted to HEX):" + bytesToHex(secretKeyConvertedFromStringKey.getEncoded()));
//
//        System.out.println("Encrypted Text (Hex Form):" + hexRepresentationofCipher);
//        System.out.println("Descrypted Text:" + decryptedText);
//
//        byte[] cipherTextFromHex = hexStringToByteArray(hexRepresentationofCipher);
//        String textConvertedbackFromStrinKey = decryptText(cipherTextFromHex, secretKeyConvertedFromStringKey);
//        System.out.println("RnD Output " + textConvertedbackFromStrinKey);
//
        
//        SecretKey secKey = getSecretEncryptionKey();
//        System.out.println("Secret key: " + secKey);
//        String secKeyIntoString = getStringKeyFromSecretKey(secKey);
//        System.out.println("String : " + secKeyIntoString);
        
        
//           String a = encryptIntoHex("hello","2AAu0EQt/6hCrfx/pnc1KQ==");
//           String b = decyText(a)  ;
//           System.out.println(b);
//        System.out.println("String Representation Of Secret key: " + secKeyIntoString);
//        String rs = encryptIntoHex("aaa", "irTLryYobR4+hk+AXaC2sQ==");

//        System.out.println("rs: "+rs);
        String key = "XueKP6nCYAfDf/LLrUlfMgr8dQaO7sMa";
        System.out.println(key.length());





    }
}