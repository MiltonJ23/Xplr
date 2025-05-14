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

public class BootSector implements SectorImpl {

    private byte[] BootSector = new byte[512]; // The size of the boot sector is 512 bytes

    /**
     * 
     * @category Boot sector
     * @param none
     * @return none
     * @throws none
     * @description The method is responsible for the initialization of the Boot Sector and therefore configure all the required fields of the Boot Sector 
     * 
     */
    BootSector() {
        // First let's fill the sectors with 0x00 and fill those that need to be filled 
        
        for(int i=0; i<512;i++){

            BootSector[0x00+i] = 0x00; // This will fill all the sectors of the boot sectors , then we will will only modify the one we need 
        }

        // Let's correctly initialize the boot sector with all the required fields 
        BootSector[0x00] = (byte) 0xEB;
        
        BootSector[0x01] = 0x58;
        
        BootSector[0x02] = (byte) 0x90; 
        // Now We are done with the Jump instruction 
        // Let's set the OEM Name 
        String OEMName  = "MSWIN4.1"; // Original Equipment Manufacturer Name , it also help know the system used for formating the disk
        
        for(int i = 0;i<OEMName.length();i++){
         
            BootSector[0x03 + i] = (byte) OEMName.charAt(i);
        
        } /* It will get the byte value for each character that composes the OEMName variable and then will convert them to byte , then is going  to store it in the appropriate offset */  
        
        // now we are done with the OEM , let's jump to the number of bytes per sector , the values of 512 is 0x0200 but since we use big endian we will store it has it is 
        
        BootSector[0x0B] = (byte) 0x02;
        
        BootSector[0x0C] = (byte) 0x00; 
        
        // Now let's address the number of sectors per cluster 
        
        // This constructor is the constructor for clusters of 4 KB , meaning a cluster will contain 8 sectors 
        
        BootSector[0x0D] = 0x08; // Here we define the number of sectors per cluster , and we didn't use the cast since the value  can be represented in the range of a byte 
        
        // Now let's address all the reserved sectors  , meaning the number of sectors that are reserved for the boot sector, here we are going to use the little endian format 
        
        BootSector[0x0E] = 0x00; 
        
        BootSector[0x0F] = 0x02;  
        
        // After this we are done with the reserved sectors , or should i say it is the size of the reserved  sectors area of the disk  and it  takes obviously 32 sectors 
        
        //Now let's specify the number of File Allocation Table for the disk 
        
        BootSector[0x10] = 0x02; 
        
        // In the following code there might be some offset that are not going to be initialized , we will initialize the bytes that the system uses to recognize the boot Sector 
        
        // Now let's set the root entries 
        BootSector[0x11] =  0x00; 
        
        BootSector[0x12] = 0x00; 
        
        BootSector[0x13] = 0x00;
        
        BootSector[0x14] = 0x00;
        
        BootSector[0x15] = 0x00;
        
        BootSector[0x16] = 0x00;

        // Now let's specify the media descriptor 


 
    }

    @Override
    public Boolean SectorInit() {
        System.out.println("Boot sector initialized");
        return true;
    }

    @Override
    public Boolean SectorFormating() {
        System.out.println("Boot sector formatted");
        return true;
    }

}
