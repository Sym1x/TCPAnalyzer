package cracker.algorithms;

public class Vigenere implements Cracker {

    private final int keyLength = 2;

    @Override
    public String getName() {
        return "Vigenere Cipher brute-force (key length defaults to 2, change it in code; lengths beyond 4 will make your cpu sweat)";
    }

    @Override
    public void crack(String input) {
        System.out.println("=== Vigenere ===");
        System.out.println("Assuming key length = " + keyLength);

        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        // start recursive key generation
        generateKeys("", keyLength, alphabet, input);
    }

    private void generateKeys(String currentKey, int length, char[] alphabet, String ciphertext) {
        if (currentKey.length() == length) {
            String plaintext = decrypt(ciphertext, currentKey);
            System.out.println("Key [" + currentKey + "]: " + plaintext);
            return;
        }

        for (char c : alphabet) {
            generateKeys(currentKey + c, length, alphabet, ciphertext);
        }
    }

    private String decrypt(String ciphertext, String key) {
        StringBuilder out = new StringBuilder();
        int keyPos = 0;

        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                out.append(decryptChar(c, key.charAt(keyPos % key.length())));
                keyPos++;
            } else {
                out.append(c);
            }
        }

        return out.toString();
    }

    private char decryptChar(char c, char key) {
        int base = Character.isUpperCase(c) ? 'A' : 'a';
        int ci = c - base;
        int ki = Character.toUpperCase(key) - 'A';
        return (char) ((ci - ki + 26) % 26 + base);
    }
}
