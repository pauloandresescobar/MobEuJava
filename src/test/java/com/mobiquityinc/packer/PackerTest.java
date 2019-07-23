package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.Assert;
import org.junit.Test;
public class PackerTest {
    private static final String CORRECT_FILE_PATH ="src/main/resources/InputSample.txt";




    @Test
    public void pack() throws APIException {
        Assert.assertEquals(Packer.pack(CORRECT_FILE_PATH),getValidOutputDummy());
    }

    private String getValidOutputDummy(){
        return "4\n-\n2,7\n8,9\n";
    }
}

