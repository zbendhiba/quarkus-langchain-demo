package dev.zbendhiba.review.ingestor.model;

public class Booking {
    private String name;
    private String time;
    private int numberOfGuests;


    public Booking() {
    }


    public Booking(String name, String time, int numberOfGuests) {
        this.name = name;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
