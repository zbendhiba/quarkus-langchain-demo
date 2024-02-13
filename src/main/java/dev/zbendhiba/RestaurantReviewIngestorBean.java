package dev.zbendhiba;

import java.util.HashMap;
import java.util.Map;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.zbendhiba.model.RestaurantReview;
import org.apache.camel.Exchange;

public class RestaurantReviewIngestorBean {

    public Document createDocument(RestaurantReview restaurantReview){
        // Create description Document from Exchange properties
        String text = "Restaurant name is : " + restaurantReview.getName() +", type of cuisine is : "+ restaurantReview.getType() + ", review from a YesRestaurant user : "+ restaurantReview.getReview() + ", is located in Blabla city";
        Map<String, String> map = new HashMap<>();
        map.put("restaurantName", restaurantReview.getName());
        map.put("cusineType", restaurantReview.getType());
        map.put("location",  "Blabla city");
        return new Document(text, new Metadata(map));
    }

}
