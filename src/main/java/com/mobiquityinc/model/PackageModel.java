package com.mobiquityinc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the data for the packages
 * Author: Paulo Andres Escobar
 */
public class PackageModel {

    private Double limit;
    private List<ItemModel> packedItems = new ArrayList<>();

    public Double getLimit() {
        return limit;
    }

    /**
     * Set maximun weight the package can take
     * @param limit is a Double
     */
    public void setLimit(Double limit) {
        this.limit = limit;
    }

    /**
     *
     * @return the list of the Packed items
     */
    public List<ItemModel> getPackedItems() {
        return packedItems;
    }

    /**
     *
     * @param packedItems is the list of items that the package will have
     */
    public void setPackedItems(List<ItemModel> packedItems) {
        this.packedItems = packedItems;
    }

}
