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

/**
 * Represents a single FAT (File Allocation Table) entry for FAT32-like filesystems.
 * <p>
 * Each FAT entry is 32 bits (4 bytes) wide, with 28 bits used for cluster addressing.
 * This class provides methods to interpret and manipulate the entry value, including
 * checking for special cluster types (free, bad, end-of-chain), and converting to/from
 * a 4-byte array representation.
 * </p>
 *
 * <ul>
 *   <li>FREE_CLUSTER: Indicates a free cluster (0x00000000).</li>
 *   <li>BAD_CLUSTER: Indicates a bad cluster (0x0FFFFFF7).</li>
 *   <li>END_OF_CHAIN_MIN: Minimum value for end-of-chain marker (0x0FFFFFF8).</li>
 *   <li>END_OF_CHAIN_MAX: Maximum value for end-of-chain marker (0x0FFFFFFFF).</li>
 *   <li>RESERVED_MASK: Mask for reserved bits (0xF0000000).</li>
 * </ul>
 *
 * <p>
 * Provides methods to:
 * <ul>
 *   <li>Check if the entry is free, bad, or an end-of-chain marker.</li>
 *   <li>Set the entry to special values (free, bad, end-of-chain).</li>
 *   <li>Convert the entry value to and from a 4-byte array (little-endian order).</li>
 *   <li>Get and set the raw entry value.</li>
 * </ul>
 * </p>
 */
public class FATEntry {
    //this class represents a single FATentry . An entry of 32 bits(4 bytes ) with 28 bits are used for cluster addressing

    /**
     * Constant representing a free (unused) cluster in the FAT (File Allocation Table) file system.
     * A cluster with this value indicates that it is available for allocation.
     */
    private final int FREE_CLUSTER =  0x00000000;
    
    /**
     * Constant representing a bad cluster marker in the FAT file system.
     * A cluster with this value is considered unusable due to corruption or physical damage.
     */
    private final int BAD_CLUSTER =  0x0FFFFFF7;
    
    /**
     * The minimum value indicating the end of a cluster chain in the FAT file system.
     * In FAT32, cluster values greater than or equal to this constant signify that the current cluster
     * is the last in the chain (end-of-chain marker).
     */
    private final int END_OF_CHAIN_MIN = 0x0FFFFFF8;
    
    
    /**
     * The maximum value used to indicate the end of a cluster chain in the FAT (File Allocation Table) file system.
     * This constant typically represents the special marker for the last cluster in a file, signaling that there are no further clusters to follow.
     */
    private final int END_OF_CHAIN_MAX = 0x0FFFFFFFF;
    
    
    
    /**
     * Bitmask used to extract the reserved bits from a FAT (File Allocation Table) entry.
     * The reserved bits are located in the highest 4 bits (bits 28-31) of the 32-bit entry.
     */
    private final int RESERVED_MASK =  0xF0000000;




    /**
     * Stores the value of a single entry in the File Allocation Table (FAT).
     * This value typically represents the next cluster in a file chain or a special marker
     * indicating the end of a file, a free cluster, or a bad cluster, depending on the FAT type.
     */
    private int FatEntryValue;

    public FATEntry(){
        this.FatEntryValue = FREE_CLUSTER;
    }

    /**
     * Checks if this FAT entry represents a free (unused) cluster.
     *
     * @return {@code true} if the FAT entry value indicates a free cluster; {@code false} otherwise.
     */
    public boolean isFREE(){
        return this.FatEntryValue == FREE_CLUSTER;
    }
    
    /**
     * Checks if this FAT entry represents a bad cluster.
     *
     * @return {@code true} if the FAT entry value indicates a bad cluster; {@code false} otherwise.
     */
    public boolean isBAD(){
        return this.FatEntryValue == BAD_CLUSTER;
    }
    
    /**
     * Checks if the FAT entry value is equal to the minimum end-of-chain marker (END_OF_CHAIN_MIN).
     *
     * @return {@code true} if this FAT entry value is the minimum end-of-chain value; {@code false} otherwise.
     */
    public boolean isEOCMIN(){
        return this.FatEntryValue == END_OF_CHAIN_MIN;
    }
    
    /**
     * Checks if the FAT entry value is equal to the maximum end-of-chain marker (END_OF_CHAIN_MAX).
     *
     * @return {@code true} if this FAT entry value is the maximum end-of-chain marker; {@code false} otherwise.
     */
    public boolean isEndOfChain(){
       return FatEntryValue >= END_OF_CHAIN_MIN && FatEntryValue <= END_OF_CHAIN_MAX;
    }


    
    /**
     * Sets the FAT entry value to indicate that the cluster is free.
     * This method assigns the constant {@code FREE_CLUSTER} to the {@code FatEntryValue} field,
     * marking the cluster as available for allocation in the FAT file system.
     */
    public void setFREE_CLUSTER(){
        this.FatEntryValue = FREE_CLUSTER;
    }
    
    /**
     * Sets the FAT entry value to indicate a bad cluster.
     * This method marks the current FAT entry as a bad cluster by assigning it the BAD_CLUSTER constant.
     */
    public void setBAD_CLUSTER(){
        this.FatEntryValue = BAD_CLUSTER;
    }
    
    /**
     * Sets the FAT entry value to the minimum value that indicates the end of a chain (END_OF_CHAIN_MIN).
     * This is typically used to mark the last cluster in a file allocation table (FAT) chain.
     */
    public void setEND_OF_CHAIN_MIN(){
        this.FatEntryValue = END_OF_CHAIN_MIN;
    }
    
    /**
     * Sets the FAT entry value to the maximum value indicating the end of a chain.
     * This method assigns the constant {@code END_OF_CHAIN_MAX} to {@code FatEntryValue},
     * marking this entry as the last in a sequence within the FAT structure.
     */
    public void setEND_OF_CHAIN_MAX(){
        this.FatEntryValue = END_OF_CHAIN_MAX;
    }


    /**
     * Converts the FAT entry value to a 4-byte array in little-endian order.
     * <p>
     * Each byte in the returned array represents a portion of the 32-bit FAT entry value,
     * with the least significant byte at index 0 and the most significant byte at index 3.
     * This is useful for serializing the FAT entry for storage or transmission.
     *
     * @return a 4-byte array representing the FAT entry value in little-endian order
     */
    public byte[] toBytes() {

        return new byte[] {

                (byte) (FatEntryValue & 0xFF),//  this will get the lowest byte and doing so on the other meanwhile 0xFF ensures that we are using unsigned bytes

                (byte) ((FatEntryValue >> 8) & 0xFF),

                (byte) ((FatEntryValue >> 16) & 0xFF),

                (byte) ((FatEntryValue >> 24) & 0xFF)
        };
    }

    /**
     * Populates the FAT entry value from a 4-byte array in little-endian order.
     *
     * @param bytes a byte array of length 4 representing the FAT entry in little-endian format
     * @throws IllegalArgumentException if the byte array is not exactly 4 bytes long
     */
    public void fromBytes(byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Byte array must be exactly 4 bytes long.");
        }
        FatEntryValue = ((bytes[3] & 0xFF) << 24) | ((bytes[2] & 0xFF) << 16) | ((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF);
    }

    /**
     * Returns the value of the FAT (File Allocation Table) entry.
     *
     * @return the integer value representing the FAT entry.
     */
    public int getFatEntryValue() {
        return FatEntryValue & 0x0FFFFFFF; // As the same as the setFatEntryValue , we are only taking the leat 28 bits and droping the last 4 bits in the bin and replacing them with zeros
    }

    /**
     * Sets the value of the FAT (File Allocation Table) entry.
     * This method allow us to set the next cluster in the linked list of entries allocated for a file
     * @param fatEntryValue the integer value to set for this FAT entry
     */
    public void setFatEntryValue(int fatEntryValue) {
        FatEntryValue = fatEntryValue & 0x0FFFFFFF; // This will allow us to take only the 28 least bits since the 4 on top of them are reserved ; our 28 bits are the ones used for the cluster addressing
    }


    /**
        * Returns a string representation of the FAT entry value in hexadecimal format.
        *
        * @return A string representing the FAT entry value, formatted as a hexadecimal number with leading zeros.
        *         For example, if FatEntryValue is 255, the returned string will be "0x000000FF".
        */
    @Override
    public String toString() {
        return String.format("0x%08X", FatEntryValue);
    }



}
