package repository;

import config.DBConnector;
import entity.PeopleEX;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PeopleEXRepository {

    public static List<PeopleEX> getPeople(Date date) throws SQLException {
        String selectTableSQL = "SELECT DATE_EVENT, FIRST_NAME, IS_ARRIVAL, SECOND_NAME, COLOR_HAIR FROM people" +
                " WHERE DATE_EVENT > ?;";
        List<PeopleEX> people = new ArrayList();

        try (Connection connection = DBConnector.getConnectionToOuterDB();
             PreparedStatement statement = connection.prepareStatement(selectTableSQL)){

            statement.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                PeopleEX newPeople = new PeopleEX();
                newPeople.setFirstName(rs.getString("FIRST_NAME"));
                newPeople.setSecondName(rs.getString("SECOND_NAME"));
                newPeople.setArrival(rs.getBoolean("IS_ARRIVAL"));
                newPeople.setColorHair(rs.getString("COLOR_HAIR"));
                newPeople.setDateEvent(rs.getTimestamp("DATE_EVENT"));
                people.add(newPeople);
            }
        }
        return people;
    }
}
