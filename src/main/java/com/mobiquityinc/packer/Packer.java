package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.manager.PackerManagerImpl;

/**
 * Author: Paulo Escobar
 */
public class Packer {

  /**
   *
   * @param filePath is the file to read and process
   * @return a string with the selected packages to send separated by a coma
   * @throws APIException if:
   * 1. Max weight that a package can take is ≤ 100
   * 2. There might be up to 15 items you need to choose from
   * 3. Max weight and cost of an item is ≤ 100
   */
  public static String pack(String filePath) throws APIException {
    PackerManagerImpl packerManager = new PackerManagerImpl();
    return packerManager.processPackageFile(filePath);
  }
}
