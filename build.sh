echo " Let's build the helping class.........................."

javac -cp build -d build src/com/Xplr/Forensics/Models/Cluster/Cluster.java
javac -cp build -d build src/com/Xplr/Forensics/Models/Cluster/ClusterBuilder.java
javac -cp build -d build src/com/Xplr/Forensics/Models/Sector/BootSector.java
javac -cp build -d build src/com/Xplr/Forensics/Models/Sector/BootSectorBuilder.java
javac -cp build -d build src/com/Xplr/Forensics/Models/Sector/Sector.java
javac -cp build -d build src/com/Xplr/Forensics/Models/Sector/SectorBuilder.java
javac -cp build -d build src/com/Xplr/Forensics/Models/Journal/JournalEntry.java
javac -cp build -d build src/com/Xplr/Forensics/Models/Journal/Journal.java
javac -cp build -d build src/com/Xplr/Forensics/Models/Journal/JournalBuilder.java
javac -cp build -d build src/com/Xplr/Forensics/Models/FAT/FATEntry.java
javac -cp build -d build src/com/Xplr/Forensics/Models/FAT/FATEntryBuilder.java
javac -cp build -d build src/com/Xplr/Forensics/Models/FAT/FAT.java
javac -cp build -d build src/com/Xplr/Forensics/Models/FAT/FATBuilder.java
javac -cp build -d build src/com/Xplr/Forensics/Models/VirtualDisk/VirtualDisk.java
javac -cp build -d build src/com/Xplr/Forensics/Models/VirtualDisk/VirtualDiskBuilder.java






echo "Let's build the Main function and cross fingers..............................."

javac -cp build -d build src/com/Xplr/Forensics/Main.java

# java -cp . com/Xplr/Forensics/Main