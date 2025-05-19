package com.Xplr.Forensics;

import com.Xplr.Forensics.Models.Sector.BootSector;
import java.io.FileOutputStream;
import java.io.IOException;


public class ExportBootSector {

    public static void main(String[] args) {
        BootSector bootSector = new BootSector();

        // Access the private BootSector array using a getter (add this method if not present)
        byte[] sectorData = bootSector.getBootSectorBytes();

        try (FileOutputStream fos = new FileOutputStream("/Users/fredmike/Desktop/ICTUniversity/Spring2025/Network:Computer\\ Forensics/Projects/Xplr/src/com/Xplr/Forensics/bootsector.bin")) {
            fos.write(sectorData);
            System.out.println("Boot sector written to bootsector.bin");
        } catch (IOException e) {
            System.err.println("Error writing boot sector: " + e.getMessage());
        }
    }
}
