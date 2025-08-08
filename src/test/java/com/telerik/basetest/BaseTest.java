package com.telerik.basetest;

import com.telerik.base.BasePage;
import com.telerik.pages.*;
import com.telerik.reports.ExtentReportManager;
import com.telerik.testdatareaders.ExcelReader;
import com.telerik.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;

@Listeners(com.telerik.listeners.MyListener.class)
public class BaseTest {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    private static boolean isSuiteNameSet = false;

    public BasePage basePage;
    public BlogsPage blogsPage;
    public DemosPage demosPage;
    public DocAndSupportPage docAndSupportPage;
    public PricingPage pricingPage;
    public ServicesPage servicesPage;
    public HomePage homePage;
    public ExcelReader excelReader;

    public static WebDriver getDriver() {
        return driver.get();
    }
    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite(){
        logger.info("<<<< Setting Up all the configs before Run >>>> ");
        ExtentReportManager.setupExtentReport();
    }

    @BeforeTest(alwaysRun = true)
    public void setUpTest(ITestContext iTestContext){
        if (!isSuiteNameSet) {
            String[] groupsList = iTestContext.getIncludedGroups();
            String includedGroupList = String.join(",", groupsList);
            String suiteName = iTestContext.getSuite().getName();
            logger.info("Starting the execution for TestSuite ==> {} with Tags {}", suiteName, includedGroupList);
            ExtentReportManager.getReport().setSystemInfo("Suite Name", iTestContext.getSuite().getName());
            isSuiteNameSet = true;
        }
        String testName = iTestContext.getName();
        logger.info("Starting the execution for Test ==> {}", testName);
        ExtentReportManager.getReport().setSystemInfo("Test Name", testName);

    }

    @BeforeMethod(alwaysRun = true)
    public void baseTestSetup() {
        basePage = new BasePage(getDriver()); // optional if not used here

        WebDriver driverInstance = basePage.getDriver(ConfigReader.getConfigReader().getBrowser());

        //Get the TestDataExcel
        File testDataFile = new File("src/test/resources/TestData", ConfigReader.getConfigReader().getTestDataWorkbook());
        excelReader = new ExcelReader(testDataFile.getPath());

        setDriver(driverInstance);
        getDriver().get(ConfigReader.getConfigReader().getUrL());

        homePage = new HomePage(getDriver());
        blogsPage = new BlogsPage(getDriver());
        demosPage = new DemosPage(getDriver());
        docAndSupportPage = new DocAndSupportPage(getDriver());
        pricingPage = new PricingPage(getDriver());
        servicesPage = new ServicesPage(getDriver());
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        logger.info("Quitting the driver =====> {}" , ConfigReader.getConfigReader().getBrowser());
        getDriver().quit();
        driver.remove();  // Clean up
    }

    @AfterTest(alwaysRun = true)
    public void closingUpTest(ITestContext iTestContext){
        String testName = iTestContext.getName();
        logger.info("Ending the execution for Test ==> {}. \n\n", testName);
    }

    @AfterSuite(alwaysRun = true)
    public void closeUpSuite(){
        logger.info("<<<< Closing Up all the configs after Run >>>> ");
        ExtentReportManager.flushReport();
    }
}
