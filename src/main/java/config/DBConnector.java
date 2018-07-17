package config;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final Logger logger = Logger.getLogger(DBConnector.class);

    public static void testConnectionToDB()  {
        logger.info("Testing connection to JDBC");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }

        Connection con = null;
        try {
            con = getConnectionToInnerDB();
            if (con != null) {
                logger.info("You successfully connected to internal db");
                con.close();
            }
        } catch (SQLException e) {
            logger.error("Failed to make connection to Inner DateBase. Please check properties");
        }

        try{
            con = getConnectionToOuterDB();
            if(con != null) {
                logger.info("You successfully connected to external db\n");
                con.close();
            }
        } catch (SQLException e){
            logger.error("Failed to make connection to Outer DateBase. Please check properties");
        }


    }

    public static Connection getConnectionToInnerDB() throws SQLException {
        AgentData agentData = AgentData.getAgentData();
        return DriverManager.getConnection(agentData.getInnerDbURL(), agentData.getInnerUser(), agentData.getInnerPassword());
    }

    public static Connection getConnectionToOuterDB() throws SQLException {

        AgentData agentData = AgentData.getAgentData();
        return DriverManager.getConnection(agentData.getOuterDbURL(),  agentData.getOuterUser(), agentData.getOuterPassword());

    }

}

