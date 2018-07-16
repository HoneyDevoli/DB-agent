package repository;

import config.AgentData;
import config.DBConnector;
import entity.PeopleEX;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Date;
import java.util.List;

public class PeopleINRepository {
    private static final Logger logger = Logger.getLogger(PeopleINRepository.class);
    private static PreparedStatement updStatement, addStatement, delStatement;
    private static int countDel = 0, countUpd = 0, countAdd = 0;

    public static boolean checkPeople(Connection connection, PeopleEX man, String agentId) throws SQLException {
        String selectTableSQL = "SELECT * FROM PEOPLE_IN_TOWN" +
                " WHERE FIRST_NAME = ? AND SECOND_NAME = ? AND AGENT_ID = ? ;";
        try(PreparedStatement statement = connection.prepareStatement(selectTableSQL)){
            statement.setString(1, man.getFirstName());
            statement.setString(2, man.getSecondName());
            statement.setString(3, agentId);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
    }

    public static void addPeople(Connection connection, PeopleEX man, String agentId) throws SQLException {
        String insertObjSQL = "INSERT INTO PEOPLE_IN_TOWN "
                + "(CHANGED_DATETIME,FIRST_NAME,SECOND_NAME,COLOR_HAIR, AGENT_ID)"
                + " VALUES (?,?,?,?,?)";

        if(addStatement == null || addStatement.isClosed()) {
            addStatement = connection.prepareStatement(insertObjSQL);
        }
        addStatement.setTimestamp(1, new Timestamp(man.getDateEvent().getTime()));
        addStatement.setString(2, man.getFirstName());
        addStatement.setString(3, man.getSecondName());
        addStatement.setString(4, man.getColorHair());
        addStatement.setString(5, agentId);
        addStatement.addBatch();

        countAdd++;
    }

    public static void delPeople(Connection connection, PeopleEX man, String agentId) throws SQLException {
        String deleteTableSQL = "DELETE FROM PEOPLE_IN_TOWN WHERE FIRST_NAME = ? AND SECOND_NAME = ? AND AGENT_ID = ? ;";

        if(delStatement == null || delStatement.isClosed()) {
            delStatement = connection.prepareStatement(deleteTableSQL);
        }
        delStatement.setString(1, man.getFirstName());
        delStatement.setString(2, man.getSecondName());
        delStatement.setString(3, agentId);
        delStatement.addBatch();

        countDel++;
    }

    public static void updPeople(Connection connection,PeopleEX man,String agentId) throws SQLException {
        String updateSQL = "UPDATE PEOPLE_IN_TOWN "
                + "SET  COLOR_HAIR = ?, CHANGED_DATETIME = ?"
                + " WHERE FIRST_NAME = ? AND SECOND_NAME = ? AND AGENT_ID = ? ;";

        if(updStatement== null || updStatement.isClosed()) {
            updStatement = connection.prepareStatement(updateSQL);
        }

        updStatement.setString(1, man.getColorHair());
        updStatement.setTimestamp(2, new Timestamp(man.getDateEvent().getTime()));
        updStatement.setString(3, man.getFirstName());
        updStatement.setString(4, man.getSecondName());
        updStatement.setString(5, agentId);
        updStatement.addBatch();

        countUpd++;
    }

    public static Timestamp selectMaxDateForAgent(String agentId) throws SQLException {
        String selectSQL = "SELECT MAX(CHANGED_DATETIME) as MAX_DATE  FROM PEOPLE_IN_TOWN WHERE AGENT_ID = ?";

        try (Connection connection = DBConnector.getConnectionToInnerDB()) {
            PreparedStatement statement = connection.prepareStatement(selectSQL);
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

                logger.debug("Last date event is " + lastDate);
                statement.close();
                return lastDate;
            }
        }
        return null;
    }

    public static void divisionPeople(List<PeopleEX> people) throws SQLException {

        AgentData config = AgentData.getAgentData();
        try (Connection connection = DBConnector.getConnectionToInnerDB()) {
            for (PeopleEX man : people) {
                if (man.isArrival()) {
                    if (checkPeople(connection, man, config.getIdAgent())) {
                        updPeople(connection, man, config.getIdAgent() );
                    } else {
                        addPeople(connection, man, config.getIdAgent());
                    }
                } else {
                    if (checkPeople(connection, man, config.getIdAgent())) {
                        delPeople(connection, man, config.getIdAgent());
                    }
                }
                executeBatches(10);
            }
            executeBatches(0);


            if (people.size() > 0) {
                config.setStartDate(people.stream().map(man -> man.getDateEvent()).max(Date::compareTo).get());
                logger.debug("Set new date: " + config.getStartDate());
            } else {
                logger.info("New people in external database not found.");
            }
        }
    }

    private static void executeBatches(int countToExecute) throws SQLException {
        if(countAdd > countToExecute) {
            executeBatch(addStatement);
            logger.trace(String.format("%d people was added.", countAdd));
            countAdd = 0;
        }

        if(countDel > countToExecute) {
            executeBatch(delStatement);
            logger.trace(String.format("%d people was deleted.",countDel));
            countDel = 0;
        }

        if(countUpd > countToExecute) {
            executeBatch(updStatement);
            logger.trace(String.format("%d people was updated.",countUpd));
            countUpd = 0;
        }
    }

    private static void executeBatch(PreparedStatement statement) throws SQLException {
        statement.executeBatch();
        statement.clearBatch();
        statement.close();
    }
}

