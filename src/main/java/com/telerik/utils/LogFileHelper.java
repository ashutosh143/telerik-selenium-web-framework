package com.telerik.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.appender.RollingFileAppender;

public class LogFileHelper {

    public static String getCurrentLogFilePath() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        RollingFileAppender appender = (RollingFileAppender) config.getAppender("RollingFile");
        return appender.getFileName();  // Returns path like ./logs/2025-08-02-02_19-47.log
    }
}

