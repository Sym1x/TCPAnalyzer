package cracker.algorithms;

import cracker.util.HexUtils;

public class XorCracker implements Cracker {

    @Override
    public String getName() {
        return "XOR Single-Byte Bruteforce";
    }

    @Override
    public void crack(String input) {
        System.out.println("=== XOR Cipher Attempts (Single-Byte) ===");

        // Convert ASCII to raw bytes
        byte[] data = input.getBytes();

        for (int key = 0; key <= 0xFF; key++) {

            byte[] decoded = xorWithKey(data, (byte) key);
            String ascii = HexUtils.bytesToAscii(decoded);

            double score = asciiScore(ascii);

            if (score > 0.70) {  // Only show likely plaintext
                System.out.printf("Key 0x%02X → %s (score: %.2f)%n",
                        key, ascii, score);
            }
        }

        System.out.println();
    }

    // === Helpers ===

    /** XORs every byte with a 1-byte key */
    private static byte[] xorWithKey(byte[] input, byte key) {
        byte[] out = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = (byte) (input[i] ^ key);
        }
        return out;
    }

    /**
     * Scores "readability" of ASCII.
     * Very basic heuristic:
     *   - printable chars raise score
     *   - garbage lowers score
     */
    private static double asciiScore(String s) {
        if (s.isEmpty()) return 0.0;

        int good = 0;

        for (char c : s.toCharArray()) {
            if (c >= 32 && c <= 126) {  // printable ASCII
                good++;
            }
        }

        return (double) good / s.length();
    }
}
