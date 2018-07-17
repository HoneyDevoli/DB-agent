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
import java.util.stream.Collectors;

class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    private static Map<String,PeopleEX> peopleOld = new HashMap<>();

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

        //test load data without data from args.
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
            logger.fatal("Error parse date from config file");
            System.exit(1);
        } catch (IOException e) {
            logger.fatal(e);
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

                List <PeopleEX> peopleNew= null;
                try {
                    peopleNew = PeopleEXRepository.getPeople(config.getStartDate());
                } catch (SQLException e) {
                    logger.error("SQL execute error or external database is unavailable.",e);
                    return;
                }

                try {
                    if(peopleOld.size() > 0) {
                        if(peopleNew.size()>0) {
                            peopleOld = appendPeople(peopleOld, peopleNew);
                        }
                        PeopleINRepository.divisionPeople(new ArrayList<>(peopleOld.values()));
                        peopleOld.clear();
                    } else {
                        PeopleINRepository.divisionPeople(peopleNew);
                    }
                } catch (SQLException e) {
                    logger.error("SQL execute error or internal database is unavailable.",e);
                    peopleOld = appendPeople(peopleOld,peopleNew);
                    config.setStartDate(peopleNew.stream().map(man -> man.getDateEvent()).max(Date::compareTo).get());
                    return;
                }

                logger.info("Stop check database.\n");
            }
        };
        timer.schedule(task, 0,config.getInterval() * 1000);
    }

    private static Map<String,PeopleEX> appendPeople(Map<String,PeopleEX> peopleOld, List<PeopleEX> peopleNew) {
        if(peopleOld.size() == 0){
            return  convertListToMap(peopleNew);
        } else {
            for(PeopleEX man : peopleNew){
                String key = man.getFirstName()+man.getSecondName();
                if(peopleOld.containsKey(key)){
                    peopleOld.remove(key);
                    peopleOld.put(key,man);
                } else {
                    peopleOld.put(key,man);
                }
            }
            return peopleOld;
        }
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
            logger.debug("Set new date from data base file " + config.getStartDate());
        } else {
            logger.debug("Start date from properties file " + config.getStartDate());
        }
    }

    private static Map<String,PeopleEX> convertListToMap(List<PeopleEX> people){
        return people.stream().collect(
                Collectors.toMap(e -> e.getFirstName() + e.getSecondName(), e -> e));
    }
}




