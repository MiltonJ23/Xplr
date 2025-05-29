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
package com.Xplr.Forensics.Models.Journal;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The {@code JournalEntry} class represents a journal entry containing metadata about a file,
 * such as its name, creation time, access date, modification time, cluster ID, and file size.
 * It is designed to store this information in a byte array (Entry) for forensic analysis purposes.
 * The class provides methods for constructing a journal entry, accessing its fields, and modifying them.
 * It also includes utility methods for assigning integer and long values to the byte array in little-endian format.
 */
public class JournalEntry {

    /**
     * Represents a single entry in the journal.
     * It holds the data for the journal entry as a byte array.
     */
    private byte[] Entry; // The size of a journal entry is up to 32 bytes

    // Let's create those fields so that we can manage them here not only let them in their bytes form
    /**
     * The ID of the cluster to which this journal entry belongs.
     */
    private int cluster_id;
    /**
     * The name of the file associated with this journal entry.
     */
    private String FileName;
    /**
     * Represents the date and time when this journal entry was created.
     * It allows retrieval of specific date and time components.
     */
    private LocalDateTime CreationDateAndTime; // this value will hold the creation date and time we should be able to retrieve any of them using this variable
    /**
     * Represents the date when the file was last accessed.
     */
    private LocalDate AccessDate; // This value will hold the last date at which the file was accessed
    /**
     * Represents the date and time when this journal entry was last modified.
     */
    private LocalDateTime Modified; // This value will hold the last modified date and the last modified time
    /**
     * Represents the size of a file in bytes.
     * For example, a 3 KB file would be represented as 3 * 1024 bytes.
     */
    private long filesize; // Meaning this is the value in byte i mean , let's say a 3 kb file , it will hold 3*1024*1024

    // Now the Journal Entry has a set of fields that are assigned when one is created
    public JournalEntry() {
        this.Entry = new byte[4096];
    }

    /**
     * Constructs a new JournalEntry object, populating it with file metadata.
     * This constructor allocates space within the Entry array for various file attributes,
     * including the filename, file attributes, creation time, last access date, last modified time,
     * cluster ID, and file size. It uses little-endian assignment for the cluster ID and file size.
     *
     * @param filename      The name of the file. This is stored at the beginning of the Entry array,
     *                      with each character of the filename occupying one byte.
     * @param attribute     The file attribute (e.g., read-only, hidden). This is intended to represent
     *                      FAT file attributes, occupying 8 bits. (currently not implemented)
     * @param creationTime  The date and time when the file was created. The hour and day of the year
     *                      are extracted and stored in the Entry array.
     * @param LastAccessDate The last access date of the file. The day of the year is extracted and
     *                       stored in the Entry array.
     * @param modified      The last modified date and time of the file. The hour and day of the year
     *                      are extracted and stored in the Entry array.
     * @param cluster_id    The ID of the starting cluster of the file's allocation chain. This is stored
     *                      using little-endian byte order.
     * @param filesize      The size of the file in bytes. This is stored using little-endian byte order.
     *                      It is assumed that LittleEndianAssignment method handles the conversion to byte
     *                      representation and storage in the Entry array.
     */
    public JournalEntry(String filename, String attribute, LocalDateTime creationTime, LocalDate LastAccessDate, LocalDateTime modified, int cluster_id, Long filesize) {
        this.Entry = new byte[4096]; // Initialize Entry here
        // Let's start with the name of the file , we should make sure that the filename is registered upon file allocation
        for (int i = 0; i < filename.length(); i++)// We are going to go through each character and allocate their bytes values in the Journal Entry
        {
            this.Entry[0x00 + i] = (byte) filename.charAt(i);
        }

        int current_offset = filename.length(); // To know where to start after filling the designated space with the name of the  file in the Journal Entry

        // Now let's allocate the attribute
        //  It refers to the FAT File attribute that can be turned on or off according to the user's will , it is amounted to 8 bits

        current_offset += 8; // since the attribute is supposed to be an 8 bits value

        // Now let's allocate the file creation time
        this.Entry[(byte) current_offset] = (byte) creationTime.getHour(); // This will allow us to store the hour at which the file was created
        current_offset++; // Since the hour is 8 bits , which means it is 1 byte we can now increase the current offset by 1

        // Now let's allocate the file creation date

        this.Entry[(byte) current_offset] = (byte) creationTime.getDayOfYear(); // this is going to store the day of the year when the file was created

        current_offset++; // Since the day of the year is 8 bits , which means it is 1 byte we can now increase the current offset by 1

        // Now let's allocate the last access date , we are still going to use the day in the year when the file was last accessed
        this.Entry[(byte) current_offset] = (byte) LastAccessDate.getDayOfYear(); // This value is 8 bits long , meaning it is 1 byte long

        current_offset += 1; // As always , after allocating a new byte , to be at the right address for the others we need to increase the value of the current offset
        // Now let's allocate the Last modified time

        this.Entry[(byte) current_offset] = (byte) modified.getHour(); // This will allow us to store the last modified date of the file , how am i going to do this ? 😩 i guess we will never know 😂, i am joking , i have to 😭

        current_offset += 1; // We should not forget to increment the current offset , if not we might overwrite something by error

        this.Entry[(byte) current_offset] = (byte) modified.getDayOfYear();//With this i am going to know also the day , someone , somehow modified this disk file , not somehow , he/she will be able to ... at his own risk 😂

        current_offset += 1; // Let's not forget 😁

        // Now let's store one of the most critical information  a Journal Entry hold , the cluster id which correspond to the starting cluster of the  allocation chain of a file
        current_offset = LittleEndianAssignment(current_offset, cluster_id); // The current offset variable will hold the next addressable offset while allocating the value of the Cluster_id

        // Now let's store the size of the file in the expected area
        current_offset = LittleEndianAssignment(current_offset, filesize); // This will point to the next addressable offset , which is no need , by the way it will store the value of the file size

        // After addressing the bytes values , now let's address the different fields
        this.FileName = filename;

        this.AccessDate = LastAccessDate;

        this.Modified = modified;

        this.cluster_id = cluster_id;

        this.filesize = filesize;

    }

    /**
     * Retrieves the cluster ID associated with this journal entry.
     *
     * @return The cluster ID as an integer.
     */
    public int getCluster_id() {
        return this.cluster_id;
    }

    /**
     * Sets the cluster ID for this journal entry.
     *
     * @param clusterId The cluster ID to set.
     */
    public void setCluster_id(int clusterId) {
        this.cluster_id = clusterId;
    }

    /**
     * Retrieves the file name associated with this journal entry.
     *
     * @return The file name.
     */
    public String getFileName() {
        return this.FileName;
    }

    /**
     * Sets the file name of the journal entry.
     *
     * @param value The new file name to set.
     */
    public void setFileName(String value) {
        this.FileName = value;

    }

    /**
     * Retrieves the date and time when this journal entry was created.
     *
     * @return The LocalDateTime object representing the creation date and time.
     */
    public LocalDateTime getCreationDateAndTime() {
        return this.CreationDateAndTime;
    }

    /**
     * Sets the creation date and time of the journal entry.
     *
     * @param res The LocalDateTime object representing the creation date and time.
     */
    public void setCreationDateAndTime(LocalDateTime res) {
        this.CreationDateAndTime = res;
    }

    /**
     * Retrieves the access date of this journal entry.
     *
     * @return The LocalDate representing the access date.
     */
    public LocalDate getAccessDate() {
        return this.AccessDate;
    }

    /**
     * Sets the access date of the journal entry.
     *
     * @param tes The new access date to set.
     */
    public void setAccessDate(LocalDate tes) {
        this.AccessDate = tes;
    }

    /**
     * Retrieves the last modification timestamp of this journal entry.
     *
     * @return The LocalDateTime representing when this entry was last modified.
     */
    public LocalDateTime getModified() {
        return this.Modified;
    }

    /**
     * Sets the last modified date and time of the journal entry.
     *
     * @param naw The new LocalDateTime to set as the modified date and time.
     */
    public void setModified(LocalDateTime naw) {
        this.Modified = naw;
    }

    /**
     * Retrieves the file size of the journal entry.
     *
     * @return The file size in bytes, or null if not set.
     */
    public Long getFileSize() {
        return this.filesize;
    }

    /**
     * Sets the filesize of the journal entry.
     *
     * @param taille The filesize to set.
     */
    public void setFilesize(Long taille) {
        this.filesize = taille;
    }

    /**
     * Assigns a long value to the Byte array in little-endian format between the specified offsets.
     *
     * @param offsetStart the starting byte offset within the Byte array
     * @param value       the value to assign
     * @return the next available offset after assignment
     */
    private int LittleEndianAssignment(int offsetStart, long value) {
        if (offsetStart + 8 > Entry.length) {
            throw new IllegalArgumentException("Offset exceeds Entry array size");
        }

        Entry[offsetStart] = (byte) (value & 0xFF);
        Entry[offsetStart + 1] = (byte) ((value >> 8) & 0xFF);
        Entry[offsetStart + 2] = (byte) ((value >> 16) & 0xFF);
        Entry[offsetStart + 3] = (byte) ((value >> 24) & 0xFF);
        Entry[offsetStart + 4] = (byte) ((value >> 32) & 0xFF);
        Entry[offsetStart + 5] = (byte) ((value >> 40) & 0xFF);
        Entry[offsetStart + 6] = (byte) ((value >> 48) & 0xFF);
        Entry[offsetStart + 7] = (byte) ((value >> 56) & 0xFF);

        return offsetStart + 8;
    }

    /**
     * Assigns an int value to the Byte array in little-endian format between the specified offsets.
     *
     * @param offsetStart the starting byte offset within the Byte array
     * @param value       the value to assign
     * @return the next available offset after assignment
     */
    private int LittleEndianAssignment(int offsetStart, int value) {
        if (offsetStart + 4 > Entry.length) {
            throw new IllegalArgumentException("Offset exceeds Entry array size");
        }

        Entry[offsetStart] = (byte) (value & 0xFF);
        Entry[offsetStart + 1] = (byte) ((value >> 8) & 0xFF);
        Entry[offsetStart + 2] = (byte) ((value >> 16) & 0xFF);
        Entry[offsetStart + 3] = (byte) ((value >> 24) & 0xFF);

        return offsetStart + 4;
    }

    /**
     * Converts the journal entry to a byte array representation.
     *
     * @return The byte array representing the journal entry.
     */
    public byte[] toByteArray() {
        return Entry;
    }

    // A present toString method to print the Journal Entry in a human readable form
    @Override
    public String toString() {
        return "JournalEntry{" +
                "Entry=" + Entry +
                ", cluster_id=" + cluster_id +
                ", FileName='" + FileName + '\'' +
                ", CreationDateAndTime=" + CreationDateAndTime +
                ", AccessDate=" + AccessDate +
                ", Modified=" + Modified +
                ", filesize=" + filesize +
                '}';
    }
}
