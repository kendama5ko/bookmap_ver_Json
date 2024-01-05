package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseSettings {

    // プロパティファイルのパスを指定
    private static final String mySqlProperties = System.getProperty("user.dir") + "\\src\\mysql.properties";

    public static Connection getConnection() {
        try {
            Properties properties = getProperties(mySqlProperties);
//            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    properties.getProperty("URL"),
                    properties.getProperty("USER"),
                    properties.getProperty("PASS"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Properties getProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        }
        return properties;
    }
}
