package com.example.myapplication;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HashUtils {

    /**
     * Generates SHA-1 hash for the input string.
     *
     * @param input String to hash
     * @return Hashed string in hexadecimal format
     */
    public static String sha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            return bytesToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 MessageDigest not available", e);
        }
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes Array of bytes
     * @return Hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    public static List<Float> hexToDecimalDigits(String hexString) {
        // Validate hexadecimal input
        HexToDecimalList hexygirl = new HexToDecimalList();
        Log.d("explode","explode size before "+hexygirl.hexStringToDecimalList(hexString).size());
        List<Float> decimalDigits= hexygirl.hexStringToDecimalList(hexString);
        return decimalDigits;
    }
}
