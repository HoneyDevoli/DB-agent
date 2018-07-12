package config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgentData {

    private static final AgentData agentData = new AgentData();

    private String innerDbURL;
    private String innerUser;
    private String innerPassword;

    private String outerDbURL;
    private String outerUser;
    private String outerPassword;

    private int interval;
    private Date startDate;
    private String idAgent;

    private  AgentData(){}

    public static AgentData getAgentData(){
        return agentData;
    }


    public String getInnerUser() {
        return innerUser;
    }

    public void setInnerUser(String innerUser) {
        this.innerUser = innerUser;
    }

    public String getInnerPassword() {
        return innerPassword;
    }

    public void setInnerPassword(String innerPassword) {
        this.innerPassword = innerPassword;
    }

    public String getOuterDbURL() {
        return outerDbURL;
    }

    public void setOuterDbURL(String outerDbURL) {
        this.outerDbURL = outerDbURL;
    }

    public String getOuterUser() {
        return outerUser;
    }

    public void setOuterUser(String outerUser) {
        this.outerUser = outerUser;
    }

    public String getOuterPassword() {
        return outerPassword;
    }

    public void setOuterPassword(String outerPass) {
        this.outerPassword = outerPass;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(String idAgent) {
        this.idAgent = idAgent;
    }

    public String getInnerDbURL() {
        return innerDbURL;
    }

    public void setInnerDbURL(String innerDbURL) {
        this.innerDbURL = innerDbURL;
    }
}
