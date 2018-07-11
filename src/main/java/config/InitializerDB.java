package config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitializerDB {

    private static final String CLASS_NAME = PostgreConfig.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    public static void CreateInnerDb(Connection connection){
        Statement stmt;

        try {
            stmt = connection.createStatement();

            String sql = "CREATE DATABASE internal_people_db";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();

            stmt = PostgreConfig.getConnectionToInnerDB().createStatement();
            sql =   "CREATE TABLE people " +
                    "(changed_datetime     timestamp NOT NULL," +
                    " first_name     text    NOT NULL, " +
                    " second_name    text NOT NULL, " +
                    " color_hair     text  NOT NULL," +
                    " agent_id text NOT NULL," +
                    " CONSTRAINT \"people_pkey\" PRIMARY KEY (first_name, second_name, agent_id))";
            stmt.executeUpdate(sql);
            stmt.close();
            logger.log(Level.SEVERE,"The database \"internal_people_db\" was created successfully");
        } catch (SQLException e) {
            logger.log(Level.SEVERE,"Database \"internal_people_db\" creation error ",e);
        }
    }

    public static void CreateOuterDbPostgre(Connection connection){
        Statement stmt;

        try {
            stmt = connection.createStatement();

            String sql = "CREATE DATABASE external_people_db";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();

            stmt = PostgreConfig.getConnectionToInnerDB().createStatement();
            sql =   "CREATE TABLE people " +
                    "(date_event     timestamp NOT NULL," +
                    " first_name     text    NOT NULL, " +
                    " is_arrival     boolean NOT NULL, " +
                    " second_name    text NOT NULL, " +
                    " color_hair     text  NOT NULL," +
                    " CONSTRAINT \"people_pkey\" PRIMARY KEY (first_name, second_name))";
            stmt.executeUpdate(sql);
            stmt.close();
            logger.log(Level.SEVERE,"The database \"external_people_db\" was created successfully");
        } catch (SQLException e) {
            logger.log(Level.SEVERE,"Database \"external_people_db\" creation error ",e);
        }
    }

    public static void CreateOuterDbMySQL(Connection connection){
        Statement stmt;

        try {
            stmt = connection.createStatement();

            String sql = "CREATE DATABASE external_people_db;";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();

            stmt = MysqlConfig.getConnectionToOuterDB().createStatement();
            sql =   "CREATE TABLE people " +
                    "(date_event     timestamp NOT NULL," +
                    " first_name     varchar(255)    NOT NULL, " +
                    " is_arrival     boolean NOT NULL, " +
                    " second_name    varchar(255) NOT NULL, " +
                    " color_hair     varchar(255)  NOT NULL," +
                    " PRIMARY KEY (first_name, second_name))";
            stmt.executeUpdate(sql);
            stmt.close();
            logger.log(Level.SEVERE,"The database \"external_people_db\" was created successfully");
        } catch (SQLException e) {
            logger.log(Level.SEVERE,"Database \"external_people_db\" creation error ",e);
        }
    }




}
