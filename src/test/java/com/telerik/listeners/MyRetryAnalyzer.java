package com.telerik.listeners;

import com.telerik.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class MyRetryAnalyzer implements IRetryAnalyzer {

    ConfigReader configReader = ConfigReader.getConfigReader();
    private int retryCount = 0;
    private static final Logger logger = LogManager.getLogger(MyRetryAnalyzer.class);
    @Override
    public boolean retry(ITestResult iTestResult) {
        int max_retryCount = Integer.parseInt(configReader.getMaxRetriesFailCaseCount());
        if(retryCount < max_retryCount && !iTestResult.isSuccess()){
            retryCount++;
            logger.info("Retrying the Test --->>> " + iTestResult.getName() + " Attempt No ---> " + retryCount);
            return true;
        }
        return false;
    }
}
