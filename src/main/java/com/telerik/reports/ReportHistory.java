package com.telerik.reports;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ReportHistory {

    public static void copyHistoryTo(String currentReportDir) {
        Path historySource = Paths.get(ExtentManager.getReportDir(), "history");
        Path historyTarget = Paths.get(currentReportDir, "history");

        if (!Files.exists(historySource)) return;

        try {
            Files.walk(historySource).forEach(source -> {
                try {
                    Path dest = historyTarget.resolve(historySource.relativize(source));
                    Files.createDirectories(dest.getParent());
                    Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveLatestHistory(String reportPath) {
        Path historyDirInLatest = Paths.get(reportPath).getParent().resolve("history");
        Path mainHistoryPath = Paths.get(ExtentManager.getReportDir(), "history");

        try {
            Files.walk(historyDirInLatest).forEach(source -> {
                try {
                    Path dest = mainHistoryPath.resolve(historyDirInLatest.relativize(source));
                    Files.createDirectories(dest.getParent());
                    Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
