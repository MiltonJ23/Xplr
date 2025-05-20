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
package com.Xplr.Forensics.Models.Cluster;

import java.util.ArrayList;

public class ClusterBuilder {

    //Let's cover litteraly our only cluster in the system
    private Cluster cluster ;


    // Let's define our Different Cluster Builder
    public ClusterBuilder(){
        this.cluster = new Cluster();
    }
    public ClusterBuilder(int pos){
        this.cluster = new Cluster(pos);
    }
    public ClusterBuilder(int pos, ArrayList<Integer> tex){
        this.cluster = new Cluster(pos,tex);
    }

    public void FreeSpace(){
        this.cluster.setOccupied(false);
    }

    public Boolean isOccupied(){
        return cluster.isOccupied();
    }

    public void SectorAllocation(ArrayList<Integer> tesla ){
        this.cluster.setSectorsLbasHoldByCluster(tesla);
    }
    public int getCluster(){
        return this.cluster.getCluster_Id();
    }



}
