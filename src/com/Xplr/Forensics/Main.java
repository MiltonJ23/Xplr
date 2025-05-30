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

import com.Xplr.Forensics.Models.VirtualDisk.VirtualDisk;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String diskName = "MyDisk";
        long diskSize = 1024 * 1024 * 1024; // 1GB
        String imageFilePath = "/Users/fredmike/Desktop/mydisk.dmg"; 

        try {
            //  Create a VirtualDisk object
            VirtualDisk virtualDisk = new VirtualDisk(diskName, diskSize);

            //. Write the virtual disk to an image file
            virtualDisk.writeToImageFile(imageFilePath);

            System.out.println("Virtual disk image created successfully at: " + imageFilePath);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
