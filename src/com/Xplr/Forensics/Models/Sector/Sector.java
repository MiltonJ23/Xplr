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

/**
 * Represents a 512-byte sector, typically used in disk or memory forensics.
 * Provides methods to access, modify, and clear the sector data.
 *
 * <p>
 * The sector is initialized to zero bytes upon creation and can be reset to all zeros
 * using the {@link #emptyTheSector()} method.
 * </p>
 *
 * <ul>
 *   <li>{@link #getSectorBytes()} - Returns the current sector data as a byte array.</li>
 *   <li>{@link #setSectorBytes(byte[])} - Sets the sector data from the provided byte array.</li>
 *   <li>{@link #emptyTheSector()} - Clears the sector by setting all bytes to zero.</li>
 * </ul>
 */
public class Sector {
    /**
     * Represents the data contained within a single sector.
     * Each sector is 512 bytes in size, which is a common sector size for storage devices.
     */
    private byte[] sector = new byte[512] ;




    /**
     * Logical Block Address (LBA) representing the sector's position on the storage device.
     * LBA is used to specify the exact location of a sector for read/write operations.
     */
    private int Lba;

    /**
     * Constructs a new Sector object and initializes all 512 bytes of the sector to zero.
     * This ensures that the sector starts in a clean state, with no residual data.
     */
    public Sector(int lba){
        for(int i=0;i<512;i++){// Nous allons initialiser tout le secteur avec du vide
            sector[0x00+i] = 0x00;
        }
        this.Lba = lba;
    }

    public Sector() {
        for(int i=0;i<512;i++){// Nous allons initialiser tout le secteur avec du vide
            sector[0x00+i] = 0x00;
        }
    }

    /**
     * Returns the raw byte array representing the contents of this sector.
     *
     * @return a byte array containing the sector data
     */
    public byte[] getSectorBytes(){
        return sector ;
    }

    /**
     * Sets the bytes of this sector.
     *
     * @param ram the byte array representing the sector's data to be set
     */
    public void setSectorBytes(byte[] ram){
        this.sector = ram;
    }

    /**
     * Empties the sector by setting all 512 bytes to zero.
     * This method initializes each byte in the sector array to 0x00,
     * effectively clearing any existing data in the sector.
     */
    public void emptyTheSector(){
        for(int i=0;i<512;i++){// Nous allons initialiser tout le secteur avec du vide
            sector[0x00+i] = 0x00;
        }
    }

    public int getLba() {
        return Lba;
    }

    public void setLba(int lba) {
        Lba = lba;
    }
}
