package dev.zbendhiba;

import java.util.HashMap;
import java.util.Map;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@ApplicationScoped
public class Langchain4jDocumentProcessor implements Processor {

    /**
     * The embedding store (the database).
     * The bean is provided by the quarkus-langchain4j-redis extension.
     */


    @Override
    public void process(Exchange exchange) throws Exception {

        // extract information from Camel Exchange
        RestaurantReview body = exchange.getIn().getBody(RestaurantReview.class);

        // Create description Document from Exchange properties
        String text = "Restaurant name is : " + body.getName() +", type of cuisine is : "+ body.getType() + ", review from a YesRestaurant user : "+ body.getReview() + ", is located in Blabla city";
        Document document = new Document(text, createMetadata(body));
        exchange.getIn().setBody(document);

    }

    private Metadata createMetadata(RestaurantReview review){
        Map<String, String> map = new HashMap<>();
        map.put("restaurantName", review.getName());
        map.put("cusineType", review.getType());
        map.put("location",  "Blabla city");
        return new Metadata(map);
    }
}
