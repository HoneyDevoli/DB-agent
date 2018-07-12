package config;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigDB {

    private static final String CLASS_NAME = ConfigDB.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    private static final String INNER_DB_URL = "jdbc:postgresql://localhost:5432/internal_people_db";
    private static final String INNER_USER = "postgres";
    private static final String INNER_PASS = "pgpassword";

    private static final String OUTER_DB_URL = "jdbc:postgresql://localhost:5432/external_people_db";
    private static final String OUTER_USER = "postgres";
    private static final String OUTER_PASS = "pgpassword";


    public static void testConnectionToDB() {

        logger.info("Testing connection to JDBC");
        try{
            Connection connection = DriverManager.getConnection(OUTER_DB_URL, OUTER_USER, OUTER_PASS);
            logger.info("You successfully connected to external db");

        } catch (SQLException e) {
            logger.error("Database \"external_people_db\" doesn`t exist", e);
            return;
        }

        try{
            Connection connection = DriverManager.getConnection(INNER_DB_URL, INNER_USER, INNER_PASS);
            logger.info("You successfully connected to internal db");
        } catch (SQLException e) {
            logger.error("Database \"internal_people_db\" doesn`t exist", e);
            return;
        }


    }

    public static Connection getConnectionToInnerDB() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(INNER_DB_URL, INNER_USER, INNER_PASS);
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to make connection to Outer DateBase. Please check properties", e);
        }
        return connection;
    }

    public static Connection getConnectionToOuterDB() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(OUTER_DB_URL, OUTER_USER, OUTER_PASS);
            return connection;
        }  catch (SQLException e) {
            logger.error("Failed to make connection to Outer DateBase. Please check properties", e);
        }
        return connection;
    }

}

