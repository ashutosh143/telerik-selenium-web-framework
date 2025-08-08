package com.telerik.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.telerik.basetest.BaseTest;
import com.telerik.reports.ExtentReportManager;
import com.telerik.utils.ConfigReader;
import com.telerik.utils.EmailUtil;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MyListener extends BaseTest implements ITestListener, IAnnotationTransformer, ISuiteListener {

    private static final Logger logger = LogManager.getLogger(MyListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.setupExtentReport();
        String className = getSimpleClassName(result);
        String methodName = result.getMethod().getMethodName();

        logger.info("🟡 Starting Test: {} => {}", className, methodName);

        String[] groups = result.getTestContext().getIncludedGroups();
        String includedGroups = String.join(",", groups);
        ExtentReportManager.createTest(className + "." + methodName, includedGroups, className,"Ashutosh Rawat");
        ExtentReportManager.getTest().log(Status.INFO, "Test Started: " + className + " => " + methodName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String className = getSimpleClassName(result);
        String methodName = result.getMethod().getMethodName();

        logger.info("✅ Test Passed: {} => {}", className, methodName);
        ExtentReportManager.getTest().log(Status.PASS, "Test Passed: " + className + " => " + methodName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String className = getSimpleClassName(result);
        String methodName = result.getMethod().getMethodName();

        logger.error("❌ Test Failed: {} => {}", className, methodName);
        logger.error("Reason: ", result.getThrowable());
        ExtentReportManager.getTest().log(Status.FAIL, "Test Failed: " + className + " => " + methodName);
        ExtentReportManager.getTest().log(Status.FAIL, "Reason: " + result.getThrowable());

        captureScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String className = getSimpleClassName(result);
        String methodName = result.getMethod().getMethodName();

        logger.warn("⚠️ Test Skipped: {} => {}", className, methodName);
        logger.error("Reason: ", result.getThrowable());
        ExtentReportManager.getTest().log(Status.SKIP, "Test Skipped: " + className + " => " + methodName);
        ExtentReportManager.getTest().log(Status.SKIP, "Reason: " + result.getThrowable());
    }


    public void onFinish(ISuite suite) {
        if(Boolean.parseBoolean(ConfigReader.getConfigReader().sentMailToUser())) {
            try {
                logger.info("Sending the Email......");
                String folderToZip = ExtentReportManager.reportFolderPath;
                String zipFile = folderToZip + ".zip";
                zipReportFolder(folderToZip, zipFile);
                EmailUtil.sendEmailWithAttachment(zipFile);
            } catch (Exception e) {
                logger.error("Error zipping or emailing report", e);
            }
        }
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(MyRetryAnalyzer.class);
    }

    // ===== Utility Methods =====

    private String getSimpleClassName(ITestResult result) {
        String fqcn = result.getTestClass().getName(); // fully qualified class name
        return fqcn.substring(fqcn.lastIndexOf('.') + 1);
    }

    private void captureScreenshot(ITestResult result) {
        WebDriver driver = BaseTest.getDriver();
        if (driver == null) {
            logger.warn("WebDriver is null, skipping screenshot capture");
            return;
        }

        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
            String timestamp = formatter.format(now);
            String screenshotName = result.getName() + "_" + timestamp + ".png";

            String screenshotDir = System.getProperty("user.dir") + "/extent-reports/screenshots/";
            File screenshotFile = new File(screenshotDir + screenshotName);
            FileUtils.forceMkdirParent(screenshotFile);

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, screenshotFile);
            logger.info("📸 Screenshot saved at: {}", screenshotFile.getAbsolutePath());

            if (ConfigReader.getConfigReader().getRunMode().equalsIgnoreCase("remote")) {
                String base64Screenshot = ExtentReportManager.convertImgToBase64(screenshotFile.getAbsolutePath());
                ExtentReportManager.getTest().fail("Screenshot on Failure",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            } else {
                // use absolute path, not relative
                String relativePath = "../screenshots/" + screenshotName;
                ExtentReportManager.getTest().fail("Screenshot on Failure",
                        MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
            }
        } catch (IOException e) {
            logger.error("❌ Failed to capture screenshot", e);
        }
    }



    private void zipReportFolder(String folderPath, String zipFilePath) throws IOException {
        Path zipPath = Paths.get(zipFilePath);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Path path = Paths.get(folderPath);
            Files.walk(path).filter(p -> !Files.isDirectory(p)).forEach(p -> {
                ZipEntry zipEntry = new ZipEntry(path.relativize(p).toString());
                try {
                    zs.putNextEntry(zipEntry);
                    Files.copy(p, zs);
                    zs.closeEntry();
                } catch (IOException e) {
                    logger.error("Zipping failed", e);
                }
            });
        }
        logger.info("📦 Report zipped: {}", zipFilePath);
    }
}
