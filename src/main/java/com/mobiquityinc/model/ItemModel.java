package com.mobiquityinc.model;

/*
* This class is for storing the package information
* Author: Paulo Escobar
* */
public class ItemModel {



    private String id;
    private Double weight;
    private Integer price;

    public ItemModel(String id, Double weight, Integer price){
        this.id = id;
        this.price = price;
        this.weight = weight;
    }
    public ItemModel(){
    }
    /**
     * Get the package id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the package id
     * @param id: is the param that sets the id for the item
     */

    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return weigth
     */
    public Double getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     *
     * @return price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     *
     * @param price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }
}
