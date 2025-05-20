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
package com.Xplr.Forensics.Models.Sector;


import java.util.*;

public class BootSector  {

    /**
     * Represents the boot sector of a storage device.
     * <p>
     * This array holds the raw bytes of the boot sector, which is typically 512 bytes in size.
     * The boot sector contains important information required to boot and identify the file system.
     */
    private byte[] bootSecto = new byte[512]; // The size of the boot sector is 512 bytes
    /**
     * Logical Block Address (LBA) representing the starting sector of the boot sector.
     * This value is typically used to identify the position of a sector on a storage device.
     * In this context, it is set to 0, indicating the first sector.
     */
    private final int Lba=0;// This stand for the Logical Block Address

    /**
     * 
     * @category Boot sector
     * @description The method is responsible for the initialization of the Boot Sector and therefore configure all the required fields of the Boot Sector , this one is the constructor for a disk of 1 Gb
     * 
     */
    public BootSector() {
        // First let's fill the sectors with 0x00 and fill those that need to be filled 
        
        for(int i=0; i<512;i++){

            bootSecto[0x00+i] = 0x00; // This will fill all the sectors of the boot sectors , then we  will only modify the one we need
        }

        // Let's correctly initialize the boot sector with all the required fields 
        bootSecto[0x00] = (byte) 0xEB;
        
        bootSecto[0x01] = 0x58;
        
        bootSecto[0x02] = (byte) 0x90;
        // Now We are done with the Jump instruction 
        // Let's set the OEM Name 
        String OEMName  = "MSWIN4.1"; // Original Equipment Manufacturer Name , it also help know the system used for formating the disk
        
        for(int i = 0;i<OEMName.length();i++){
         
            bootSecto[0x03 + i] = (byte) OEMName.charAt(i);
        
        } /* It will get the byte value for each character that composes the OEMName variable and then will convert them to byte , then is going  to store it in the appropriate offset */  
        
        // now we are done with the OEM , let's jump to the number of bytes per sector , the values of 512 is 0x0200 but since we use big endian we will store it has it is 
        
        bootSecto[0x0B] = (byte) 0x02;
        
        bootSecto[0x0C] = (byte) 0x00;
        
        // Now let's address the number of sectors per cluster 
        
        // This constructor is the constructor for clusters of 4 KB , meaning a cluster will contain 8 sectors 
        
        bootSecto[0x0D] = 0x08; // Here we define the number of sectors per cluster , and we didn't use the cast since the value  can be represented in the range of a byte
        
        // Now let's address all the reserved sectors  , meaning the number of sectors that are reserved for the boot sector, here we are going to use the little endian format 
        
        bootSecto[0x0E] = 0x00;
        
        bootSecto[0x0F] = 0x02;
        
        // After this we are done with the reserved sectors , or should i say it is the size of the reserved  sectors area of the disk  and it  takes obviously 32 sectors 
        
        //Now let's specify the number of File Allocation Table for the disk 
        
        bootSecto[0x10] = 0x02;
        
        // In the following code there might be some offset that are not going to be initialized , we will initialize the bytes that the system uses to recognize the boot Sector 
        
        // Now let's set the root entries 
        bootSecto[0x11] =  0x00;
        
        bootSecto[0x12] = 0x00;
        
        // Now this is the Small Sectors size 
        bootSecto[0x13] = 0x00;
        
        bootSecto[0x14] = 0x00;
        
        //Now let's set the Media Descriptor , the one  to tell the system which disk it is , we are going with the classical one which is 0xF8
        
        bootSecto[0x15] = (byte) 0xF8;
        
        // Now let's set the size of the sectors per track  which is information that defines how many sectors are present in each track of the disk
        bootSecto[0x18] = 0x3F;
        bootSecto[0x19] = 0x00;

        // Now let's specify the number of heads 
        bootSecto[0x1A] = (byte) 0xFF;
        bootSecto[0x1B] = 0x00;

        // Now let's specify the total numbers of sectors of the entire disk 
        bootSecto[0x20] = 0x00;
        bootSecto[0x21] = 0x00;
        bootSecto[0x22] = 0x20; // The number of sectors for this 1 Gb is 2 097 152 sectors , converted to hexadecimal is 200000 , and we are going to store it in little endian format , but since we filled the array of the sectors with 0x00 , we only make changes to meaningful bytes
        bootSecto[0x23] = 0x00;

        //Now let's specify the size of the file allocation table , which corresponds to the number of sectors pers FAT, which is 2048 sectors per FAT 
        bootSecto[0x24] = 0x00;
        bootSecto[0x25] = 0x08;


        // Since we have setted all the empty bytes to 0x00 it covers the following fields 
        // the FAT Flag
        // the FAT version 
        // 

        // Now let's set the File System Information bytes 
        bootSecto[0x30] = 0x00;
        bootSecto[0x31] = 0x01;

        // Now let's set the Backup Boot Sector responsible for the backup of the boot sector when the system is corrupted
        bootSecto[0x32] = 0x00;
        bootSecto[0x33] = 0x06;

        // We can get over with the reserved bytes since we already set them to 0x00
        // Now let's set the Drive Number , which represents a kinda like id to say that the disk is a fixed disk or floppy disk
        bootSecto[0x42] = (byte) 0x80;

        // Now let's set the Extended Boot signature , which is a byte that is used to identify the boot sector
        bootSecto[0x44] = (byte) 0x29;

        //Now let's set the Volume Identifier , which is unique for each produced disk regardless of the system used to format it
        // We will set it to find the hexadecimal sequence of bytes that the RandomUniqueVolumeIDGenerator method will generate
        byte[] VolumeID = RandomUniqueVolumeIDGenerator(); 
        bootSecto[0x45] = VolumeID[0];
        bootSecto[0x46] = VolumeID[1];
        bootSecto[0x47] = VolumeID[2];
        bootSecto[0x48] = VolumeID[3];
        // Now let's specify the Volume label , the label will be called "Xplr"
        String VolumeLabel = "Xplr Disk"; // The label of the disk , it is used to name the disk
        for(int i=0 ; i<VolumeLabel.length();i++){ // we are going to convert each character of the  labale seaquence and store it in  
            bootSecto[0x49+i] = (byte) VolumeLabel.charAt(i);
        }

        // Now let's set the File System Type , which is the type of file system used to format the disk
        String FileSysstemType  = "FAT32   "; 
        for(int i=0 ; i<FileSysstemType.length();i++){ // we are going to convert each character of the  labale seaquence and store it in  
            bootSecto[0x54+i] = (byte) FileSysstemType.charAt(i);
        }

        //  Now let's attack the boot code , which is the code that is executed when the system boots up
        // we have to replace the 0x00 bytes with 0x86 for each concerned bytes in the offset of the BootCode 
       bootSecto[0x5A] = (byte) 0xFA;// this is the CLI instruction that is going to clears the interrupt flag by disabling the hardware interrupts
       bootSecto[0x5B] = (byte) 0xF4;// this is the HLT instruction that is going to halt the CPU until the next external interrupt is received

        // Now let's conclude with the Boot Signature 
        bootSecto[0x1FE] = (byte) 0x55;
        bootSecto[0x1FF] = (byte) 0xAA;

    }

    public BootSector(long disk_size, String disk_name){
        // First let's fill the sectors with 0x00 and fill those that need to be filled

        for(int i=0; i<512;i++){

            bootSecto[0x00+i] = 0x00; // This will fill all the sectors of the boot sectors , then we will will only modify the one we need
        }

        // Let's correctly initialize the boot sector with all the required fields
        bootSecto[0x00] = (byte) 0xEB;

        bootSecto[0x01] = 0x58;

        bootSecto[0x02] = (byte) 0x90;
        // Now We are done with the Jump instruction
        // Let's set the OEM Name
        String OEMName  = "MSWIN4.1"; // Original Equipment Manufacturer Name , it also help know the system used for formating the disk

        for(int i = 0;i<OEMName.length();i++){

            bootSecto[0x03 + i] = (byte) OEMName.charAt(i);

        } /* It will get the byte value for each character that composes the OEMName variable and then will convert them to byte , then is going  to store it in the appropriate offset */

        // now we are done with the OEM , let's jump to the number of bytes per sector , the values of 512 is 0x0200 but since we use big endian we will store it has it is

        bootSecto[0x0B] = (byte) 0x02;

        bootSecto[0x0C] = (byte) 0x00;



        if(disk_size<262144){// Meaning if the disk_size is less than 256 MB

            bootSecto[0x0D] = 0x01; // Meaning 1  sector of 512 bytes per cluster

            // Now let's change some  parameters of the disk depending on the size of the disk , such as the number of sectors of the entire disk , the number of cluster of the entire disk , the number of sectors per FAT

            // Let's start with the number of sectors of the entire disk
            LittleEndianAssignment((byte) 0x20, NumberOfSectorsOfTheEntireDisk(disk_size));

            //Now let's specify the size of the file allocation table , which corresponds to the number of sectors per FAT
            // In this case , the number of Sectors considering our disk size  is
            long NbSector = disk_size/512;

            //This is the number of Clusters per File Allocation Table

            LittleEndianAssignment((byte) 0x24 , NbSector);




        }else if(disk_size <8589934592L) { // The L is because the value is too big for an int , so we need to specify to the jvm that we will use that number as a long

            // meaning we will have 8 sectors per cluster

            bootSecto[0x0D] = 0x08;// Here we define the number of sectors per cluster , and we didn't use the cast since the value  can be represented in the range of a byte

            // Now let's change some  parameters of the disk depending on the size of the disk , such as the number of sectors of the entire disk , the number of cluster of the entire disk , the number of sectors per FAT

            // Let's start with the number of sectors of the entire disk
            LittleEndianAssignment((byte) 0x20, NumberOfSectorsOfTheEntireDisk(disk_size));

            //Now let's specify the size of the file allocation table , which corresponds to the number of sectors per FAT
            long NbCluster = disk_size / (8*512) ;
            LittleEndianAssignment((byte) 0x24 , NbCluster);




        } else if (disk_size<17179869184L) {

            // In this case we will have 16 sectors per cluster 
            bootSecto[0x0D] = 0x10;
            // Now let's change some  parameters of the disk depending on the size of the disk , such as the number of sectors of the entire disk , the number of cluster of the entire disk , the number of sectors per FAT

            // Let's start with the number of sectors of the entire disk
            LittleEndianAssignment((byte) 0x20, NumberOfSectorsOfTheEntireDisk(disk_size));

            //Now let's specify the size of the file allocation table , which corresponds to the number of sectors per FAT
            long NbCluster = disk_size / (16*512) ;
            LittleEndianAssignment((byte) 0x24 , NbCluster);

        }else if(disk_size<34359738368L){

            //Here we will have to go with 32 sectors per cluster
            bootSecto[0x0D] = 0x20; // Meaning we will have 32 sectors per cluster
            // Now let's change some  parameters of the disk depending on the size of the disk , such as the number of sectors of the entire disk , the number of cluster of the entire disk , the number of sectors per FAT

            // Let's start with the number of sectors of the entire disk
            LittleEndianAssignment((byte) 0x20, NumberOfSectorsOfTheEntireDisk(disk_size));

            //Now let's specify the size of the file allocation table , which corresponds to the number of sectors per FAT
            long NbCluster = disk_size / (32*512) ;
            LittleEndianAssignment((byte) 0x24 , NbCluster);
        }
        // Now let's address all the reserved sectors  , meaning the number of sectors that are reserved for the boot sector, here we are going to use the little endian format

        bootSecto[0x0E] = 0x00;

        bootSecto[0x0F] = 0x02;

        // After this we are done with the reserved sectors , or should i say it is the size of the reserved  sectors area of the disk  and it  takes obviously 32 sectors

        //Now let's specify the number of File Allocation Table for the disk

        bootSecto[0x10] = 0x02;

        // In the following code there might be some offset that are not going to be initialized , we will initialize the bytes that the system uses to recognize the boot Sector

        // Now let's set the root entries
        bootSecto[0x11] =  0x00;

        bootSecto[0x12] = 0x00;

        // Now this is the Small Sectors size
        bootSecto[0x13] = 0x00;

        bootSecto[0x14] = 0x00;

        //Now let's set the Media Descriptor , the one  to tell the system which disk it is , we are going with the classical one which is 0xF8

        bootSecto[0x15] = (byte) 0xF8;

        // Now let's set the size of the sectors per track  which is information that defines how many sectors are present in each track of the disk
        bootSecto[0x18] = 0x3F;
        bootSecto[0x19] = 0x00;

        // Now let's specify the number of heads
        bootSecto[0x1A] = (byte) 0xFF;
        bootSecto[0x1B] = 0x00;




        // Since we have setted all the empty bytes to 0x00 it covers the following fields
        // the FAT Flag
        // the FAT version
        //

        // Now let's set the File System Information bytes
        bootSecto[0x30] = 0x00;
        bootSecto[0x31] = 0x01;

        // Now let's set the Backup Boot Sector responsible for the backup of the boot sector when the system is corrupted
        bootSecto[0x32] = 0x00;
        bootSecto[0x33] = 0x06;

        // We can get over with the reserved bytes since we already set them to 0x00
        // Now let's set the Drive Number , which represents a kinda like id to say that the disk is a fixed disk or floppy disk
        bootSecto[0x42] = (byte) 0x80;

        // Now let's set the Extended Boot signature , which is a byte that is used to identify the boot sector
        bootSecto[0x44] = (byte) 0x29;

        //Now let's set the Volume Identifier , which is unique for each produced disk regardless of the system used to format it
        // We will set it to find the hexadecimal sequence of bytes that the RandomUniqueVolumeIDGenerator method will generate
        byte[] VolumeID = RandomUniqueVolumeIDGenerator();
        bootSecto[0x45] = VolumeID[0];
        bootSecto[0x46] = VolumeID[1];
        bootSecto[0x47] = VolumeID[2];
        bootSecto[0x48] = VolumeID[3];
        // Now let's specify the Volume label , the label will be called whatever the value of disk_name is to a maximum of 8 bytes , so 8 character
        String VolumeLabel = disk_name; // The label of the disk , it is used to name the disk
        for(int i=0 ; i<VolumeLabel.length();i++){ // we are going to convert each character of the  labale seaquence and store it in
            bootSecto[0x49+i] = (byte) VolumeLabel.charAt(i);
        }

        // Now let's set the File System Type , which is the type of file system used to format the disk
        String FileSysstemType  = "FAT32   ";
        for(int i=0 ; i<FileSysstemType.length();i++){ // we are going to convert each character of the  labale seaquence and store it in
            bootSecto[0x54+i] = (byte) FileSysstemType.charAt(i);
        }

        //  Now let's attack the boot code , which is the code that is executed when the system boots up
        // we have to replace the 0x00 bytes with 0x86 for each concerned bytes in the offset of the BootCode
        bootSecto[0x5A] = (byte) 0xFA;// this is the CLI instruction that is going to clears the interrupt flag by disabling the hardware interrupts
        bootSecto[0x5B] = (byte) 0xF4;// this is the HLT instruction that is going to halt the CPU until the next external interrupt is received

        // Now let's conclude with the Boot Signature
        bootSecto[0x1FE] = (byte) 0x55;
        bootSecto[0x1FF] = (byte) 0xAA;
    }


    // Random unique volume ID generator , helping to identify a disk

    private  byte[]  RandomUniqueVolumeIDGenerator(){

        Random randomProcess = new Random();// This is the number of times the choice of a number for random number is going to be done
        int randomRound = randomProcess.nextInt(127); // This is the number of times the choice of a number for random number is going to be done
        int bytevalue ; // will contains the return random number that is going to be converted to a byte 
        byte[] randomSequence = new byte[4] ; // This is the random sequence that is going to be returned

        while(randomRound>0){
            bytevalue = randomProcess.nextInt(256); // This is the number of times the choice of a number for random number is going to be done
            randomSequence[randomProcess.nextInt(4)] = (byte) bytevalue; // This is the random sequence that is going to be returned
            randomRound--;
        }
        
        return randomSequence;
    }


    public byte[] getBootSectorBytes() {
            return bootSecto;
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

    /**
     * Assigns a long value to the BootSector array in little-endian format between the specified offsets.
     *
     * @param offsetStart the starting byte offset within the BootSector array
     * @param siz         the value to assign, which will be split into bytes
     */
    private void LittleEndianAssignment(byte offsetStart, long siz) {
        byte[] valueToAssign = Long.toString(siz).getBytes(); 
        int byteArraySize = valueToAssign.length - 1;
        int maxBytes = 9;
        byte StartOfByteArray = offsetStart;

        for (int i = 0; i < maxBytes; i++) {
            if (byteArraySize >= 0) {
                bootSecto[StartOfByteArray] = valueToAssign[byteArraySize];
                byteArraySize--;
            } else {
                bootSecto[StartOfByteArray] = (byte) '0';
            }
            StartOfByteArray += 1;
        }
    }

}
