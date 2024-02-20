package dev.zbendhiba;

import dev.langchain4j.agent.tool.Tool;
import jakarta.inject.Singleton;
@Singleton
public class RestaurantBooking {

    @Tool("book a restaurant for a user at a time for a number of guests")
    public void bookRestaurant(String userName, String restaurantName, String time, int numberOfGuests){
        System.out.println(String.format("**************************************** Booking %s for %s at %s, nb of guests is %s", restaurantName, userName, time, numberOfGuests));
    }
}
