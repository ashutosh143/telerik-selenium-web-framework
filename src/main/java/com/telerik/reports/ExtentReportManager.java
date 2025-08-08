package com.telerik.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.telerik.utils.ConfigReader;
import com.telerik.utils.LogFileHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ExtentReportManager {
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();
    public static String reportFolderPath;
    public static ExtentTest logSummaryTest;
    public static ExtentTest apiSummaryTest;
    private static final StringBuilder apiLogBuffer = new StringBuilder();


    public static void setupExtentReport() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"));
            reportFolderPath = System.getProperty("user.dir") + "/extent-reports/html-reports";
            ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(reportFolderPath + "/Report_" + timestamp + ".html");
            extentSparkReporter.config().setTheme(Theme.DARK);
            extentSparkReporter.config().setDocumentTitle("Automation Execution Report");
            extentSparkReporter.config().setReportName("Telerik Automation Report");

            extentReports = new ExtentReports();
            extentReports.attachReporter(extentSparkReporter);
            extentReports.setSystemInfo("Environment", "QA");
            extentReports.setSystemInfo("Browser", ConfigReader.getConfigReader().getBrowser());
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            logSummaryTest = extentReports.createTest("📜 Execution Log Summary");
            apiSummaryTest = extentReports.createTest("\uD83D\uDE80 API Execution Summary");

        }
    }

    public static void createTest(String testName, String groups, String classNameTable, String author) {
        ExtentTest extentTest = extentReports.createTest("<b>" + testName + "</b>")
                .assignCategory(groups, classNameTable)
                .assignAuthor(author)
                .assignDevice("chrome");
        extentTestThreadLocal.set(extentTest);
    }

    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    public static ExtentReports getReport() {
        return extentReports;
    }

    public static void flushReport() {
        if (extentReports != null) {
            String logPath = LogFileHelper.getCurrentLogFilePath();
            if (logSummaryTest != null) {
                extentTestThreadLocal.set(logSummaryTest); // ensure correct test is active
                attachLogFileContent(logPath);
            }
          //   attachApiLogsBasicFormat();
//            if (apiSummaryTest != null && !apiLogBuffer.isEmpty()) {
//                attachApiLogsAdvanceFormat(apiSummaryTest, apiLogBuffer.toString());
//            }
            extentReports.flush();
        }
    }


    public static String convertImgToBase64(String screenshotPath) {
        try {
            byte[] file = FileUtils.readFileToByteArray(new File(screenshotPath));
            return Base64.encodeBase64String(file);
        } catch (IOException e) {
            throw new RuntimeException("Error reading screenshot file: " + screenshotPath, e);
        }
    }

    public static void attachLogFileContent1(String logFilePath) {
        try {
            String logContent = FileUtils.readFileToString(new File(logFilePath), "UTF-8");

            // Escape HTML characters to prevent rendering issues
            logContent = logContent.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

            // Use neutral styling for theme adaptability
            String htmlLog = "<pre style='padding:10px; border:1px solid #ccc; background-color: transparent; color: inherit; font-size:13px;'>"
                    + logContent +
                    "</pre>";

            getTest().info(htmlLog);
        } catch (IOException e) {
            getTest().warning("⚠️ Unable to attach log file: " + e.getMessage());
        }
    }

    public static void attachLogFileContent(String logFilePath) {
        try {
            String logContent = FileUtils.readFileToString(new File(logFilePath), "UTF-8");

            // Escape HTML characters to prevent rendering issues
            logContent = logContent.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

            // Final version: Clean and borderless <pre>
            String htmlLog = """
                <div style='font-family:monospace;'>
                    <div style='display:inline-block; background-color:#9b59b6; color:white; font-size:12px; font-weight:bold; padding:2px 6px; border-radius:4px; margin-bottom:6px;'>Execution Logs</div>
                    <div style='border:1px solid #9b59b6; border-radius:6px; background-color:#1e1e1e; padding:10px;'>
                        <pre style='color:white; background:none; border:none; padding:0; margin:0; box-shadow:none;'>""" + logContent + "</pre></div></div>";

            getTest().info(htmlLog);
        } catch (IOException e) {
            getTest().warning("⚠️ Unable to attach log file: " + e.getMessage());
        }
    }






    public static void attachApiLogsBasicFormat() {
        // Define your API log text
        String apiLog = apiLogBuffer.toString();

        // Styled HTML block with inline green "API Logs" tag and preformatted log text
        String styledApiLog = """
                <div style='border:1px solid #2ecc71; border-radius:6px; background-color:#1e1e1e; padding:10px; font-family:monospace;'>
                    <span style='display:inline-block; background-color:#2ecc71; color:white; font-size:12px; font-weight:bold; padding:2px 6px; border-radius:4px; margin-bottom:8px;'>API Logs</span><br>
                    <pre style='color:white; margin:0;'>""" + apiLog + "</pre></div>";

        // Log it in a single entry to keep the label and log together
        apiSummaryTest.info(styledApiLog);
    }


    public static void attachApiLogsAdvanceFormat1(ExtentTest extentTest, String apiLog) {

        String styledApiLog = """
                <style>
                    .log-box {
                        border: 1px solid #2ecc71;
                        border-radius: 6px;
                        background-color: #1e1e1e;
                        padding: 10px;
                        font-family: monospace;
                        color: white;
                        position: relative;
                    }
                    .log-label {
                        display: inline-block;
                        background-color: #2ecc71;
                        color: white;
                        font-size: 12px;
                        font-weight: bold;
                        padding: 2px 6px;
                        border-radius: 4px;
                        margin-bottom: 4px;
                    }
                    .log-content {
                        max-height: 6.5em;
                        overflow: hidden;
                        transition: max-height 0.3s ease;
                        white-space: pre-wrap;
                    }
                    .log-box.expanded .log-content {
                        max-height: 1000px;
                    }
                    .toggle-btn {
                        color: #2ecc71;
                        cursor: pointer;
                        font-size: 13px;
                        margin-top: 8px;
                        display: inline-block;
                    }
                </style>
                
                <div class='log-label'>API Logs</div>
                <div class='log-box' onclick='
                    var box = this;
                    var isExpanded = box.classList.toggle("expanded");
                    var btn = box.querySelector(".toggle-btn");
                    btn.innerText = isExpanded ? "Hide Full Log" : "Show Full Log";
                '>
                    <div class='log-content'>""" + apiLog + "</div>" + """
                    <div class='toggle-btn'>Show Full Log</div>
                </div>
                """;

        extentTest.info(styledApiLog);
    }

    public static void attachApiLogsAdvanceFormat(ExtentTest extentTest, String apiLog) {
        String escapedLog = escapeHtml(apiLog);

        String html = """
        <style>
            .log-box {
                border: 1px solid #2ecc71;
                border-radius: 6px;
                background-color: #1e1e1e;
                padding: 10px;
                font-family: monospace;
                color: white;
                margin-bottom: 10px;
                max-height: 400px;      /* Set a visible height */
                overflow-y: auto;       /* Enable vertical scrolling */
                white-space: pre-wrap;
            }
            .log-label {
                background-color: #2ecc71;
                color: white;
                font-size: 12px;
                font-weight: bold;
                padding: 2px 6px;
                border-radius: 4px;
                margin-bottom: 6px;
                display: inline-block;
            }
        </style>
        <div class="log-label">API Logs</div>
        <div class="log-box">%s</div>
        """.formatted(escapedLog);

        extentTest.info(html);
    }


    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }



    public static void logApiToTestAndSummary(String title, String fullFormattedApiLog) {
        String logEntry = title + "\n\n" + fullFormattedApiLog;

        // Add to central summary buffer
        apiLogBuffer.append(logEntry).append("\n\n");

        // Log to current test
        ExtentTest test = getTest();
        if (test != null) {
            attachApiLogsAdvanceFormat(test, logEntry);
        }
    }

}

