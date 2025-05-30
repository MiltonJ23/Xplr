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
package com.Xplr.Forensics;

import com.Xplr.Forensics.Models.Sector.BootSector;
import java.io.FileOutputStream;
import java.io.IOException;


public class ExportBootSector {

    public static void main(String[] args) {
        BootSector bootSector = new BootSector(4294967296L,"Anakin");

        // Access the private BootSector array using a getter (add this method if not present)
        byte[] sectorData = bootSector.getBootSectorBytes();

        try (FileOutputStream fos = new FileOutputStream("/Users/fredmike/Downloads/bootsector.bin")) {
            fos.write(sectorData);
            System.out.println("Boot sector written to bootsector.bin");
        } catch (IOException e) {
            System.err.println("Error writing boot sector: " + e.getMessage());
        }
    }
}
