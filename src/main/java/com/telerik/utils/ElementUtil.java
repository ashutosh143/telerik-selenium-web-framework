package com.telerik.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ElementUtil {
    private static final Logger logger = LogManager.getLogger(ElementUtil.class);

    public WebElement getElementByFieldName(String fieldName, Map<String, Supplier<WebElement>> elementMap) {
        Supplier<WebElement> supplier = elementMap.get(fieldName);

        if (supplier == null) {
            logger.info("⚠️ No mapping found for field: " + fieldName);
            return null;
        }

        try {
            return supplier.get();
        } catch (Exception e) {
            logger.info("❌ Exception while retrieving element for field: {}", fieldName);
            e.printStackTrace();
            return null;
        }
    }

    public boolean verifyPresenceOfElementsOnPage(List<String> fieldNames, Map<String, Supplier<WebElement>> elementMap) {
        boolean allPresent = true;

        for (String fieldName : fieldNames) {
            WebElement element = getElementByFieldName(fieldName, elementMap);

            if (element == null) {
                logger.info("Element is null. Please check WebElement mapping for: " + fieldName);
                allPresent = false;
                continue;
            }

 //           if (!customWait.getElementPresentState()) {
//                System.out.println("⛔ Wait condition failed for: " + fieldName);
//                allPresent = false;
//                continue;
//            }

            if (element.isDisplayed()) {
                logger.info(" {} is present on the page.", fieldName);
            } else {
                logger.info(" {} is NOT present on the page.", fieldName);
                allPresent = false;
            }
        }

        return allPresent;
    }
}

