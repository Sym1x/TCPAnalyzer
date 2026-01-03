# TCPAnalyzer

A small ***experimental*** project that captures TCP payloads in real time and tries cracking techniques (all's brute-force for now) on the intercepted data.

---

## There are 2 parts:
1\. **Sniffer** : Npcap + Pcap4J
- Capture TCP packets from a selected network interface
- Extract TCP payloads
- Write to a ***temporary*** log file (future enhancement would be to continuously log)
```bash
sniffer/sniffer_log.txt
```
2\. **Cracker** : Java logic 
- Continously watch the sniffer log
- Utils to convert from HEX to Byte to ASCII
- Implements cracking algorithms (implementing the Cracker interface) and adds them to the registry (which allows for scaling later)

---

# HOW TO TEST
It's unbuilt so you have to compile and run. The sniffer uses Pcap4j jars that are already installed in sniffer/lib.

1\. Compile and run up the network sniffer :<br>
cd into sniffer\<br>
compile:
```bash
javac -cp "lib/*" -d out src/*.java
```
run Main:
```bash
java -cp "out;lib/*" Main
```

2\. Compile and run up the cracker :<br>
from root directory<br>
compile:
```bash
javac -d ./cracker/out cracker/*.java
```
run cracker.Main:
```bash
java -cp ./cracker/out/ cracker.Main
```

### Optional
You can create and establish communication through a socket (using Java’s built-in networking libraries).<br>
Server and Client implemented in example_connection\ 
