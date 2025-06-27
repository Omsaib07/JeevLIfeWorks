package com.sms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new SMSException("Unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new SMSException("Error loading configuration file", ex);
        }
    }

    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }

    public static String getCsvExportPath() {
        return properties.getProperty("student.csv.export.path");
    }

    public static String getLogFilePath() {
        return properties.getProperty("log.file.path");
    }
}