//package com.auth_service.utility;
//
//import lombok.Value;
//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.binary.Hex;
//import org.springframework.stereotype.Component;
//
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.KeySpec;
//
//import javax.crypto.*;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.PBEKeySpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.*;
//import java.security.NoSuchAlgorithmException;
//@Component
//public class AesUtil {
//
//    private Cipher cipher;
//
//    @Value("${aes.passphrase}")
//    private String passphrase;
//
//    @Value("${aes.iterationCount}")
//    private int iterationCount;
//
//    @Value("${aes.keySize}")
//    private int keySize;
//
//    @Value("${aes.salt}")
//    private String salt;
//
//    @Value("${aes.iv}")
//    private String iv;
//
//    public AesUtil(){
//
//        try {
//            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException e){
//            throw fail(e);
//        }
//    }
//
//    public String decrypt(String ciphertext){
//        SecretKey key = generateKey(salt, passphrase);
//        doFinal()
//    }
//
//    private byte[] doFinal(int encryptMode,SecretKey key,String iv, byte[] bytes) {
//        try {
//            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
//            return cipher.doFinal(bytes);
//        } catch (InvalidKeyException
//                 | InvalidAlgorithmParameterException
//                 | IllegalBlockSizeException
//                 | BadPaddingException e) {
//            throw fail(e);
//        }
//    }
//    private SecretKey generateKey(String salt, String passphrase){
//        try{
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount,keySize);
//            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(),"AES");
//            return key;
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
//            throw fail(e);
//        }
//    }
//
//    public static String random(int length) {
//        byte[] salt = new byte[length];
//        new SecureRandom().nextBytes(salt);
//        return hex(salt);
//    }
//
//    public static String base64(byte[] bytes) {
//        return new String(Base64.encodeBase64(bytes));
//    }
//
//    public static byte[] base64(String str) {
//        return Base64.decodeBase64(str.getBytes());
//    }
//
//    public static String hex(byte[] bytes) {
//        return new String(Hex.encodeHex(bytes));
//    }
//
//    public static byte[] hex(String str){
//        try {
//            return Hex.decodeHex(str.toCharArray());
//        } catch (DecoderException e){
//            throw new IllegalStateException();
//        }
//    }
//
//    private IllegalStateException fail(Exception e){
//        return new IllegalStateException(e);
//    }
//}
