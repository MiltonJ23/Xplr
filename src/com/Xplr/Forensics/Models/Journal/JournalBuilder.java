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

/**
 * The {@code JournalBuilder} class is responsible for constructing {@link Journal} objects.
 * It provides methods to initialize and manipulate the root directory of the journal.
 * This class follows the Builder design pattern to facilitate the creation of {@link Journal} instances.
 */
public class JournalBuilder {
    
    // This object is the one supposed to build the journal object 



    /**
        * The root directory of the journal, serving as the starting point for navigating the journal's structure.
        */
    private Journal rootDirectory;



    /**
        * Constructs a new JournalBuilder, initializing the root directory of the journal.
        */
    public JournalBuilder(){
        this.rootDirectory = new Journal();
       
    }

    /**
     * Retrieves the root directory of the journal.
     *
     * @return The root directory of the journal.
     */
    public Journal getRootDirectory() {
        return rootDirectory;
    }

    /**
        * Sets the root directory for the journal.
        *
        * @param rootDirectory The root directory to be set for the journal.
        */
    public void setRootDirectory(Journal rootDirectory) {
        this.rootDirectory = rootDirectory;
    }
}
