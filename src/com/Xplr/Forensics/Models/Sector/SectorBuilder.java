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
package com.Xplr.Forensics.Models.Sector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The SectorBuilder class provides a builder pattern for creating and manipulating Sector objects.
 * It allows setting sector data, retrieving a copy of the sector, and exporting the sector's bytes to a binary file.
 *
 * <p>Usage example:
 * <pre>
 *     SectorBuilder builder = new SectorBuilder();
 *     builder.SetSector(byteArray);
 *     Sector sectorCopy = builder.GetSector();
 *     builder.printSectorBinaryFile("/path/to/directory");
 * </pre>
 * </p>
 *
 * Methods:
 * <ul>
 *     <li>{@link #SetSector(byte[])} - Sets the sector's byte data.</li>
 *     <li>{@link #GetSector()} - Returns a copy of the current Sector object.</li>
 *     <li>{@link #printSectorBinaryFile(String)} - Writes the sector's bytes to a binary file at the specified path.</li>
 * </ul>
 */
public class SectorBuilder {
/**
 * The {@code sector} field holds a reference to a {@link Sector} object that is being constructed or manipulated
 * by this builder. It represents the current state of the sector as it is being built.
 */
   private  Sector sector;


    /**
     * Constructs a new SectorBuilder instance and initializes its internal Sector object.
     */
    public SectorBuilder(int pos) {
        this.sector = new Sector(pos);
    }

    /**
     * Sets the bytes of the sector using the provided byte array.
     *
     * @param tem the byte array representing the sector data to set
     */
    public void SetSector(byte[] tem) {
        this.sector.setSectorBytes(tem);
    }

    /**
     * Creates and returns a new {@link Sector} object initialized with the byte data
     * from the current sector.
     *
     * @return a new {@link Sector} instance containing the same sector bytes as the current sector.
     */
    public Sector GetSector() {
        byte[] soluble = this.sector.getSectorBytes();
        Sector TESL = new Sector();
        TESL.setSectorBytes(soluble);
        return TESL;
    }

    /**
     * Writes the sector data to a binary file named "Sector.bin" at the specified path.
     * <p>
     * This method retrieves the sector's byte representation and writes it to a file.
     * If the operation is successful, it prints the file location to the console.
     * If an I/O error occurs, it prints an error message to the standard error stream.
     * </p>
     *
     * @param path the directory path where the "Sector.bin" file will be created
     */
    public void printSectorBinaryFile(String path) {
        String completePath = path + "/Sector.bin";

        byte[] SectorData = this.sector.getSectorBytes();

        try (FileOutputStream fos = new FileOutputStream(completePath)) {
            fos.write(SectorData);
            System.out.println(" The Binary File was created at the location :"+completePath);
        } catch (IOException e) {
            System.err.println("An error occurred while attempting to create the Binary "+e.getMessage());
        }


    }


}
