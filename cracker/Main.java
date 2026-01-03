package cracker;

import cracker.util.HexUtils;
import cracker.util.FileUtils;
import cracker.algorithms.Cracker;
import cracker.algorithms.AlgorithmRegistry;
import cracker.model.Packet;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // Select algorithm
        System.out.println("=== Select cracking algorithm ===");
        List<Cracker> list = AlgorithmRegistry.getAlgorithms();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + " - " + list.get(i).getName());
        }
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        Cracker algorithm = AlgorithmRegistry.getByIndex(choice);

        System.out.println("\n[*] Selected: " + algorithm.getName());
        System.out.println("--------------------------------------\n");

        // Path to sniffer log
        File logFile = new File("./sniffer/sniffer_log.txt");

        System.out.println("[*] Cracker started.");
        System.out.println("[*] Waiting for sniffer log file: " + logFile.getAbsolutePath());

        while (!logFile.exists()) {
            Thread.sleep(500);
        }

        System.out.println("[*] Sniffer log detected.");
        System.out.println("[*] Beginning live cracking...\n");

        int lastCount = 0;

        while (true) {

            List<Packet> packets = FileUtils.loadPackets(logFile);

            if (packets.size() > lastCount) {

                System.out.println("=== New packets detected: " + (packets.size() - lastCount) + " ===\n");

                for (int i = lastCount; i < packets.size(); i++) {

                    Packet p = packets.get(i);

                    System.out.println(">> PACKET @ " + p.timestamp);
                    System.out.println("Hex length = " + p.hexPayload.length());

                    byte[] bytes = HexUtils.hexToBytes(p.hexPayload);
                    String ascii = HexUtils.bytesToAscii(bytes);

                    System.out.println("ASCII:");
                    System.out.println(ascii + "\n");

                    // run selected algorithm
                    algorithm.crack(ascii);

                    System.out.println("\n----------------------------------------\n");
                }

                lastCount = packets.size();
            }

            Thread.sleep(1500);
        }
    }
}
