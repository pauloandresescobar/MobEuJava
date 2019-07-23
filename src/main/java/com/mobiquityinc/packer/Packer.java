package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.manager.PackerManagerImpl;

public class Packer {


  public static String pack(String filePath) throws APIException {
    PackerManagerImpl packerManager = new PackerManagerImpl();
    return packerManager.processPackageFile(filePath);
  }
}
