import config.PostgreConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class Main {
    public static void main(String[] args) {
        Connection c = PostgreConfig.getDBConnection();
        Statement stmt;
        //PostgreConfig.con();
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE people " +
                    "(date_event     timestamp NOT NULL," +
                    " first_name     text    NOT NULL, " +
                    " is_arrival     boolean NOT NULL, " +
                    " second_name    text NOT NULL, " +
                    " color_hair     text  NOT NULL," +
                    " CONSTRAINT \"people_pkey\" PRIMARY KEY (first_name, second_name))";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
