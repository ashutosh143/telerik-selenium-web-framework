package com.telerik.utils;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CustomWait {
    protected WebDriver driver;
    private final WebDriverWait wait;
    private boolean elementState;


    public CustomWait(WebDriver driver, Duration timeouts) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeouts);
    }
     public void waitForVisibilityOfElement(WebElement webElement){
        try {
            wait.until(ExpectedConditions.visibilityOf(webElement));
        }catch (TimeoutException e){
            //e.printStackTrace();
            setElementPresentState(false);
            System.err.println("Element is not visible after 60 seconds."+ e.getMessage());
        }
     }

    private void setElementPresentState(boolean state) {
        elementState=state;
    }

    public boolean getElementPresentState() {
        return elementState;
    }

    public void waitForElementToBeClickable(WebElement webElement){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
        }catch (TimeoutException e){
            //e.printStackTrace();
            System.err.println("Element is not clickable" + e.getMessage());
        }
    }
}
