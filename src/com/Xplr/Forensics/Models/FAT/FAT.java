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
package com.Xplr.Forensics.Models.FAT;

import java.util.ArrayList;

public class FAT {

    private long NumberOfClusterPerFAT; // This number also corresponds to the number of FAT entries in the File Allocation Table
    private ArrayList<FATEntry> FATEntries = new ArrayList<FATEntry>(); // This is the list of all the File Allocation Table entries
    private long FATSize; // This refers to the number of sectors per FAT , it can be easily obtained through some computations given the number of cluster per file allocation table
    private long FreeCluster;
    private int location ; // this refers to the  offset positioning  of the File Allocation Table in the Disk; we are going to proceed with the sector LBA
    // Concerning the start of the FAT , it is given

    // The FAT Backup is going to be handled in the FATBuiler

    // Let's start with the constructor
    public FAT(int SectorLba){ // this will be the default FAT of a 1 Gb virtual disk

        this.NumberOfClusterPerFAT = 262144; // The number of cluster of a disk of 1 Gb
        //Each FATEntries id in the list will map a cluster by his id

        // Now let's initialize the File Allocation Table  Entries of this FAT
        for(int i=0;i<NumberOfClusterPerFAT;i++){
             // I raised a null pointer exception , because we never know , it can ruin everything i guess
                FATEntries.add(new FATEntryBuilder().free().build()); // I won't forget to keep in mind that the cluster 0 and 1 are reserved meaning , i will start allocating starting cluster 2

        }
        // After defining File Allocation Entries of the FAT , now let's move on to fill the other fields
        // Now let's set the number of Sectors per FAT , referring to the size of the FAT
        this.FATSize = (NumberOfClusterPerFAT*4L+511)/512; // The L is to cast the value to a Long

        // Now let's set the FreeClusterNumber right
        FreeCluster = NumberOfClusterPerFAT;

        // Now let's specify the location , meaning the offset/Logical Block Address  on the disk , i think this will be at sector 2
        location = SectorLba; // SectorLba is the Sector from where the FileAllocation Table can be found
    }

    public FAT(long disk_size, int SectorLba){
       if(disk_size<272629760) // In this case the disk size is less than 260 Mb
       {
           this.NumberOfClusterPerFAT = NumberOfSectorsOfTheEntireDisk(disk_size);//In this case , a cluster corresponds to a sector , since the cluster size is 512 bytes , it can then be assimilated to a cluster
           //Each FATEntries id in the list will map a cluster by his id

           // Now let's initialize the File Allocation Table  Entries of this FAT
           for(int i=0;i<NumberOfClusterPerFAT;i++){
                // I raised a null pointer exception , because we never know , it can ruin everything i guess
                   FATEntries.add(new FATEntryBuilder().free().build());

           }
           // After defining File Allocation Entries of the FAT , now let's move on to fill the other fields
           // Now let's set the number of Sectors per FAT , referring to the size of the FAT
           this.FATSize = (NumberOfClusterPerFAT*4L+511)/512;

           // Now let's set the FreeClusterNumber right
           FreeCluster = NumberOfClusterPerFAT;
           // Now let's specify the location , meaning the offset/Logical Block Address  on the disk , i think this will be at sector 2
           location = SectorLba; // SectorLba is the Sector from where the FileAllocation Table can be found


       }
       else if(disk_size<8589934592L)
       {
           this.NumberOfClusterPerFAT = NumberOfSectorsOfTheEntireDisk(disk_size)/8; // For this intervalle of less than 8 gb or storage , the cluster size is by default 4 Kb meaning 8 sectors

           //Each FATEntries id in the list will map a cluster by his id

           // Now let's initialize the File Allocation Table  Entries of this FAT
           for(int i=0;i<NumberOfClusterPerFAT;i++){
                // I raised a null pointer exception , because we never know , it can ruin everything i guess
                   FATEntries.add(new FATEntryBuilder().free().build());

           }

           // After defining File Allocation Entries of the FAT , now let's move on to fill the other fields
           // Now let's set the number of Sectors per FAT , referring to the size of the FAT
           this.FATSize = (NumberOfClusterPerFAT*4L+511)/512;

           // Now let's set the FreeClusterNumber right
           FreeCluster = NumberOfClusterPerFAT;
           // Now let's specify the location , meaning the offset/Logical Block Address  on the disk , i think this will be at sector 2
           location = SectorLba; // SectorLba is the Sector from where the FileAllocation Table can be found


       }
       else if(disk_size<17179869184L)
       {
           this.NumberOfClusterPerFAT = NumberOfSectorsOfTheEntireDisk(disk_size)/16; // For this interval of less than 16 gb or storage , the cluster size is by default 8 Kb meaning 16 sectors

           //Each FATEntries id in the list will map a cluster by his id

           // Now let's initialize the File Allocation Table  Entries of this FAT
           for(int i=0;i<NumberOfClusterPerFAT;i++){
                // I raised a null pointer exception , because we never know , it can ruin everything i guess
                   FATEntries.add(new FATEntryBuilder().free().build());

           }

           // After defining File Allocation Entries of the FAT , now let's move on to fill the other fields
           // Now let's set the number of Sectors per FAT , referring to the size of the FAT
           this.FATSize = (NumberOfClusterPerFAT*4L+511)/512;

           // Now let's set the FreeClusterNumber right
           FreeCluster = NumberOfClusterPerFAT;
           // Now let's specify the location , meaning the offset/Logical Block Address  on the disk , i think this will be at sector 2
           location = SectorLba; // SectorLba is the Sector from where the FileAllocation Table can be found
       }
       else if (disk_size<34359738368L)
       {

           this.NumberOfClusterPerFAT = NumberOfSectorsOfTheEntireDisk(disk_size)/32; // For this interval of less than 16 gb or storage , the cluster size is by default 16 Kb meaning 16 sectors

           //Each FATEntries id in the list will map a cluster by his id

           // Now let's initialize the File Allocation Table  Entries of this FAT
           for(int i=0;i<NumberOfClusterPerFAT;i++){
                // I raised a null pointer exception , because we never know , it can ruin everything i guess
                   FATEntries.add(new FATEntryBuilder().free().build());

           }

           // After defining File Allocation Entries of the FAT , now let's move on to fill the other fields
           // Now let's set the number of Sectors per FAT , referring to the size of the FAT
           this.FATSize = (NumberOfClusterPerFAT*4L+511)/512;

           // Now let's set the FreeClusterNumber right
           FreeCluster = NumberOfClusterPerFAT;
           // Now let's specify the location , meaning the offset/Logical Block Address  on the disk , i think this will be at sector 2
           location = SectorLba; // SectorLba is the Sector from where the FileAllocation Table can be found

       }

    }


   // Now let's build a method which based on the cluster id will return the FATEntry
/**
 * Retrieves the FATEntry corresponding to the specified cluster identifier.
 * <p>
 * If the FAT entries list is empty, this method returns a new FATEntry
 * instance marked as free. Otherwise, it returns the FATEntry at the
 * position corresponding to {@code cluster_id - 2}.
 * </p>
 *
 * @param cluster_id the identifier of the cluster whose FAT entry is to be retrieved
 * @return the FATEntry associated with the given cluster ID, or a free FATEntry if the list is empty
 */
   public FATEntry findFATEntryUsingClusterIdentification(int cluster_id){
        if(FATEntries.isEmpty())
        {
            return new FATEntryBuilder().free().build();
        }else
        {
            return FATEntries.get(cluster_id-2);
        }
   }

/**
 * Allocates clusters for a file based on its byte size using the FAT (File Allocation Table) model.
 * <p>
 * This method calculates the number of clusters required for the given file size,
 * searches for free clusters in the FAT, and links them together to represent the file's storage.
 * If there are not enough free clusters available, the method returns {@code null}.
 * Otherwise, it updates the FAT entries to reflect the allocation and returns a list of allocated cluster indices.
 * </p>
 *
 * @param fileByteSize The size of the file in bytes to be allocated on the disk.
 * @return An {@code ArrayList<Integer>} containing the indices of the allocated clusters,
 *         or {@code null} if there is insufficient space.
 */
public ArrayList<Integer> AllocateFileUsingHisBytes(long fileByteSize){
        // The first step would be to be sure about the size of a cluster for this disk but this method would be the by default meaning less than 8 Gb , therefore its cluster size would be 4096
        int clusterSize = 4096;

        //Then let's determine the number of cluster needed for the allocation
        int clusterRequired = (int) fileByteSize/clusterSize;
        ArrayList<Integer> freeCluster= new ArrayList<>();
        // Then let's get the address of the free clusters
        for(int i=0;i<FATEntries.size() && freeCluster.size()<clusterRequired;i++)
        {
                if(FATEntries.get(i).isFREE()) // On parcoure la liste des FATEntry et on recueille les clusters qui ne sont pas assignes
                {
                    freeCluster.add(i);
                }
        }

        //Now let's check if the number of freecluster we got is enough
        if(freeCluster.size()<clusterRequired-1)
        {
            return null; // Since there is not enough space
        }

        // Now let's properly allocate the clusters
        for(int i=0;i<freeCluster.size();i++)
        {
            int clustId = freeCluster.get(i); // let's store the value of the first free cluster id fetched by the program on the disk to initiate the chain
            FATEntry entry = FATEntries.get(clustId-2);//With this we can fetch the correct file allocation table entry
            if(i<freeCluster.size()-1)
            {
                int nextClusterInLine = freeCluster.get(i+1);
                entry.setFatEntryValue(nextClusterInLine); // This method allow us to set which cluster will be the next in the line
            }
            else
            {
                entry.setEND_OF_CHAIN_MAX();
            }
            this.FreeCluster-=1;
        }

        return freeCluster;
}

    /**
     * Frees a chain of clusters in the FAT (File Allocation Table) starting from the specified cluster ID.
     * <p>
     * This method traverses the cluster chain beginning at {@code startClusterId}, marking each cluster
     * in the chain as free until the end of the chain is reached. The method updates the FAT entries
     * and increments the free cluster counter for each cluster that is freed.
     * </p>
     *
     * @param startClusterId the ID of the first cluster in the chain to be freed; must be >= 2 and within the FAT range
     */
    public void freeClusterChain(int startClusterId) {
        // Start freeing the chain from the given start cluster
        int clusterId = startClusterId;  

        // Loop through the chain as long as the cluster ID is valid
        while (clusterId >= 2 && clusterId < FATEntries.size() + 2) {  

         // Retrieve the FAT entry corresponding to the current cluster
        FATEntry entry = FATEntries.get(clusterId - 2);  

        // Get the next cluster in the chain (before freeing the current one)
        int nextCluster = entry.getFatEntryValue();  

        // Mark this cluster as free in the FAT
        entry.setFREE_CLUSTER();  

        // Increment the free cluster counter
        FreeCluster++;  

        // If we reach the end of the chain or a loop, stop
        if (entry.isEndOfChain() || nextCluster == clusterId) {  
              break; // End of chain reached
            }  

        // Move to the next cluster in the chain
        clusterId = nextCluster;  
            }
    }


    /**
     * Calculates the total number of sectors on the entire disk based on its size in bytes.
     *
     * @param siz the size of the disk in bytes
     * @return the number of 512-byte sectors that fit into the given disk size
     */
    private long NumberOfSectorsOfTheEntireDisk(long siz){
        long result = siz/512;
        return result;
    }
}
