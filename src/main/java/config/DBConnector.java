package config;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String CLASS_NAME = DBConnector.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    public static void testConnectionToDB() throws SQLException {
        logger.info("Testing connection to JDBC");
        Connection con = getConnectionToInnerDB();
        if(con != null) {
            logger.info("You successfully connected to internal db");
            con.close();
        }

        con = getConnectionToOuterDB();
        if(con != null) {
            logger.info("You successfully connected to external db");
            con.close();
        }


    }

    public static Connection getConnectionToInnerDB() {
        AgentData agentData = AgentData.getAgentData();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(agentData.getInnerDbURL(), agentData.getInnerUser(), agentData.getInnerPassword());
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to make connection to Outer DateBase. Please check properties", e);
        }
        return connection;
    }

    public static Connection getConnectionToOuterDB() {

        AgentData agentData = AgentData.getAgentData();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(agentData.getOuterDbURL(),  agentData.getOuterUser(), agentData.getOuterPassword());
            return connection;
        }  catch (SQLException e) {
            logger.error("Failed to make connection to Outer DateBase. Please check properties", e);
        }
        return connection;
    }

}

