package entity;

import java.util.Date;

public class PeopleEX {
    private String firstName;
    private String secondName;
    private String colorHair;
    private boolean isArrival;
    private Date dateEvent;

    public PeopleEX(){}


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

    public boolean isArrival() {
        return isArrival;
    }

    public void setArrival(boolean arrival) {
        isArrival = arrival;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }
}
