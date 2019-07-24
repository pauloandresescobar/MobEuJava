package com.mobiquityinc.manager;

import com.mobiquityinc.model.PackageModel;
import com.mobiquityinc.model.ItemModel;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.util.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/*
 * This class process the package information
 * Author: Paulo Escobar
 * */
public class PackerManagerImpl implements PackerManager {


    private static final Logger LOGGER = Logger.getLogger("com.mobiquityinc.manager.PackerManagerImpl");

    /**
     * This method is to load the file to process
     * @param filePath is the path to the file that we are trying to process
     * @return new BufferedReader that contains the content of the file
     * @throws FileNotFoundException if the file is not found
     */
    @Override
    public BufferedReader loadFile(String filePath) throws FileNotFoundException{
        File file = new File(filePath);
        return new BufferedReader(new FileReader(file));
    }

    @Override
    public String processPackageFile(String filePath) throws APIException {
        String output = "";
        List<PackageModel> packageModelList = getAllValidPackages(filePath);
        if(packageModelList !=null){
            for (int i = 0; i < packageModelList.size(); i++) {
                output = output + getSelectedPackageItems(packageModelList.get(i))+"\n";
            }
            return output;
        }
        return null;
    }

    /**
     * @param packageModel is the package with the valid items that we want to proces
     * @return String with the ids of the items that where selected for
     * packing separated by a coma.
     */
    @Override
    public String getSelectedPackageItems(PackageModel packageModel) throws APIException {
        try {
            Double limitPackageWeigth = packageModel.getLimit();
            String packages = null;
            if (packageModel.getPackedItems().size() == 0) {
                return "-";
            }
            packageModel.getPackedItems().sort(Comparator.comparing(ItemModel::getPrice).reversed());
            Integer higestPrice = packageModel.getPackedItems().get(0).getPrice();
            for (int i = 0; i < packageModel.getPackedItems().size(); i++) {
                Integer price = packageModel.getPackedItems().get(i).getPrice();
                Double weight = packageModel.getPackedItems().get(i).getWeight();
                String id = packageModel.getPackedItems().get(i).getId();


                if (!isUniqueId(id, packageModel.getPackedItems())) {
                    throw new APIException(Constants.DUPLICATED_ID_MESSAGE);
                }


                if (higestPrice == price && weight <= limitPackageWeigth) {
                    ItemModel itemModel = searchLowerWeigthWithSamePrice(packageModel.getPackedItems(), weight, price);
                    if (itemModel == null) {
                        if (packages != null && !packages.contains(id)) {
                            packages = packages + "," + id;
                        } else {
                            packages = id;
                        }
                        limitPackageWeigth = limitPackageWeigth - weight;
                    } else {
                        if (packages == null) {
                            packages = itemModel.getId();
                            limitPackageWeigth = limitPackageWeigth - itemModel.getWeight();
                        } else if (!packages.contains(itemModel.getId())) {
                            packages = itemModel.getId();
                            limitPackageWeigth = limitPackageWeigth - itemModel.getWeight();
                        } else {
                            packages = packages + "," + id;
                            limitPackageWeigth = limitPackageWeigth - weight;
                        }
                    }
                } else if (packages != null && limitPackageWeigth - weight > 0 && !packages.contains(id)) {
                    packages = packages + "," + id;
                    limitPackageWeigth = limitPackageWeigth - weight;
                    higestPrice = price;
                }
            }
            return packages;
        } catch(Exception e) {
            throw e;
        }
    }

    /**
     * This method compares if there are more items with the same price and lower
     * @param items is the list of items to compare
     * @param weight param weigth is to compare with all the items
     * @param price param to compare with all the items
     * @return itemModel with the items that match the conditions
     */
    @Override
    public ItemModel searchLowerWeigthWithSamePrice(List<ItemModel> items, Double weight, Integer price){
        List<ItemModel> itemModel = items.stream().filter(item -> item.getWeight() < weight && item.getPrice()==price).collect(Collectors.toList());
        if(!itemModel.isEmpty()){
            itemModel.sort(Comparator.comparing(ItemModel::getWeight));
            return itemModel.get(0);
        } else {
            return null;
        }
    }


    private boolean isUniqueId(String id , List<ItemModel> items){
        long repeatedIds = items.stream().filter(item -> item.getId().contentEquals(id)).count();
        if( repeatedIds >1){
            return false;
        }

        return true;
    }

    /**
     * this is a method to return all valid packages with its respective list of items to carry
     * without been filtered
     * @param filePath file path of the file we are processing
     * @return List<PackageModel>
     */
    @Override
    public List<PackageModel> getAllValidPackages(String filePath) throws APIException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = loadFile(filePath);
            List<String> inputs = bufferedReader.lines().collect(Collectors.toList());
            return getItemsFromInput(inputs);
        } catch (FileNotFoundException e) {
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

    @Override
    public List<PackageModel> getItemsFromInput(List<String> inputs) throws APIException{
        try {
            List<PackageModel> packages = new ArrayList<>();
            int linesToRead = inputs.size();
            for (int i = 0; i < linesToRead; i++) {
                PackageModel packageModel = new PackageModel();
                String[] rawData = inputs.get(i).split(":");
                if (rawData.length >1){
                    String[] items = rawData[1].split("\\(");

                    Double packageLimit = Double.parseDouble(rawData[0].replaceAll(" ", ""));
                    List<ItemModel> thingsToSend = new ArrayList<>();

                    if (items.length -1 > Constants.MAX_ITEMS_AMOUNT ) {
                        throw new APIException(Constants.PACKAGE_ITEMS_LIMIT_EXCEDED_MESSAGE);
                    }

                    if (packageLimit > Constants.MAX_PACKAGE_WEIGHT) {
                        throw new APIException(Constants.PACKAGE_LIMIT_WEIGHT_EXCEDED_MESSAGE);
                    }

                    if (packageLimit <= Constants.MAX_PACKAGE_WEIGHT ) {

                        for (int j = 1; j < items.length; j++) {

                            String[] thingDetails = items[j].split(",");
                            String index = thingDetails[0];
                            double weight = Double.parseDouble(thingDetails[1]);

                            if(weight>Constants.MAX_ITEM_WEIGHT){
                                throw new APIException(Constants.PACKAGE_ITEM_WEIGHT_LIMIT_EXCEDED_MESSAGE);
                            }

                            String priceText = thingDetails[2].replaceAll("\\D+", "");
                            Integer price = Integer.parseInt(priceText);

                            if(price>Constants.MAX_ITEM_COST){
                                throw new APIException(Constants.PACKAGE_ITEM_PRICE_EXCEDED_MESSAGE);
                            }

                            if (weight < packageLimit) {
                                ItemModel thing = new ItemModel(index, weight, price);
                                thingsToSend.add(thing);
                            }
                        }
                        packageModel.setPackedItems(thingsToSend);
                        packageModel.setLimit(packageLimit);
                        packages.add(packageModel);

                    }
                }
            }

            return packages;

        } catch (APIException e) {
            throw e;

        } catch(NumberFormatException e){
            throw new APIException(Constants.NUMBER_EXCEPTION_MESSAGE);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

        return null;
    }


}
