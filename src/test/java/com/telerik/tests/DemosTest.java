package com.telerik.tests;

import com.telerik.basetest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DemosTest extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setupPage() {
        homePage.openDemosPage();
    }

    @Test(groups = {"sanity"})
    public void demosPageTitleTest() {
        String titleOfPage = demosPage.getTitle();
        System.out.println(titleOfPage);
        Assert.assertEquals(titleOfPage, "Telerik Product Demos, Examples and Tutorials for all Telerik products" , " ERROR - Title is not matching. Actual:  " + demosPage.getTitle());
    }

    @Test(groups = {"sanity", "regression"})
    public void demosTestA() {
        System.out.println("This is demosTestA function");
    }

    @Test(groups = {"regression"})
    public void demosTestB() {
        System.out.println("This is demosTestB function");
    }
}
