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

public class FATEntry {
    //this class represents a single FATentry . An entry of 32 bits(4 bytes ) with 28 bits are used for cluster addressing

    private final int FREE_CLUSTER =  0x00000000;
    private final int BAD_CLUSTER =  0x0FFFFFF7;
    private final int END_OF_CHAIN_MIN = 0x0FFFFFF8;
    private final int END_OF_CHAIN_MAX = 0x0FFFFFFFF;
    private final int RESERVED_MASK =  0xF0000000;




    private int FatEntryValue;

    public FATEntry(){

    }

    public Boolean isFREE(){
        return this.FatEntryValue == FREE_CLUSTER;
    }
    public Boolean isBAD(){
        return this.FatEntryValue == BAD_CLUSTER;
    }
    public Boolean isEOCMIN(){
        return this.FatEntryValue == END_OF_CHAIN_MIN;
    }
    public Boolean isEOCMAX(){
        return this.FatEntryValue == END_OF_CHAIN_MAX;
    }

    public void setFREE_CLUSTER(){
        this.FatEntryValue = FREE_CLUSTER;
    }
    public void setBAD_CLUSTER(){
        this.FatEntryValue = BAD_CLUSTER;
    }
    public void setEND_OF_CHAIN_MIN(){
        this.FatEntryValue = END_OF_CHAIN_MIN;
    }
    public void setEND_OF_CHAIN_MAX(){
        this.FatEntryValue = END_OF_CHAIN_MAX;
    }










    public int getFatEntryValue() {
        return FatEntryValue;
    }

    public void setFatEntryValue(int fatEntryValue) {
        FatEntryValue = fatEntryValue;
    }
}
