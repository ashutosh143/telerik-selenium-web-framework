package com.telerik.pages;

import com.telerik.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

    private final Actions actions;


    @FindBy(css = ".TK-Menu-Item-Link[href='/support/demos']")
    public WebElement demos;

    @FindBy(css = ".TK-Menu-Item-Link[href='/services']")
    public WebElement services;

    @FindBy(css = ".TK-Menu-Item-Link[href='/blogs']")
    public WebElement blogs;

    @FindBy(css = ".TK-Menu-Item-Link[href='/support']")
    public WebElement docAndSupport;

    @FindBy(xpath = "//a[@class='TK-Menu-Item-Link'][normalize-space()='Pricing']")
    public WebElement pricing;


    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        actions = new Actions(driver);
    }

    public void openBlogsPage() {
        //actions.moveToElement(getTheElementAfterVisible(accountsAndLists)).perform();;
        getTheElementAfterVisible(blogs).click();
    }

    public void openDemosPage() {
        getTheElementAfterVisible(demos).click();
    }

    public void openPricingPage() {
        getTheElementAfterVisible(pricing).click();
    }

    public void openServicesPage() {
        getTheElementAfterVisible(services).click();
        getTheElementAfterVisible(services).click();
    }

    public void openDocAndSupportPage() {
        getTheElementAfterVisible(docAndSupport).click();
        getTheElementAfterVisible(docAndSupport).click();
    }

}
