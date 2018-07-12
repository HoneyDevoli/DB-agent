import config.AgentData;
import config.DBConnector;
import org.apache.log4j.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

class Main {
    private static final String CLASS_NAME = DBConnector.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    public static void main(String[] args) {
        String agentProp, log4jProp;
        if (args.length < 2) {
            logger.fatal("Invalid arguments. Cannot load properties files");
        } else {
            agentProp = args[0];
            log4jProp = args[1];

            PropertyConfigurator.configure(log4jProp);
            initAgentProperty(agentProp);
        }

        //test
        initAgentProperty("src/main/resources/agent.properties");
        try {
            DBConnector.testConnectionToDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        new Thread() {
            public void run() {
                try {
                    while (true) {
                        System.out.println("Server: " + "kek");
                        AgentData agentData = AgentData.getAgentData();
                        sleep(agentData.getInterval());

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }
    private static void initAgentProperty(String path){
        FileInputStream fis;
        Properties property = new Properties();
        AgentData config = AgentData.getAgentData();
        try {
            fis = new FileInputStream(path);
            property.load(fis);

            config.setInnerDbURL(property.getProperty("db.urlIN"));
            config.setInnerPassword(property.getProperty("db.passwordIN"));
            config.setInnerUser(property.getProperty("db.usernameIN"));

            config.setOuterDbURL(property.getProperty("db.urlEX"));
            config.setOuterUser(property.getProperty("db.usernameEX"));
            config.setOuterPassword(property.getProperty("db.passwordEX"));

            config.setInterval(Integer.parseInt(property.getProperty("interval")));
            config.setIdAgent(property.getProperty("idAgent"));

            try {
                Date startDate =new SimpleDateFormat("dd.MM.yyyy").parse(property.getProperty("startDate"));
                config.setStartDate(startDate);
            } catch (ParseException e) {
                logger.error("Error parse date from config file");
            }
        } catch (FileNotFoundException e) {
            logger.fatal("File agent properties not found",e);
        } catch (IOException e) {
            logger.error(e);
        }
    }
}

