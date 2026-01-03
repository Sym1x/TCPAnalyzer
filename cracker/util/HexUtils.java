package cracker.util;

public class HexUtils {

    // Converts hex string with spaces ("41 42 43") to byte[]
    public static byte[] hexToBytes(String hex) {
        hex = hex.replaceAll("\\s+", ""); // remove spaces
        int len = hex.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    // Converts byte[] to ASCII string
    public static String bytesToAscii(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int val = b & 0xFF;
            if (val >= 32 && val <= 126) {
                sb.append((char) val);
            } else {
                sb.append('.'); // non-printable placeholder
            }
        }
        return sb.toString();
    }
}
