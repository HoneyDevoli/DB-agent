package entity;

import java.util.Date;

public class PeopleIN {
    private String firstName;
    private String secondName;
    private String colorHair;
    private Date changedDatetime;
    private String agentId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getColorHair() {
        return colorHair;
    }

    public void setColorHair(String colorHair) {
        this.colorHair = colorHair;
    }

    public Date getChangedDatetime() {
        return changedDatetime;
    }

    public void setChangedDatetime(Date changedDatetime) {
        this.changedDatetime = changedDatetime;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
