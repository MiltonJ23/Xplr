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

/**
 * The {@code ClusterBuilder} class is responsible for constructing and managing {@link Cluster} objects.
 * It provides methods to create clusters with different configurations, allocate sectors to a cluster,
 * check cluster occupancy, and retrieve cluster information.
 *
 * <p>
 * This class offers multiple constructors to initialize a cluster with or without specific parameters
 * such as position and initial sector allocation. It also includes methods to mark a cluster as free,
 * determine if a cluster is occupied, assign sectors to a cluster, and retrieve the cluster's ID.
 * </p>
 *
 * <p>
 * The primary goal of this class is to encapsulate the creation and modification of {@link Cluster} objects,
 * providing a clear and controlled interface for interacting with cluster instances.
 * </p>
 */
public class ClusterBuilder {

    //Let's cover litteraly our only cluster in the system
    /**
     * The cluster object being built.
     */
    private Cluster cluster ;


    // Let's define our Different Cluster Builder
    /**
        * Constructs a new ClusterBuilder instance.
        * Initializes the internal cluster object.
        */
    public ClusterBuilder(){
        this.cluster = new Cluster();
    }
    /**
     * Constructs a new ClusterBuilder with the specified position.
     *
     * @param pos The position of the cluster to be built. This position is used to initialize the cluster's position.
     */
    public ClusterBuilder(int pos){
        this.cluster = new Cluster(pos);
    }
    /**
        * Constructs a ClusterBuilder with the specified position and texture indices.
        *
        * @param pos The position of the cluster.
        * @param tex An ArrayList of integer texture indices associated with the cluster.
        */
    public ClusterBuilder(int pos, ArrayList<Integer> tex){
        this.cluster = new Cluster(pos,tex);
    }

    /**
        * Marks the cluster as free (not occupied).
        */
    public void FreeSpace(){
        this.cluster.setOccupied(false);
    }

    /**
        * Checks if the cluster is currently occupied.
        *
        * @return {@code true} if the cluster is occupied, {@code false} otherwise.
        */
    public Boolean isOccupied(){
        return cluster.isOccupied();
    }

    /**
        * Sets the sectors LBAs held by the cluster.
        *
        * @param tesla An ArrayList of Integers representing the sector LBAs.
        */
    public void SectorAllocation(ArrayList<Integer> tesla ){
        this.cluster.setSectorsLbasHoldByCluster(tesla);
    }
    /**
     * Retrieves the ID of the cluster.
     *
     * @return The ID of the cluster.
     */
    public int getCluster(){
        return this.cluster.getCluster_Id();
    }

    public Cluster Build(){
        return this.cluster;    }



}
