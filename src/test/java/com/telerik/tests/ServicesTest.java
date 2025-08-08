package com.telerik.tests;

import com.telerik.basetest.BaseTest;
import com.telerik.listeners.MyRetryAnalyzer;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import io.restassured.specification.RequestSpecification;

public class ServicesTest  extends BaseTest {
    private static final Logger logger = LogManager.getLogger(ServicesTest.class);


    @BeforeMethod(alwaysRun = true)
    public void setupPage() {
        homePage.openServicesPage();
    }


    @Test(retryAnalyzer = MyRetryAnalyzer.class, groups = {"sanity"})
    //@Test(groups = {"sanity"})
    public void isTalkToAConsultantButtonPresentTest() {
        Assert.assertTrue(servicesPage.validateIfContinueButtonIsDisplayed(), "Talk To A Consultant button is not displaying in Services page");
    }

    @Test(groups = {"sanity", "regression"})
    public void serviceApiCall() {
        logger.info("This is serviceApiCall function");
        ApiMethodsTests apiMethodsTests = new ApiMethodsTests();
        apiMethodsTests.getRequest();
        logger.info("Duplicate call to getRequest");
        apiMethodsTests.getRequest();
        apiMethodsTests.postRequestTest();

    }

    @Test(groups = {"regression"})
    public void serviceTestB() {
        logger.info("This is serviceTestB function");
        ApiMethodsTests apiMethodsTests = new ApiMethodsTests();
        apiMethodsTests.postRequestTest();
        //apiMethodsTests.postRequestTest();
    }


    @Test(groups = {"sanity"})
    public void fieldsOnThePageTest() throws IOException {
        List<String> fieldsName = excelReader.getFieldNamesFromExcel("ServicesPage");
        Assert.assertTrue(servicesPage.verifyPresenceOfElementsOnPage(fieldsName),"Some elements are not present on the service page. ");
    }

}
