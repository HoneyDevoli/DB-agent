package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysqlConfig {
    private static final String CLASS_NAME = MysqlConfig.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String OUTER_DB_URL = "jdbc:mysql://localhost:3306/external_people_db";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void testConnectionToDB() {
        final String METHOD_NAME = "testConnectionToDB";
        logger.log(Level.SEVERE,"Testing connection to MySQL JDBC");
        Connection connectionServer = null;
        try {
            Class.forName(DRIVER);
            logger.log(Level.SEVERE,"MySQL JDBC Driver successfully connected");

            connectionServer = DriverManager.getConnection(SERVER_URL, USER, PASS);
            logger.log(Level.SEVERE,"You successfully connected to server MySQL now");

        } catch (ClassNotFoundException e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "MySQL JDBC Driver is not found. Include it in your library path", e);
            return;
        } catch (SQLException e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Failed to make connection to server MySQL. Please check properties", e);
            return;
        }

        try{
            Connection connection = DriverManager.getConnection(OUTER_DB_URL, USER, PASS);
            logger.log(Level.SEVERE,"You successfully connected to external db MySQL");
        } catch (SQLException e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Database \"external_people_db\" doesn`t exist", e);
            return;
        }


    }

    public static Connection getConnectionToOuterDB() {
        final String METHOD_NAME = "getConnectionToOuterDB";

        Connection dbConnection = null;
        try {
            Class.forName(DRIVER);
            dbConnection = DriverManager.getConnection(OUTER_DB_URL, USER, PASS);
            return dbConnection;
        } catch (ClassNotFoundException e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "MySQL JDBC Driver is not found. Include it in your library path", e);
        } catch (SQLException e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Failed to make connection to Outer DateBase Postgres. Please check properties", e);
        }
        return dbConnection;
    }

    public static Connection getConnectionToServer(){
        Connection connectionServer = null;
        try {
            Class.forName(DRIVER);
            connectionServer = DriverManager.getConnection(SERVER_URL, USER, PASS);
            return  connectionServer;

        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE,"MySQL JDBC Driver is not found. Include it in your library path", e);
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE,"Failed to make connection to server MySQL. Please check properties", e);
            return null;
        }
    }
}
