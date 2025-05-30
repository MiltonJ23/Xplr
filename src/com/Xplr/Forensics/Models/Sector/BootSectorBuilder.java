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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BootSectorBuilder {
    BootSector bootSector ;


   public BootSectorBuilder(long byt,String disk_name){
        bootSector = new BootSector(byt,disk_name);
    }

    public void PrintBootSectorBinaryFile(String path) throws FileNotFoundException {
       // Let's access the data of the Boot Sector
        byte[] SectorData = this.bootSector.getBootSectorBytes();
        String CompletePath = path+"/BootSector.bin";
        try(FileOutputStream fos = new FileOutputStream(path)){
            fos.write(SectorData);
            System.out.println("Le binaire du BootSector est a l'emplacement "+path+"/BootSector.bin");
        }catch(IOException e){
            System.err.println("An error occured when we tried to write the BootSector Data"+e.getMessage());
        }
    }

    public BootSector Build(){
       return this.bootSector;
    }

}
