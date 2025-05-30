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
package com.Xplr.Forensics.Models.VirtualDisk;

import java.io.FileInputStream;
import java.io.IOException;

public class VirtualDiskBuilder {
    private VirtualDisk virtualDisk;




    /**
     * Constructs a new VirtualDiskBuilder instance.
     * Initializes the internal virtual disk object.
     */
    public VirtualDiskBuilder(String filename ) {
        this.virtualDisk = new VirtualDisk(filename);
    }


        /**
     * Loads a virtual disk image from an image file into the VirtualDisk object
     * managed by this builder.
     *
     * @param imageFilePath The path to the image file to load.
     * @return This VirtualDiskBuilder instance, for chaining.
     * @throws IOException If an I/O error occurs during file reading.
     */
    public VirtualDiskBuilder loadFromImageFile(String imageFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(imageFilePath)) {
            // Good lord , it should read the entire file into a byte array , no jokes  , this method is a nemesis , don't know if it will works or not 🙏🏾
            long fileSize = new java.io.File(imageFilePath).length();

            // here we read the entire image file into a byte array
            byte[] content = new byte[(int) fileSize];
            fis.read(content);

            // then we set the content of the VirtualDisk
            this.virtualDisk.fromByteArray(content);

            return this;
        } catch (IOException e) {
            System.err.println("Error loading virtual disk image: " + e.getMessage());
            throw e; // Re-throw the exception to be handled by the caller
        }
    }




     public VirtualDisk build() {
        return this.virtualDisk;
    }

    public void Build(String imagePath) {
        try {
            this.virtualDisk.writeToImageFile(imagePath);
        } catch (Exception e) {
            e.printStackTrace();        }
    }
}
