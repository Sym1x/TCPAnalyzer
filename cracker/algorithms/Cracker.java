package cracker.algorithms;

public interface Cracker {
    String getName();     // e.g., "Caesar Cipher"
    void crack(String input);
}
