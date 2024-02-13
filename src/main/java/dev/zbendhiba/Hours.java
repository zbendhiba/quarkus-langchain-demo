package dev.zbendhiba;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hours {
    @JsonProperty("monday")

    private WeekDay monday;
    @JsonProperty("tuesday")
    private WeekDay tuesday;
    @JsonProperty("wednesday")
    private WeekDay wednesday;
    @JsonProperty("thursday")
    private WeekDay thursday;
    @JsonProperty("friday")
    private WeekDay friday;
    @JsonProperty("saturday")
    private WeekDay saturday;
    @JsonProperty("sunday")
    private WeekDay sunday;

    public Hours() {
    }

    public WeekDay getMonday() {
        return monday;
    }

    public void setMonday(WeekDay monday) {
        this.monday = monday;
    }

    public WeekDay getTuesday() {
        return tuesday;
    }

    public void setTuesday(WeekDay tuesday) {
        this.tuesday = tuesday;
    }

    public WeekDay getWednesday() {
        return wednesday;
    }

    public void setWednesday(WeekDay wednesday) {
        this.wednesday = wednesday;
    }

    public WeekDay getThursday() {
        return thursday;
    }

    public void setThursday(WeekDay thursday) {
        this.thursday = thursday;
    }

    public WeekDay getFriday() {
        return friday;
    }

    public void setFriday(WeekDay friday) {
        this.friday = friday;
    }

    public WeekDay getSaturday() {
        return saturday;
    }

    public void setSaturday(WeekDay saturday) {
        this.saturday = saturday;
    }

    public WeekDay getSunday() {
        return sunday;
    }

    public void setSunday(WeekDay sunday) {
        this.sunday = sunday;
    }
}
