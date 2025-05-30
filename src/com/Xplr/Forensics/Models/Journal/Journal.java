/*
 * This file is part of the marquis valois distribution
 * Copyright (c) 2024 Acheron Systems corp.
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

import java.io.FileWriter;
import java.util.ArrayList;
import java.nio.file.*;

/**
 * The {@code Journal} class represents a collection of journal entries, along with metadata
 * such as the Logical Block Address (LBA) where the journal is located on a disk image.
 * It provides functionalities to manage journal entries, including adding new entries,
 * retrieving existing entries, setting the LBA, and exporting the journal content to a file.
 *
 * <p>The class uses an {@code ArrayList} to store {@link JournalEntry} objects, allowing
 * for dynamic addition and retrieval of entries. It also stores the LBA as a byte array,
 * which is crucial for locating the journal within a disk image during forensic analysis.
 *
 * <p>The {@code Journal} class includes methods for:
 * <ul>
 *   <li>Creating a new journal with an empty list of entries and an initial LBA.</li>
 *   <li>Adding new journal entries.</li>
 *   <li>Retrieving all journal entries.</li>
 *   <li>Setting the Logical Block Address (LBA) of the journal.</li>
 *   <li>Exporting the journal content to a specified file path.</li>
 *   <li>Returning a string representation of the journal, including all its entries.</li>
 * </ul>
 *
 * <p>This class is designed to be a core component in forensic applications that require
 * the analysis and management of journal data extracted from disk images.
 *
 * @see JournalEntry
 */
public class Journal {
    
    // This will hold a list of journal entries     



    /**
        * A list to store the journal entries.
        * Each entry represents a specific event or piece of information recorded in the journal.
        */
    private ArrayList<JournalEntry> entries;



    /**
     * Represents the Logical Block Address (LBA) of the journal in the disk image.
     * This byte array stores the LBA, which is a unique identifier for the journal's location on the disk.
     */
    private byte[] JournalLba;  // This value is the Logical Block Address of the Journal in the Disk Image



    /**
        * Constructs a new Journal object.
        * Initializes the entries list as an empty ArrayList and allocates a byte array for JournalLba.
        * The JournalLba is initialized with a size of 1 byte, representing the Logical Block Address.
        */
    public Journal() {
        this.entries = new ArrayList<>();
        this.JournalLba = new byte[1]; // This is considering the size of the LBA to be 1 byte
    }


    /**
        * Retrieves the list of journal entries.
        *
        * @return An ArrayList containing the journal entries.
        */
    public ArrayList<JournalEntry> getEntries() {
        return entries;
    }

    /**
        * Sets the entries for this journal.
        *
        * @param entries The new list of journal entries to set.
        */
    public void setEntries(ArrayList<JournalEntry> entries) {
        this.entries = entries;
    }

    /**
        * Retrieves the Logical Block Address (LBA) of the journal.
        *
        * @return A byte array representing the LBA of the journal.
        */
    public byte[] getJournalLba() {
        return JournalLba;
    }

    /**
        * Sets the Logical Block Address (LBA) of the journal.
        *
        * @param journalLba A byte array representing the LBA of the journal.
        */
    public void setJournalLba(byte[] journalLba) {
        JournalLba = journalLba;
    }


    // Now let's create the method to add an entry to the journal
    /**
        * Adds a new entry to the journal.
        *
        * @param entry The journal entry to add.
        * @throws IllegalArgumentException if the provided entry is null.
        */
    public void newEntry(JournalEntry entry){
        if (entry != null) {
            this.entries.add(entry);
        } else {
            throw new IllegalArgumentException("Entry cannot be null");
        }
    }

    // Now let's create a method to export the journal by creating a new text file at a desired path
    /**
 * Exports the content of the Journal object to a specified file path.
 *
 * @param filePath The path to the file where the journal content will be exported.
 * @throws RuntimeException if an error occurs during file creation or writing.
 */
public void exportJournal(String filePath) {
    Path file = Paths.get(filePath);
    try {
        Files.createFile(file); // Create the file at the specified path if it does not exist
        // Write the journal content to the file
        try (FileWriter tes = new FileWriter(file.toString())) {
            tes.write(this.toString());
        }
        System.out.println("Journal exported successfully to: " + file);
    } catch (Exception e) {
        throw new RuntimeException("Error exporting journal to " + filePath, e);
    }
}


    // Now let's create a method to return all the content of the journal as a string

    /**
     * Returns a string representation of the journal, including all its entries.
     * Each entry's string representation is appended to the result, separated by a newline.
     *
     * @return A string representation of the journal.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Journal Entries:\n");
        for (JournalEntry entry : entries) {
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }

    public byte[] toByteArray() {
         // This method converts the entire journal into a byte array and then returns it 
        StringBuilder sb = new StringBuilder();
        sb.append("Journal Entries:\n");
        for (JournalEntry entry : entries) {
            sb.append(entry.toString()).append("\n");
        }
        String journalContent = sb.toString();
        return journalContent.getBytes(); // Convert the string to bytes using the default charset
    }



}
