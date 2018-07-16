import config.AgentData;
import config.DBConnector;
import entity.PeopleEX;
import org.apache.log4j.*;
import repository.PeopleEXRepository;
import repository.PeopleINRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        String agentProp, log4jProp;
        if (args.length < 2) {
            logger.fatal("Invalid arguments. Cannot load properties files");
        } else {
            agentProp = args[0];
            log4jProp = args[1];

            PropertyConfigurator.configure(log4jProp);
            initAgentProperty(agentProp);

            DBConnector.testConnectionToDB();

            checkDate();
            checkDBTask();
        }

        //test load data
        initAgentProperty("src/main/resources/agent.properties");
        DBConnector.testConnectionToDB();

        checkDate();
        checkDBTask();
    }

    private static void initAgentProperty(String path) {
        Properties property = new Properties();
        AgentData config = AgentData.getAgentData();
        try (FileInputStream fis = new FileInputStream(path);){
            property.load(fis);

            config.setInnerDbURL(property.getProperty("db.urlIN"));
            config.setInnerPassword(property.getProperty("db.passwordIN"));
            config.setInnerUser(property.getProperty("db.usernameIN"));

            config.setOuterDbURL(property.getProperty("db.urlEX"));
            config.setOuterUser(property.getProperty("db.usernameEX"));
            config.setOuterPassword(property.getProperty("db.passwordEX"));

            config.setInterval(Integer.parseInt(property.getProperty("interval")));
            config.setIdAgent(property.getProperty("idAgent"));

            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(property.getProperty("startDate"));
            config.setStartDate(startDate);


        } catch (FileNotFoundException e) {
            logger.fatal("File agent properties not found", e);
            System.exit(1);
        } catch (ParseException e) {
            logger.error("Error parse date from config file");
            System.exit(1);
        } catch (IOException e) {
            logger.error(e);
            System.exit(1);
        }
    }

    private static void checkDBTask() {

        AgentData config = AgentData.getAgentData();
        Timer timer = new Timer(false);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                logger.info("Start check database...");

                List <PeopleEX> people= null;
                try {
                    people = PeopleEXRepository.getPeople(config.getStartDate());
                } catch (SQLException e) {
                    logger.error("SQL execute error or external database is unavailable.",e);
                    return;
                }

                try {
                    PeopleINRepository.divisionPeople(people);
                } catch (SQLException e) {
                    logger.error("SQL execute error or internal database is unavailable.",e);
                    //TODO реализовать накопление данных, сохранение полученных людей и замена стартовой датыю
                    return;
                }

                logger.info("Stop check database.\n");
            }
        };
        timer.schedule(task, 0,config.getInterval() * 1000);
    }

    private static void checkDate(){
        AgentData config = AgentData.getAgentData();

        Date lastDateFromDb = null;
        try {
            lastDateFromDb = PeopleINRepository.selectMaxDateForAgent(config.getIdAgent());
        } catch (SQLException e) {
           logger.error(e);
        }

        if (lastDateFromDb != null && lastDateFromDb.after(config.getStartDate())) {
            config.setStartDate(lastDateFromDb);
            logger.info("Set new date from data base file " + config.getStartDate());
        } else {
            logger.info("Start date from properties file " + config.getStartDate());
        }
    }
}




