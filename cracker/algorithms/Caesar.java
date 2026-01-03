package cracker.algorithms;

public class Caesar implements Cracker {

    @Override
    public String getName() {
        return "Caesar Cipher";
    }

    @Override
    public void crack(String input) {

        System.out.println("=== Caesar Cipher ===");

        for (int shift = 1; shift < 26; shift++) {
            StringBuilder sb = new StringBuilder();

            for (char c : input.toCharArray()) {
                if (c >= 'A' && c <= 'Z') {
                    sb.append((char)((c - 'A' + shift) % 26 + 'A'));
                }
                else if (c >= 'a' && c <= 'z') {
                    sb.append((char)((c - 'a' + shift) % 26 + 'a'));
                }
                else {
                    sb.append(c);
                }
            }

            System.out.println("Shift " + shift + ": " + sb);
        }
    }
}
