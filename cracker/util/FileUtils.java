package cracker.util;

import cracker.model.Packet;
import java.io.*;
import java.util.*;

public class FileUtils {

    public static List<Packet> loadPackets(File logFile) throws Exception {

        List<Packet> packets = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(logFile));

        String line;
        String timestamp = null;
        int length = 0;
        StringBuilder hexPayload = new StringBuilder();
        boolean inPacket = false;

        while ((line = br.readLine()) != null) {

            if (line.startsWith("=== PACKET START ===")) {
                inPacket = true;
                timestamp = null;
                length = 0;
                hexPayload.setLength(0);
                continue;
            }

            if (line.startsWith("=== PACKET END ===")) {
                inPacket = false;
                packets.add(new Packet(timestamp, length, hexPayload.toString().trim()));
                continue;
            }

            if (inPacket) {
                if (line.startsWith("TIMESTAMP:")) {
                    timestamp = line.substring("TIMESTAMP:".length()).trim();
                } else if (line.startsWith("LENGTH:")) {
                    length = Integer.parseInt(line.substring("LENGTH:".length()).trim());
                } else if (line.startsWith("PAYLOAD:")) {
                    // skip
                } else {
                    hexPayload.append(line).append(" ");
                }
            }
        }

        br.close();
        return packets;
    }
}
