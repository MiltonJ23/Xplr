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
 * Builder class for constructing and configuring {@link FATEntry} instances.
 * <p>
 * The {@code FATEntryBuilder} provides a fluent API for setting the state of a FAT entry,
 * such as marking it as free, bad, end-of-chain, or linking it to the next cluster.
 * This builder pattern simplifies the creation and configuration of {@link FATEntry} objects
 * for use in FAT (File Allocation Table) file system implementations.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 * FATEntry entry = new FATEntryBuilder()
 *     .free()
 *     .build();
 * </pre>
 *
 * <p>
 * The builder supports method chaining for convenient and readable configuration.
 * </p>
 *
 * @author [Your Name]
 * @since 1.0
 */
public class FATEntryBuilder {

    private FATEntry fatEntry;

    public FATEntryBuilder() {
        this.fatEntry = new FATEntry();
    }

    public FATEntryBuilder(FATEntry entry) {
        this.fatEntry = entry;
    }

    // Set as free cluster
    /**
     * Marks the current FAT entry as a free (unused) cluster.
     * 
     * @return this builder instance for method chaining
     */
    public FATEntryBuilder free() {
        fatEntry.setFREE_CLUSTER();
        return this;
    }

    // Set as bad cluster
    /**
     * Marks the current FAT entry as a bad cluster.
     * 
     * This method sets the status of the associated FAT entry to indicate that it is a bad cluster,
     * which typically means the cluster is unusable due to corruption or hardware errors.
     *
     * @return this builder instance for method chaining
     */
    public FATEntryBuilder bad() {
        fatEntry.setBAD_CLUSTER();
        return this;
    }

    // Set as end of chain
    /**
     * Marks the current FAT entry as the end of a chain.
     * This method sets the FAT entry's value to indicate that it is the last entry in a cluster chain,
     * typically by assigning a special end-of-chain marker (e.g., END_OF_CHAIN_MIN or END_OF_CHAIN_MAX).
     *
     * @return this builder instance for method chaining
     */
    public FATEntryBuilder endOfChain() {
        fatEntry.setEND_OF_CHAIN_MIN(); // or setEND_OF_CHAIN_MAX()
        return this;
    }

    // Set next cluster
    /**
     * Sets the value of the next cluster in the FAT entry.
     *
     * @param clusterId the identifier of the next cluster to link in the FAT entry
     * @return this FATEntryBuilder instance for method chaining
     */
    public FATEntryBuilder nextCluster(int clusterId) {
        fatEntry.setFatEntryValue(clusterId);
        return this;
    }


    // Build and return the FATEntry
    /**
     * Builds and returns the current {@link FATEntry} instance.
     *
     * @return the constructed {@link FATEntry} object
     */
    public FATEntry build() {
        return fatEntry;
    }
}
