package cracker.model;

public class Packet {
    public String timestamp;
    public int length;
    public String hexPayload;

    public Packet(String timestamp, int length, String hexPayload) {
        this.timestamp = timestamp;
        this.length = length;
        this.hexPayload = hexPayload;
    }
}
