package dev.zbendhiba;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.zbendhiba.model.Booking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.apache.camel.model.rest.RestBindingMode;

@ApplicationScoped
public class RestaurantReviewIngestorRoute extends RouteBuilder {

    @Inject
    @Named("embeddingStoreIngestor")
    private EmbeddingStoreIngestor embeddingStoreIngestor;

    @Inject
    RecommendationRestaurant recommendationRestaurant;


    @Override
    public void configure() throws Exception {

        getContext().getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
        getContext().getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");

        restConfiguration()
                .bindingMode(RestBindingMode.json)
                .componentProperty("lazyStartProducer", "true")
                .dataFormatProperty("autoDiscoverObjectMapper", "true");


        rest("restaurant")
                .post("/review/")
                .to("direct:process-ingest");

        from("direct:process-ingest")
                .wireTap("direct:convert-to-document")
                .transform().simple("Thanks");

        from("direct:convert-to-document")
                .bean(RestaurantReviewIngestorBean.class)
                // TODO change this
                .process(exchange -> {
                    Document document =exchange.getIn().getBody(Document.class);
                    embeddingStoreIngestor.ingest(document);
                });


        from("telegram:bots?timeout=30000")
                .log("Text received in Telegram : ${body}")
                // this is just a Hello World, we suppose that we receive only text messages from user
                .filter(simple("${body} != '/start'"))
                    .log("Text to send to user based on response from ChatGPT : ${body}")
                    .process(exchange -> {
                            IncomingMessage incomingMessage = exchange.getMessage().getBody(IncomingMessage.class);
                            exchange.getIn().setBody(recommendationRestaurant.recommend(incomingMessage.getText(), incomingMessage.getFrom().getFirstName()));
                        })
                        .to("telegram:bots")
                        .end();



    }
}

