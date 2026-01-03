import org.pcap4j.core.*;
import org.pcap4j.packet.*;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Sniffer {

    private static FileWriter sessionLogWriter;
    private static File sessionLogFile;

    public static void start() {
        try {
            Scanner scanner = new Scanner(System.in);

            // list network interfaces & choose interactively
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();
            if (interfaces == null || interfaces.isEmpty()) {
                System.out.println("No network interfaces found.");
                return;
            }

            System.out.println("Choose a network interface:");
            for (int i = 0; i < interfaces.size(); i++) {
                System.out.println(i + ": " +
                        interfaces.get(i).getName() +
                        (interfaces.get(i).getDescription() != null
                                ? " - " + interfaces.get(i).getDescription()
                                : ""));
            }

            System.out.print("Enter interface number: ");
            int choice = scanner.nextInt();

            if (choice < 0 || choice >= interfaces.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            final PcapNetworkInterface nif = interfaces.get(choice);
            System.out.println("\nSelected interface: " +
                    nif.getName() + " - " + nif.getDescription());
            System.out.println("Starting capture...\n");

            // Create session log file
            String filename = "sniffer_log.txt";
            sessionLogFile = new File(filename);
            sessionLogWriter = new FileWriter(sessionLogFile);
            System.out.println("Logging packets to: " + filename);

            // pcap handle
            final PcapHandle handle = new PcapHandle.Builder(nif.getName())
                    .snaplen(65536)
                    .promiscuousMode(PcapNetworkInterface.PromiscuousMode.PROMISCUOUS)
                    .timeoutMillis(10)
                    .build();

            // listener
            PacketListener listener = packet -> {
                if (packet.contains(TcpPacket.class)) {
                    TcpPacket tcp = packet.get(TcpPacket.class);
                    byte[] payload = tcp.getPayload() != null
                            ? tcp.getPayload().getRawData()
                            : null;

                    if (payload != null && payload.length > 0) {

                        // Print to terminal
                        System.out.println("TCP payload (" + payload.length + " bytes):");
                        System.out.println(convertHex(payload));
                        System.out.println();

                        // Write to session log
                        try {
                            sessionLogWriter.write("=== PACKET START ===\n");
                            sessionLogWriter.write("TIMESTAMP: " + LocalDateTime.now() + "\n");
                            sessionLogWriter.write("LENGTH: " + payload.length + "\n");
                            sessionLogWriter.write("PAYLOAD:\n");

                            sessionLogWriter.write(convertHex(payload) + "\n");
                            sessionLogWriter.write("=== PACKET END ===\n\n");
                            sessionLogWriter.flush();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };

            // Shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down sniffer...");
                try {
                    if (handle != null && handle.isOpen()) {
                        handle.breakLoop();
                        handle.close();
                        System.out.println("NIC restored. Sniffer closed.");
                    }

                    if (sessionLogWriter != null)
                        sessionLogWriter.close();

                    if (sessionLogFile != null && sessionLogFile.exists()) {
                        sessionLogFile.delete();
                        System.out.println("Session log deleted.");
                    }

                    System.out.flush();
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            // start capturing
            handle.loop(-1, listener);

        } catch (PcapNativeException e) {
            System.out.println("Pcap native error: " + e.getMessage());
        } catch (NotOpenException e) {
            System.out.println("Handle closed unexpectedly.");
        } catch (InterruptedException e) {
            System.out.println("Capture interrupted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // convert to hex bytes
    private static String convertHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

}
