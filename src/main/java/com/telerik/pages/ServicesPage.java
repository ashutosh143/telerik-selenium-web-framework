package com.telerik.pages;

import com.telerik.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ServicesPage extends BasePage {

    public ServicesPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        initializeElementMap(); // Initialize mapping once in constructor
    }

    // WebElements
    @FindBy(xpath = "//h2[@id='consulting']")
    private WebElement consultingWebElement;

    @FindBy(xpath = "//h3[normalize-space()='Kendo UI Consulting']")
    private WebElement kendoUiConsultingWebElement;

    @FindBy(xpath = "//h3[normalize-space()='Telerik Consulting']")
    private WebElement telerikConsultingWebElement;

    // Element map
    private final Map<String, Supplier<WebElement>> elementMap = new HashMap<>();

    private void initializeElementMap() {
        elementMap.put("Consulting", () -> consultingWebElement);
        elementMap.put("Kendo UI Consulting", () -> kendoUiConsultingWebElement);
        elementMap.put("Telerik Consulting", () -> telerikConsultingWebElement);
        // ➕ Add more mappings here
    }

    public boolean verifyPresenceOfElementsOnPage(List<String> fieldNames) {
        return elementUtil.verifyPresenceOfElementsOnPage(fieldNames, elementMap);
    }


    public boolean validateIfContinueButtonIsDisplayed() {
        // Placeholder method, to be implemented
        return true;
    }
}
