import config.InitializerDB;
import config.MysqlConfig;
import config.PostgreConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class Main {
    public static void main(String[] args) {

        PostgreConfig.testConnectionToDB();
        MysqlConfig.testConnectionToDB();

        //Create outer database
        //Connection connection = PostgreConfig.getConnectionToServer();
        //InitializerDB.CreateOuterDbPostgre(connection);

//        Connection connection = MysqlConfig.getConnectionToServer();
//        InitializerDB.CreateOuterDbMySQL(connection);





    }
}
