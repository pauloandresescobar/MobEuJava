package com.mobiquityinc.manager;

import com.mobiquityinc.model.ItemModel;
import com.mobiquityinc.model.PackageModel;
import com.mobiquityinc.exception.APIException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.List;

public interface PackerManager {
     String getSelectedPackageItems(PackageModel packageModel);
     List<PackageModel> getAllValidPackages(String filePath) throws APIException;
     BufferedReader loadFile(String filePath) throws FileNotFoundException;
     String processPackageFile(String filePAth) throws APIException;
     List<PackageModel> getItemsFromInput(List<String> inputs) throws APIException;
     ItemModel searchLowerWeigthWithSamePrice(List<ItemModel> items, Double weight, Integer price);
}
