package com.mobicuityinc.manager;
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
    List stringList;

    @Spy
    PackerManagerImpl packer;


    private PackerManagerImpl packerManager = new PackerManagerImpl();

    private static final String WRONG_FILE_PATH ="src/fakedir/00";
    private static final String CORRECT_FILE_PATH ="src/main/resources/InputSample.txt";
    private static final String EXCEDED_ITEM_LIMIT_FILE_PATH ="src/test/java/test_files/ExcededPackageItemLimits.txt";
    private static final String EXACT_NUMBER_OF_ITEMS_FILE_PATH ="src/test/java/test_files/ExactNumberOfItems.txt";
    private static final String EXEDED_PRICE_FILE_PATH ="src/test/java/test_files/ExcededPrice.txt";
    private static final String LARGE_FILE ="src/test/java/test_files/LargeFileWTestFile.txt";
    private static final String SPACE_BETWEEN_LINES_INPUT_FILE ="src/test/java/test_files/LargeFileWTestFileWithSpaces.txt";
    private String message = "";
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    /*
    * Test to verify that the file was found with a correct path
    * */
    @Test
    public void loadFileTest() throws FileNotFoundException {

        BufferedReader bufferedReader = packer.loadFile(CORRECT_FILE_PATH);
        Assert.assertNotNull(bufferedReader);
    }

    /*
     * this test wil verify if the exception throwed is FileNotFoundException with the wrong file path
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
        message = "";
        try {
            packerManager.processPackageFile(EXEDED_PRICE_FILE_PATH);
        } catch (APIException e) {
            message = e.getMessage();
        }
        Assert.assertEquals(message, Constants.PACKAGE_ITEM_PRICE_EXCEDED_MESSAGE);
    }


    /*
     * this test wil verify if the APIException is the same that
     * brings package limit of items exceded message
     * */
    @Test
    public void getAllValidPackagesWhenExcededpackageLimit() {
        message = "";
        try {
            packerManager.getAllValidPackages(EXCEDED_ITEM_LIMIT_FILE_PATH);
        } catch (APIException e) {
            message = e.getMessage();
        }
        Assert.assertEquals(message, Constants.PACKAGE_ITEMS_LIMIT_EXCEDED_MESSAGE);
    }


    @Test
    public void processPackageFileTest() throws APIException {
        String output = packerManager.processPackageFile(CORRECT_FILE_PATH);
        Assert.assertNotNull(output);
    }


    @Test
    public void processPackageFileLargeFileTest() throws APIException {
        String output = packerManager.processPackageFile(LARGE_FILE);
        Assert.assertNotNull(output);

    }

    @Test
    public void processPackageFileWithLetters(){
        message="";
        try{
            packerManager.processPackageFile("src/test/java/test_files/FileWithLeters.txt");
        } catch(Exception e){
           message=e.getMessage();
        }
        Assert.assertEquals(message,Constants.NUMBER_EXCEPTION_MESSAGE);

    }

    /*
    * This test is to make sure that the spaces between lines that are inside the file dont
    * damage the output
    * */
    @Test
    public void processPackageFileWithSpacesTest() throws APIException {
        String output = packerManager.processPackageFile(SPACE_BETWEEN_LINES_INPUT_FILE);
        Assert.assertNotNull(output);
    }

    /*
    * Check for duplicated id
    * the exception message should be DUPLICATED_ID_MESSAGE
    * */
    @Test
    public void processPackageWhenDuplicatedIdTest()  {
        message ="";
        try {
            packerManager.processPackageFile("src/test/java/test_files/DuplicatedIdTestFile.txt");
        }catch (Exception e){
            message= e.getMessage();
        }
            Assert.assertEquals(message,Constants.DUPLICATED_ID_MESSAGE);
    }

    /*
    * This test is to check if we return "-" when
    * there is no items selected
    * */
    @Test
    public void processPackageFileWhenNoMatchesTest() throws APIException {
        String output = packerManager.processPackageFile(EXACT_NUMBER_OF_ITEMS_FILE_PATH);
        Assert.assertEquals(output,"-\n");
    }

    /*
     * Test the getItemsFromInput method if
     * the param is empty should return empty but not null
     * */
    @Test
    public void getItemsFromInputWhenInputEmpty() throws APIException {
        Assert.assertNotNull(packerManager.getItemsFromInput(stringList));
    }

    /*
     * Test the getItemsFromInput method if
     * it has more than 15 objects then the message of the exception shuold be
     * PACKAGE_ITEMS_LIMIT_EXCEDED_MESSAGE
     * */
    @Test
    public void getItemsFromInputWhenInputItemsExceded(){
        message = "";
        Mockito.when(stringList.get(0)).thenReturn("8 : (1,15.3,€34) (2,53.38,€45) (3,88.62,€98) " +
                "(4,78.48,€3) (5,72.30,€76) (6,30.18,€9) (7,46.34,€48) (8,85.31,€29) (9,14.55,€74) (10,3.98,€16) " +
                "(11,26.24,€55) (12,63.69,€52) (13,76.25,€75) (14,60.02,€74) (15,93.18,€35) (16,89.95,€78)");
        Mockito.when(stringList.size()).thenReturn(1);
        try{
            packerManager.getItemsFromInput(stringList);

        } catch(Exception e){
            message=e.getMessage();
        }
        Assert.assertEquals(Constants.PACKAGE_ITEMS_LIMIT_EXCEDED_MESSAGE,message);
    }


    @Test
    public void getItemsFromInputWhenExactInputItems() throws APIException{
        message = "";
        Mockito.when(stringList.get(0)).thenReturn("8 : (1,15.3,€34) (2,53.38,€45) (3,88.62,€98) " +
                "(4,78.48,€3) (5,72.30,€76) (6,30.18,€9) (7,46.34,€48) (8,85.31,€29) (9,14.55,€74) (10,3.98,€16) " +
                "(11,26.24,€55) (12,63.69,€52) (13,76.25,€75) (14,60.02,€74) (15,93.18,€35)");
        Mockito.when(stringList.size()).thenReturn(1);
        Assert.assertNotNull(packerManager.getItemsFromInput(stringList));

    }

    /*
    * Test the getItemsFromInput method if
    * the param is null should return null
    * */
    @Test
    public void getItemsFromInputWhenInputNull() throws Exception {
        Assert.assertNull(packerManager.getItemsFromInput(null));
    }

}
