package com.telerik.base;

import com.telerik.utils.ConfigReader;
import com.telerik.utils.CustomWait;
import com.telerik.utils.ElementUtil;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected CustomWait customWait;
    protected ConfigReader configReader;
    protected ElementUtil elementUtil;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil();
        this.customWait = new CustomWait(driver, Duration.ofSeconds(ConfigReader.getConfigReader().getGlobalWaitValue()));
    }

    public WebDriver getDriver(String browser) {
        WebDriver driver;
        String runMode = ConfigReader.getConfigReader().getRunMode(); // "local" or "remote"

        if (runMode.equalsIgnoreCase("remote")) {
            driver = getRemoteDriver(browser);
        } else {
            driver = getLocalDriver(browser);
        }

        return driver;
    }

    private WebDriver getLocalDriver(String browser) {
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;

            case "firefox":
                driver = new FirefoxDriver();
                break;

            case "safari":
                driver = new SafariDriver();
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        return driver;
    }


    private WebDriver getRemoteDriver(String browser)  {
        MutableCapabilities options;

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--incognito");
                options = chromeOptions;
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-private");
                options = firefoxOptions;
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("-inprivate");
                options = edgeOptions;
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(ConfigReader.getConfigReader().getHubURL() +"/wd/hub"), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        driver.manage().window().maximize();
        return driver;
    }


    public WebElement getTheElementAfterVisible(WebElement element){
        customWait.waitForVisibilityOfElement(element);
        return element;
    }

    public void quitDriver(){
        driver.quit();
    }
    public String getTitle(){
        return driver.getTitle();
    }

    public Boolean verifyLogo(){
        return true;
    }
}
