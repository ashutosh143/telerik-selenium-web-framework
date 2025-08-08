package com.telerik.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    private static final String REPORT_DIR = "extent-report";
    private static final String HISTORY_DIR = REPORT_DIR + "/history";
    private static final String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    private static final String REPORT_FILE = REPORT_DIR + "/Report_" + timestamp + ".html";

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static void createInstance() {
        // Create report directories
        new File(REPORT_DIR).mkdirs();
        new File(HISTORY_DIR).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_FILE);

        // Report appearance settings
        spark.config().setReportName("Automation Execution Report");
        spark.config().setDocumentTitle("Test Results");
        spark.config().setTheme(Theme.STANDARD); // DARK or STANDARD
        spark.config().setEncoding("utf-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA");
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static String getCurrentReportPath() {
        return REPORT_FILE;
    }

    public static String getReportDir() {
        return REPORT_DIR;
    }
}
