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
 * Represents a cluster in a forensic context, typically used to group sectors on a storage device.
 * Each cluster has a unique identifier, a list of sector LBAs (Logical Block Addresses) it holds,
 * and an occupancy status indicating whether it is currently in use.
 *
 * <p>
 * Fields:
 * <ul>
 *   <li><b>Cluster_Id</b>: Unique identifier for the cluster.</li>
 *   <li><b>SectorsLbasHoldByCluster</b>: List of sector LBAs associated with this cluster.</li>
 *   <li><b>Occupied</b>: Boolean flag indicating if the cluster is occupied.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Constructors:
 * <ul>
 *   <li>{@code Cluster(int id, ArrayList<Integer> liste)}: Initializes a cluster with a given ID and list of sector LBAs, sets occupied to false.</li>
 *   <li>{@code Cluster(int cluster_Id)}: Initializes a cluster with a given ID.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Getter and Setter methods are provided for all fields.
 * </p>
 */
public class Cluster {

    // Now let's configure the fields

    /**
     * Unique identifier for the cluster.
     */
    private int Cluster_Id;


    /**
     * Holds the list of sector LBAs (Logical Block Addresses) associated with this cluster.
     * Each integer in the list represents the LBA of a sector that is part of the cluster.
     */
    private ArrayList<Integer> SectorsLbasHoldByCluster;
    
    
    /**
     * Indicates whether the cluster is currently occupied.
     * A value of {@code true} means the cluster is in use,
     * while {@code false} means it is available.
     */
    private Boolean Occupied;

// After the fields configuration , let's define the constructors

    /**
     * Constructs a new Cluster with the specified ID and list of sector LBAs.
     *
     * @param id the unique identifier for the cluster
     * @param liste the list of sector LBAs (Logical Block Addresses) held by this cluster
     */
    public Cluster(int id, ArrayList<Integer> liste){
            this.Cluster_Id=id;
            this.SectorsLbasHoldByCluster = liste;
            this.Occupied=false;
    }

    /**
     * Constructs a new Cluster with the specified cluster ID.
     *
     * @param cluster_Id the unique identifier for the cluster
     */
    public Cluster(int cluster_Id) {
        Cluster_Id = cluster_Id;
    }

    public Cluster(){

    }

    // After defining the constructor , let's define the getter and setter

    /**
     * Returns the unique identifier of the cluster.
     *
     * @return the cluster ID as an integer
     */
    public int getCluster_Id() {
        return Cluster_Id;
    }

    /**
     * Sets the unique identifier for the cluster.
     *
     * @param cluster_Id the integer value to set as the cluster's ID
     */
    public void setCluster_Id(int cluster_Id) {
        Cluster_Id = cluster_Id;
    }

    /**
     * Returns a list of sector LBAs (Logical Block Addresses) held by this cluster.
     *
     * @return an ArrayList of Integer values representing the LBAs of sectors associated with the cluster
     */
    public ArrayList<Integer> getSectorsLbasHoldByCluster() {
        return SectorsLbasHoldByCluster;
    }

    /**
     * Sets the list of sector LBAs (Logical Block Addresses) held by this cluster.
     *
     * @param sectorsLbasHoldByCluster An ArrayList of Integer values representing the LBAs of sectors associated with this cluster.
     */
    public void setSectorsLbasHoldByCluster(ArrayList<Integer> sectorsLbasHoldByCluster) {
        SectorsLbasHoldByCluster = sectorsLbasHoldByCluster;
    }
    /**
     * Returns the occupancy status of the cluster.
     *
     * @return {@code true} if the cluster is occupied; {@code false} otherwise.
     */
    public Boolean isOccupied() {
        return Occupied;
    }

    /**
     * Sets the occupied status of the cluster.
     *
     * @param occupied true if the cluster is occupied; false otherwise
     */
    public void setOccupied(Boolean occupied) {
        Occupied = occupied;
    }


}
