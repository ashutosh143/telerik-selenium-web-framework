package com.telerik.tests;

import com.telerik.basetest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BlogsTest extends BaseTest {

    @BeforeMethod
    public void setupPage() {
        homePage.openBlogsPage();
    }

    @Test(groups = {"sanity", "regression"})
    public void isLogoPresentTest() {
        Assert.assertTrue(blogsPage.verifyLogo(), "Log is not present");
    }

    @Test(groups = {"sanity"})
    public void blogsPageTitleTest() {
        String titleOfPage = blogsPage.getTitle();
        Assert.assertEquals(titleOfPage, "Your Source for .NET & JavaScript Developer Info – Telerik Blogs" , " ERROR - Title is not matching. Actual:  " + blogsPage.getTitle());
    }

    @Test(groups = {"regression"})
    public void blogsPageTestA() {
        System.out.println("This is blogsPageTestA function");
    }

    @Test(groups = {"sanity", "regression"})
    public void blogsPageTestB() {
        System.out.println("This is blogsPageTestB function");
    }

}

