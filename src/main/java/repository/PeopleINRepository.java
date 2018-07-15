package repository;

import config.DBConnector;
import entity.PeopleEX;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class PeopleINRepository {
    private static final String CLASS_NAME = PeopleINRepository.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    public static Boolean checkPeople(String firstName, String secondName, String agentId) {
        PreparedStatement statement = null;
        String selectTableSQL = "SELECT * FROM PEOPLE_IN_TOWN" +
                " WHERE FIRST_NAME = ? AND SECOND_NAME = ? AND AGENT_ID = ? ;";

        try (Connection connection = DBConnector.getConnectionToInnerDB()) {
            statement = connection.prepareStatement(selectTableSQL);
            statement.setString(1, firstName);
            statement.setString(2, secondName);
            statement.setString(3, agentId);

           //logger.info(String.format("Check people with name  %s %s .....", firstName, secondName));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return false;
    }

    public static void addPeople(String firstName, String secondName, String agentId, String colorHair, Date changedDate) {
        String insertObjSQL = "INSERT INTO PEOPLE_IN_TOWN "
                + "(CHANGED_DATETIME,FIRST_NAME,SECOND_NAME,COLOR_HAIR, AGENT_ID)"
                + " VALUES (?,?,?,?,?)";

        PreparedStatement statement = null;
        try (Connection connection = DBConnector.getConnectionToInnerDB()) {
            statement = connection.prepareStatement(insertObjSQL);
            statement.setTimestamp(1, new java.sql.Timestamp(changedDate.getTime()));
            statement.setString(2, firstName);
            statement.setString(3, secondName);
            statement.setString(4, colorHair);
            statement.setString(5, agentId);
            statement.executeUpdate();

            logger.info(String.format("People with name  %s %s was added.", firstName, secondName));
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public static void delPeople(String firstName, String secondName, String agentId) {
        PreparedStatement statement = null;
        String deleteTableSQL = "DELETE FROM PEOPLE_IN_TOWN WHERE FIRST_NAME = ? AND SECOND_NAME = ? AND AGENT_ID = ? ;";
        try (Connection connection = DBConnector.getConnectionToInnerDB()) {
            statement = connection.prepareStatement(deleteTableSQL);
            statement.setString(1, firstName);
            statement.setString(2, secondName);
            statement.setString(3, agentId);
            statement.executeUpdate();

            logger.info(String.format("People with name %s %s was deleted", firstName, secondName));
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public static void updPeople(String firstName, String secondName, String agentId, String colorHair, Date changedDate) {
        String updateSQL = "UPDATE PEOPLE_IN_TOWN "
                + "SET  COLOR_HAIR = ?, CHANGED_DATETIME = ?"
                + " WHERE FIRST_NAME = ? AND SECOND_NAME = ? AND AGENT_ID = ? ;";

        PreparedStatement statement = null;
        try (Connection connection = DBConnector.getConnectionToInnerDB()) {
            statement = connection.prepareStatement(updateSQL);
            statement.setString(1, colorHair);
            statement.setTimestamp(2, new java.sql.Timestamp(changedDate.getTime()));
            statement.setString(3, firstName);
            statement.setString(4, secondName);
            statement.setString(5, agentId);
            statement.executeUpdate();

            logger.info(String.format("People with name %s %s was updated", firstName, secondName));
        } catch (SQLException e) {
            logger.info(e);
        }
    }

    public static Timestamp selectMaxDateForAgent(String agentId) {
        PreparedStatement statement = null;
        String selectSQL = "SELECT MAX(CHANGED_DATETIME) as MAX_DATE  FROM PEOPLE_IN_TOWN WHERE AGENT_ID = ?";

        try (Connection connection = DBConnector.getConnectionToInnerDB()) {
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, agentId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Timestamp lastDate = rs.getTimestamp("MAX_DATE");
                if(lastDate != null) {
                    int nanos = lastDate.getNanos() / 1000000;
                    nanos++;
                    nanos = nanos * 1000000;
                    lastDate.setNanos(nanos);
                }

                logger.info("Last date event is " + lastDate);
                return lastDate;
            }

        } catch (SQLException e) {
            logger.error(e);
        }
        return null;
    }
}
