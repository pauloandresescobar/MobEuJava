package com.mobicuityinc.manager;

import com.mobiquityinc.model.ItemModel;
import com.mobiquityinc.model.PackageModel;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.manager.PackerManagerImpl;
import com.mobiquityinc.util.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.List;

public class PackerManagerImplTest {

    @Mock
    ItemModel itemModel;

    @Mock
    PackageModel packageModel;

    @Spy
    PackerManagerImpl packer;


    private PackerManagerImpl packerManager = new PackerManagerImpl();

    private static final String WRONG_FILE_PATH ="src/fakedir/00";
    private static final String CORRECT_FILE_PATH ="src/main/resources/InputSample.txt";
    private static final String EXCEDED_ITEM_LIMIT_FILE_PATH ="src/test/java/test_files/ExcededPackageItemLimits.txt";
    private static final String EXACT_NUMBER_OF_ITEMS_FILE_PATH ="src/test/java/test_files/ExactNumberOfItems.txt";
    private static final String EXEDED_PRICE_FILE_PATH ="src/test/java/test_files/ExcededPrice.txt";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadFileTest() throws FileNotFoundException {

        BufferedReader bufferedReader = packer.loadFile(CORRECT_FILE_PATH);
        Assert.assertNotNull(bufferedReader);
    }

    /*
     * this test wil verify if the exception throwed is FileNotFoundException
     * */
    @Test(expected = FileNotFoundException.class)
    public void loadFileFailTest() throws FileNotFoundException {
        BufferedReader bufferedReader = packer.loadFile(WRONG_FILE_PATH);
    }

    /*
    * this test wil verify if the APIException is the one that
    * brings the exceded price message
    * */
    @Test
    public void getItemsFromInputTestWhenExcededPrice()  {
        String message = "";
        try {
            String thingsToSend = packerManager.processPackageFile(EXEDED_PRICE_FILE_PATH);
        } catch (APIException e) {
            message = e.getMessage();
        }
        Assert.assertEquals(message, Constants.PACKAGE_ITEM_PRICE_EXCEDED_MESSAGE);
    }


    @Test
    public void getAllvalidPackagesTest() throws APIException, FileNotFoundException {
        packer.getAllValidPackages(CORRECT_FILE_PATH);
        Mockito.verify(packer,Mockito.times(1)).loadFile(CORRECT_FILE_PATH);

    }

    @Test(expected = APIException.class)
    public void getThingsToSendWhenExcededpackageLimit() throws APIException {
        List<PackageModel> thingsToSend = packerManager.getAllValidPackages(EXCEDED_ITEM_LIMIT_FILE_PATH);
    }

    @Test
    public void processPackageFileTest() throws APIException {

        String thingsToSend = packerManager.processPackageFile(CORRECT_FILE_PATH);
        Assert.assertNotNull(thingsToSend);
    }


    @Test
    public void processPackageFileWhenNoMatchesTest() throws APIException {
        String thingsToSend = packerManager.processPackageFile(EXACT_NUMBER_OF_ITEMS_FILE_PATH);
        Assert.assertEquals(thingsToSend,"-\n");
    }

}
