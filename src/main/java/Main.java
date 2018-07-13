import config.AgentData;
import config.DBConnector;
import entity.PeopleEX;
import entity.PeopleIN;
import org.apache.log4j.*;
import repository.PeopleEXRepository;
import repository.PeopleINRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static java.lang.System.exit;

class Main {
    private static final String CLASS_NAME = Main.class.getName();
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

        //test load data
        initAgentProperty("src/main/resources/agent.properties");
        try {
            DBConnector.testConnectionToDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //test changes date from db
        AgentData config = AgentData.getAgentData();

        Date lastDateFromDb = PeopleINRepository.selectMaxDateForAgent(config.getIdAgent());
        if(lastDateFromDb != null && lastDateFromDb.before(config.getStartDate())) {
            config.setStartDate(config.getStartDate());
        } else {
            config.setStartDate(lastDateFromDb);
        }


        while (true) {
            try {
                logger.info("Start check database");
                    ArrayList<PeopleEX> people = PeopleEXRepository.getPeople(AgentData.getAgentData().getStartDate());
                    for(PeopleEX man : people) {
                        if(man.isArrival()){
                            if(PeopleINRepository.checkPeople(man.getFirstName(),man.getSecondName(),config.getIdAgent())){
                                PeopleINRepository.updPeople(man.getFirstName(),man.getSecondName(),config.getIdAgent(),man.getColorHair(),man.getDateEvent());
                            } else {
                                PeopleINRepository.addPeople(man.getFirstName(),man.getSecondName(),config.getIdAgent(),man.getColorHair(),man.getDateEvent());
                            }
                        } else{
                            PeopleINRepository.delPeople(man.getFirstName(),man.getSecondName(),config.getIdAgent());
                        }
                    }
                    //logger.info(PeopleINRepository.checkPeople("EMMA","STONE", "ASD"));
                    //PeopleINRepository.addPeople("Lilly","Abdulina","ASD","Black",Date.from(Instant.now()));
                    logger.info("Set new date:");
                    config.setStartDate(PeopleINRepository.selectMaxDateForAgent("ASD"));


                Thread.sleep(config.getInterval() * 1000);
                //logger.info("kek");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
                Date startDate =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(property.getProperty("startDate"));
                config.setStartDate(startDate);
            } catch (ParseException e) {
                logger.error("Error parse date from config file");
            }
        } catch (FileNotFoundException e) {
            logger.fatal("File agent properties not found",e);
            System.exit(0);
        } catch (IOException e) {
            logger.error(e);
        }
    }
}

