/*
 * This file is part of the marquis valois distribution
 * Copyright (c) 2024 Acheron Systems.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.Xplr.Forensics.Models.VirtualDisk;

import com.Xplr.Forensics.Models.Cluster.Cluster;
import com.Xplr.Forensics.Models.Cluster.ClusterBuilder;
import com.Xplr.Forensics.Models.FAT.FAT;
import com.Xplr.Forensics.Models.FAT.FATEntry;
import com.Xplr.Forensics.Models.Journal.Journal;
import com.Xplr.Forensics.Models.Journal.JournalBuilder;
import com.Xplr.Forensics.Models.Journal.JournalEntry;
import com.Xplr.Forensics.Models.Sector.BootSector;
import com.Xplr.Forensics.Models.Sector.BootSectorBuilder;
import com.Xplr.Forensics.Models.Sector.Sector;
import com.Xplr.Forensics.Models.Sector.SectorBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a virtual disk with functionalities for file storage, retrieval, and deletion.
 * This class manages the disk's structure, including the boot sector, file allocation tables (FATs),
 * root directory, and data content. It provides methods for creating, reading, writing, and deleting
 * files on the virtual disk, as well as calculating slack space and persisting the disk's state to an
 * image file.
 */
public class VirtualDisk {

    private Long disk_size; // This value corresponds to the value in bytes of the disk size
    private String disk_name; // This corresponds to the label of the disk
    private BootSector bootSector;
    private FAT Main; // This corresponds to the Main file Allocation table
    private FAT Backup; // This corresponds to the Backup file Allocation table
    private Journal rootDirectory;
    private byte[] Content; // This is the variable that actually hold the byte array of the whole disk
    private long totalSectorsOnDisk;
    private long totalClustersOnDisk;
    private int ClusterSizeInSectors; // This is the size of a cluster in terms of number of sectors it is supposed to hold
    private int sectorSize = 512; // This corresponds to the size of a sector on the disk
    private  ArrayList<Cluster> clusters; // This is the list of clusters that are formed on the disk based on the allocation of sectors
    /**
     * Constructor for creating a VirtualDisk with a specified disk name and size.
     *
     * @param disk_name The name of the virtual disk.
     * @param disk_size The size of the virtual disk in bytes.
     */
    public VirtualDisk(String disk_name, Long disk_size) {
        this.disk_size = disk_size;
        this.disk_name = disk_name;
        
        if(disk_size<272629760) { this.ClusterSizeInSectors=1;} else if(disk_size<8589934592L) { this.ClusterSizeInSectors=8;} else if(disk_size<17179869184L){ this.ClusterSizeInSectors=16;} else if (disk_size<34359738368L) {this.ClusterSizeInSectors=32;};
        this.Content = new byte[disk_size.intValue()]; // This is the size of the entire disk
        this.bootSector = new BootSectorBuilder(disk_size, disk_name).Build(); // With this we hold the value of the bootsector
        this.rootDirectory = new JournalBuilder().getRootDirectory(); // With this we actually initialize the root directory right
        this.totalSectorsOnDisk = NumberOfSectorsOfTheEntireDisk(disk_size); // With this we know the exact number of sectors of the entire disk regardless of whether it is in the reserved region or not
        this.totalClustersOnDisk = (disk_size / (ClusterSizeInSectors * sectorSize)); // This is the total number of clusters on the disk regardless of whether it is reserved or not , of course we will reserve the cluster 0 and 1 later

        DiskSectorsInitialization(); // With this i initialized the byte array of  the entire disk
        Main = new FAT(this.disk_size, ClusterSizeInSectors * sectorSize);
        Backup = new FAT(this.disk_size, 1 + (int) Main.getFATSize() + ClusterSizeInSectors * sectorSize); // Backup FAT after Main FAT, also making sure that the offset on the disk correspond to the cluster1
        ArrayList<ArrayList<Integer>> ListOfSectorsClustersLba = FetchSectorsLbaForClusters(ClusterSizeInSectors, disk_size);
        ArrayList<Cluster> clusters = GiveClusterFormationOnDisk(ListOfSectorsClustersLba, disk_size);
    }


    /**
     * Constructs a VirtualDisk object representing a 1GB disk.
     * Initializes the disk with a specified name, size, and other parameters.
     *
     * @param disk_name The name of the virtual disk.
     */
    public VirtualDisk(String disk_name) // This constructor is the one for a classic 1 Gb disk
    {
        this.disk_size = 1073741824L; // 1GB
        this.disk_name = disk_name;
        this.ClusterSizeInSectors = 8; // Example: 8 sectors per cluster
        this.Content = new byte[1073741824]; // This is the size of the entire disk
        this.bootSector = new BootSectorBuilder(disk_size, disk_name).Build(); // With this we hold the value of the bootsector
        this.rootDirectory = new JournalBuilder().getRootDirectory();// With this we actually initialize the root directory right
        this.totalSectorsOnDisk = NumberOfSectorsOfTheEntireDisk(1073741824L); // With this we know the exact number of sectors of the entire disk regardless of whether it is in the reserved region r not
        this.totalClustersOnDisk = 262144; // This is the total number of clusters on the disk regardless of whether it is reserved or not , of course we will reserve the cluster 0 and 1 later

        DiskSectorsInitialization();// With this i initialized the byte array of  the entire disk
        Main = new FAT(this.disk_size, ClusterSizeInSectors*512);
        Backup = new FAT(this.disk_size, 1 + (int) Main.getFATSize() + ClusterSizeInSectors*512); // Backup FAT after Main FAT, also making sure that the offset on the disk correspond to the cluster1
        ArrayList<ArrayList<Integer>> ListOfSectorsClustersLba = FetchSectorsLbaForClusters(ClusterSizeInSectors, disk_size);
        ArrayList<Cluster> clusters = GiveClusterFormationOnDisk(ListOfSectorsClustersLba, disk_size);
    }















    // Let's create a method that is going to initialize the byte array of the disk with free sectos
    /**
     * Initializes the disk sectors by filling the disk content with sector data.
     * This method iterates through the disk content, dividing it into sectors of a predefined size (512 bytes).
     * For each sector, it copies the bytes from a pre-built sector (obtained from SectorBuilder) into the corresponding
     * location in the disk content. If the remaining space in the disk content is less than the sector size, it copies
     * only the remaining bytes.
     */
    public void DiskSectorsInitialization() {
        byte[] sectorBytes = new SectorBuilder(1).GetSector().getSectorBytes(); // Let's get the bytes of the sector
        //With this initialize each one of the disk sector
        for (int i = 0; i < this.Content.length; i += sectorSize) {
            //Let's check if there's enough space for the allocation
            if (i + sectorSize <= this.Content.length) // There is enough space
            {
                System.arraycopy(sectorBytes, 0, this.Content, i, sectorSize);
            } else {
                // In the case there is not enough space for an array of 512 bytes
                int restBytes = this.Content.length - i;
                System.arraycopy(sectorBytes, 0, this.Content, i, restBytes);
            }
        }
    }

    /**
     * Fetches the sector LBA (Logical Block Address) for each cluster on the disk.
     *
     * @param ClusterSizeInSector The number of sectors per cluster.
     * @param disk_size           The size of the disk in bytes.
     * @return An ArrayList of ArrayLists, where each inner ArrayList contains the
     * sector LBAs for a single cluster.
     */
    public ArrayList<ArrayList<Integer>> FetchSectorsLbaForClusters(int ClusterSizeInSector, long disk_size) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        long SectorsOnDisk = NumberOfSectorsOfTheEntireDisk(disk_size);

        for (int i = 0; i < SectorsOnDisk; i += ClusterSizeInSector) {
            ArrayList<Integer> clusterSectors = new ArrayList<>();
            for (int j = 0; j < ClusterSizeInSector; j++) {
                int sectorLba = i + j; // Calculate the sector LBA

                // Check if the sector LBA is within the bounds of the disk
                if (sectorLba < SectorsOnDisk) {
                    clusterSectors.add(sectorLba);
                } else {
                    // If the sector LBA is out of bounds, break the inner loop
                    break;
                }
            }
            result.add(clusterSectors);
        }
        return result;
    }

    // A method that will return a list of clusters that matches the allocation of the entire disk
    /**
     * Creates a list of Cluster objects based on a list of sector LBA lists.
     *
     * @param listOfSectorsLba A list of ArrayLists, where each inner ArrayList contains the LBA (Logical Block Address) of sectors belonging to a cluster.
     * @param disk_size The size of the disk (not directly used in the cluster formation logic, but potentially relevant for overall disk context).
     * @return An ArrayList of Cluster objects, where each Cluster represents a formed cluster from the input sector LBA lists.  Cluster IDs start from 2 (0 and 1 are reserved).
     */
    public ArrayList<Cluster> GiveClusterFormationOnDisk(ArrayList<ArrayList<Integer>> listOfSectorsLba, long disk_size) {
        ArrayList<Cluster> Clusteri = new ArrayList<>();
        int i = 0;
        for (ArrayList<Integer> sectorsLba : listOfSectorsLba) {
            ClusterBuilder clusterBuilder = new ClusterBuilder(i + 2, sectorsLba); // We start from 2 because 0 and 1 are reserved
            Cluster cluster = clusterBuilder.Build();
            Clusteri.add(cluster);
            i++;
        }
        return Clusteri;
    }



    /**
     * Checks if a file exists on the virtual disk.
     *
     * @param filename The name of the file to check.
     * @return True if the file exists, false otherwise.
     */
    public boolean fileExists(String filename) {
        return findJournalEntryUsingFilename(filename) != null;
    }

    /**
     * Searches for a journal entry within the root directory based on the provided filename.
     * This method iterates through all entries in the root directory, comparing the filename of each
     * journal entry with the given filename. If a match is found, the corresponding journal entry is returned.
     * If no match is found after iterating through all entries, the method returns null.
     *
     * @param filename The filename to search for within the journal entries.
     * @return The JournalEntry object if a match is found, otherwise null.
     */
    private JournalEntry findJournalEntryUsingFilename(String filename) {
        for (JournalEntry entry : rootDirectory.getEntries()) { // We just go through the entire root directory to find the journal entry that has a filename attribute value that corresponds to what we are searching for
            if (entry.getFileName().equals(filename)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Frees a chain of clusters in both the main and backup FAT tables, starting from a given cluster ID.
     * This method iterates through a chain of clusters linked in the File Allocation Tables (FATs).
     * For each cluster in the chain, it marks the corresponding FAT entries in both the main and backup FATs as free.
     * The iteration continues until the end of the cluster chain is reached (indicated by a cluster ID of 0) or if a FAT entry is not found.
     *
     * @param StartCluster_id The ID of the starting cluster in the chain to be freed.
     */
    public void freeClustersInBothFat(int StartCluster_id) {
        int current_Cluster_id = StartCluster_id;
        while (current_Cluster_id != 0) {
            FATEntry fatEntry = Main.findFATEntryUsingClusterIdentification(current_Cluster_id);
            // Now let's ensure that the entry exist
            if (fatEntry == null) break;

            int nextCluster_id = fatEntry.getFatEntryValue();// We read the value of the next cluster in the allcoation chain
            Main.findFATEntryUsingClusterIdentification(current_Cluster_id).setFREE_CLUSTER();// We set the FATEntry that points to the cluster to be free in the Main File allocation table
            Backup.findFATEntryUsingClusterIdentification(current_Cluster_id).setFREE_CLUSTER(); // We set the FATEntry value pointing on the cluster to be considered free in the Backup File allocation table
            current_Cluster_id = nextCluster_id; // We update the variable inside the loop right
        }
    }

    /**
     * Writes a file's content to the disk, updating the FAT, Journal and disk content.
     *
     * @param filename The name of the file being written.
     * @param fileContent             Byte array containing the file's content.
     * @param ClusterSizeinSector The size of each cluster in sectors.
     * @throws IOException If an I/O error occurs.
     */
    public void writeFileInDisk(String filename, byte[] fileContent, int ClusterSizeinSector) throws IOException {
        //  Allocate clusters for the file
        ArrayList<Integer> allocated_Cluster_Chain = this.Main.AllocateFileUsingHisBytes( fileContent.length, ClusterSizeinSector);
        ArrayList<Integer> allocated_Cluster_Chain_test = this.Backup.AllocateFileUsingHisBytes(fileContent.length, ClusterSizeinSector);

        //  Check if the allocation was successful and if the two FATs are synchronized
        if (!allocated_Cluster_Chain_test.equals(allocated_Cluster_Chain)) {
            System.out.println("The allocation was not successful");
            return;
        } else {
            // If the allocation was successful, we proceed to write the file content to the disk
            System.out.println("The allocation was successful");
        }

        //  Get the list of clusters
        ArrayList<Cluster> clusters = GiveClusterFormationOnDisk(FetchSectorsLbaForClusters(ClusterSizeinSector, disk_size), disk_size);

        int writtenBytes = 0; // The number of bytes already written in the disk

        //  Iterate through the allocated clusters
        for (int i = 0; i < allocated_Cluster_Chain.size(); i++) {
            int clusterId = allocated_Cluster_Chain.get(i);

            //  Find the cluster object corresponding to the cluster ID
            Cluster currentCluster = null;
            for (Cluster cluster : clusters) {
                if (cluster.getCluster_Id() == clusterId) {
                    currentCluster = cluster;
                    break;
                }
            }

            //  Check if the cluster object was found
            if (currentCluster == null) {
                System.out.println("Cluster object not found for cluster ID: " + clusterId);
                continue; // Skip to the next cluster
            }

            //  Get the list of sector LBAs for the current cluster
            ArrayList<Integer> sectorLBAs = currentCluster.getSectorsLbasHoldByCluster();

            // Iterate through the sector LBAs and write the file content
            for (int j = 0; j < sectorLBAs.size(); j++) {
                int sectorLBA = sectorLBAs.get(j);

                //  Calculate the offset in the disk's byte array for the current sector
                int sectorOffset = sectorLBA * sectorSize;

                //  Calculate the number of bytes to write to the current sector
                int bytesToWrite = Math.min(sectorSize, fileContent.length - writtenBytes);

                //  Write the file content to the disk
                System.arraycopy(fileContent, writtenBytes, Content, sectorOffset, bytesToWrite);

                // Let's  update the number of bytes written
                writtenBytes += bytesToWrite;

                // Let's check if all file content has been written so that we can break the loop and rest assured
                if (writtenBytes >= fileContent.length) {
                    break; // Here the part of the file content that correspond to a cluster
                }
            }

            //  Check if all file content has been written
            if (writtenBytes >= fileContent.length) {
                break; // All file content has been written
            }
        }
        // Create a journal entry
        JournalEntry entry = new JournalEntry(filename, "A", LocalDateTime.now(), LocalDate.now(), LocalDateTime.now(), allocated_Cluster_Chain.get(0), (long) fileContent.length);
        rootDirectory.newEntry(entry);

        // Now let's make sure the content of the rootDirectory is saved in the byte array of the disk 
        persistJournalToDisk(); // Now we are sure the Updated journal is in the disk and can be accessible at any time 
    }

    /**
     * Creates a new file on the virtual disk.
     *
     * @param filename The name of the file to create.
     * @param content  The content of the file as a byte array.
     * @throws IOException If an I/O error occurs.
     */
    public void createFile(String filename, byte[] content) throws IOException {

        writeFileInDisk(filename, content,this.ClusterSizeInSectors);

       persistJournalToDisk();
    }

    /**
     * Deletes a file from the virtual disk.
     *
     * @param filename The name of the file to delete.
     */
    public void deleteFile(String filename) {
        // Let's find the journal entry for the file to be deleted 
        JournalEntry entry = findJournalEntryUsingFilename(filename);
        if (entry == null) {
            System.out.println("File '" + filename + "' not found.");
            return;
        }

        // We are simply going to mark the FATEntries that map those clusters in the allocation chain to free so that new content can be write into it 
        freeClustersInBothFat(entry.getCluster_id());

        System.out.println("File '" + filename + "' deleted successfully.");
    }

    /**
     * Calculates the total slack space on the virtual disk.
     *
     * @return The total slack space in bytes.
     */
    public long calculateTotalSlackSpace() {
        long totalSlackSpace = 0;
        for (JournalEntry entry : rootDirectory.getEntries()) {
            int startCluster = entry.getCluster_id();
            long fileSize = entry.getFileSize();
            int clusterCount = Main.FindSizeOfAllocationChain(startCluster);
            totalSlackSpace += (long) clusterCount * ClusterSizeInSectors * sectorSize - fileSize;
        }
        return totalSlackSpace;
    }

    /**
     * Reads a file's content from the disk.
     *
     * @param filename The name of the file to read.
     * @return A byte array containing the file's content, or null if the file is not found.
     * @throws IOException If an I/O error occurs.
     */
    public byte[] readFile(String filename) throws IOException {
        //  Find the journal entry for the file
        JournalEntry entry = findJournalEntryUsingFilename(filename);
        if (entry == null) {
            System.out.println("File '" + filename + "' not found.");
            return null;
        }

        // Then get the starting cluster ID and file size from the journal entry
        int startCluster = entry.getCluster_id();
        long fileSize = entry.getFileSize();

        // Finally read the file content from the disk
        byte[] fileContent = readFileContent(startCluster, fileSize);

        return fileContent;
    }

    /**
     * Reads a file's content from the disk, given the starting cluster and file size.
     *
     * @param startCluster The starting cluster ID of the file.
     * @param fileSize     The size of the file in bytes.
     * @return A byte array containing the file's content.
     * @throws IOException If an I/O error occurs.
     */
    private byte[] readFileContent(int startCluster, long fileSize) throws IOException {
        // First let's determine the number of clusters needed to hold the file content 
        int numClustersNeeded = (int) Math.ceil((double) fileSize / (ClusterSizeInSectors * sectorSize));

        // Le'ts create a byte array to hold the file content 
        byte[] fileContent = new byte[(int) fileSize];

        // Now we read the title content from the disk starting from the given cluster 
        int bytesRead = 0;
        int currentCluster = startCluster;
        for (int i = 0; i < numClustersNeeded; i++) {
            // Calculate the offset in the disk's byte array for the current cluster
            int clusterOffset = (currentCluster - 2) * ClusterSizeInSectors * sectorSize;

            // Iterate through each byte in the cluster and read the file content
            for (int j = 0; j < ClusterSizeInSectors * sectorSize; j++) {
                // Check if there are more bytes to read , this is seriously a hazzle 
                if (bytesRead < fileSize) {
                    // Read the current byte from the disk
                    fileContent[bytesRead++] = Content[clusterOffset + j];
                } else {
                    // If all file content has been read, break the inner loop
                    break;
                }
            }

            // Get the next cluster in the chain
            FATEntry entry = Main.findFATEntryUsingClusterIdentification(currentCluster);
            if (entry == null) break;
            currentCluster = entry.getFatEntryValue();
        }

        return fileContent;
    }

    /**
        * Calculates the number of sectors in the entire virtual disk.
        *
        * @param siz The size of the virtual disk in bytes.
        * @return The total number of sectors in the virtual disk.
        */
    private long NumberOfSectorsOfTheEntireDisk(long siz) {
        long result = siz / 512;
        return result;
    }



    /**
     * Persists the journal (represented by the root directory) to the virtual disk.
     * This method converts the root directory to a byte array and writes it to cluster 2
     * of the virtual disk. It checks if the journal data fits within the cluster's capacity
     * and handles the error if it exceeds the limit.
     */
    private void persistJournalToDisk() {
        //  Let's convert the journal to a byte array using the designated method 
        byte[] journalBytes = rootDirectory.toByteArray();

        // Then we determine the offset for the cluster 2 on the disk , so that we can write the journal data there 
        int cluster2Offset = 2 * ClusterSizeInSectors * sectorSize;

        // We first see if the Journal Data fits within the cluster no 2 capacity 
        if (journalBytes.length > ClusterSizeInSectors * sectorSize) {
            System.out.println("Error: Journal size exceeds cluster 2 capacity.");
            return; 
        }

       // The case it doesn't have enough space is not yet implemented , but for now let's copy the journal content in the Content 
        System.arraycopy(journalBytes, 0, this.Content, cluster2Offset, journalBytes.length);
    }


    /**
        * Retrieves the boot sector of the virtual disk.
        *
        * @return The boot sector of the virtual disk.
        */
    public BootSector getBootSector() {
        return bootSector;
    }

    /**
        * Retrieves the content of the virtual disk.
        *
        * @return A byte array representing the content of the virtual disk.
        */
    public byte[] getContent() {
        return Content;
    }

    /**
        * Retrieves the main FAT (File Allocation Table) associated with this virtual disk.
        *
        * @return The main FAT object.
        */
    public FAT getMain() {
        return Main;
    }

    /**
        * Retrieves the backup FAT.
        *
        * @return The backup FAT.
        */
    public FAT getBackup() {
        return Backup;
    }

    /**
     * Retrieves the root directory of the virtual disk.
     *
     * @return The root directory as a Journal object.
     */
    public Journal getRootDirectory() {
        return rootDirectory;
    }

    /**
        * Retrieves the total number of sectors on the virtual disk.
        *
        * @return The total number of sectors as a long value.
        */
    public long getTotalSectorsOnDisk() {
        return totalSectorsOnDisk;
    }

    /**
        * Retrieves the total number of clusters on the virtual disk.
        *
        * @return The total number of clusters as a long value.
        */
    public long getTotalClustersOnDisk() {
        return totalClustersOnDisk;
    }

    /**
        * Retrieves the size of a cluster in sectors.
        *
        * @return The cluster size in sectors.
        */
    public int getClusterSizeInSectors() {
        return ClusterSizeInSectors;
    }

    /**
        * Retrieves the name of the virtual disk.
        *
        * @return The name of the virtual disk.
        */
    public String getDisk_name() {
        return disk_name;
    }

    /**
     * Retrieves the size of the virtual disk.
     *
     * @return The size of the virtual disk in bytes.
     */
    public Long getDisk_size() {
        return disk_size;
    }

    /**
     * Returns the byte array representation of the entire virtual disk.
     *
     * @return A byte array containing the complete virtual disk image.
     */
    public byte[] toByteArray() {
        return Content;
    }


    /**
        * Populates the virtual disk's content from a byte array.
        *
        * @param bytes The byte array containing the data to be written to the virtual disk.
        * @throws IllegalArgumentException if the size of the byte array does not match the expected disk size.
        */
    public void fromByteArray(byte[] bytes) {
        if (bytes.length != disk_size) {
            throw new IllegalArgumentException("Byte array size does not match the disk size.");
        }
        this.Content = bytes;
    }

    /**
     * Writes the virtual disk's byte array content to an image file.
     *
     * @param imageFilePath The path to the image file to be created.
     * @throws IOException If an I/O error occurs during file writing.
     */
    public void writeToImageFile(String imageFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(imageFilePath)) {
            fos.write(Content);
        }
    }
}
