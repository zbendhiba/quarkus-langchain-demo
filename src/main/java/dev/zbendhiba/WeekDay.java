package dev.zbendhiba;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeekDay {
    @JsonProperty("opens_at")
    private String opens_at;
    @JsonProperty("closes_at")
    private String closes_at;
    @JsonProperty("is_closed")
    private boolean is_closed;

    public WeekDay(){}

    public String getOpens_at() {
        return opens_at;
    }

    public void setOpens_at(String opens_at) {
        this.opens_at = opens_at;
    }

    public String getCloses_at() {
        return closes_at;
    }

    public void setCloses_at(String closes_at) {
        this.closes_at = closes_at;
    }

    public boolean isIs_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }
}
