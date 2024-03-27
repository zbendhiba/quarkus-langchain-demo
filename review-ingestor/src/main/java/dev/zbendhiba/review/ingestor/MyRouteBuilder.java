package dev.zbendhiba.review.ingestor;

import dev.langchain4j.data.document.Document;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class MyRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:hello")
                .log("hello world!");
        // Enable Jackson Type Converter
        getContext().getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
        getContext().getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");

        // Configure REST endpoint
        restConfiguration()
                .bindingMode(RestBindingMode.json)
                .component("undertow").host("0.0.0.0").port(8082)

                .componentProperty("lazyStartProducer", "true")
                .dataFormatProperty("autoDiscoverObjectMapper", "true");

        // Define REST endpoint
        rest("restaurant")
                .post("/review/")
                .to("direct:process-ingest");

        // Define route
        from("direct:process-ingest")
                 .wireTap("direct:convert-to-document")
                .transform().simple("Thanks");

        from("direct:convert-to-document")
                .bean(RestaurantReviewIngestorBean.class);
                // TODO change this
                /*.process(exchange -> {
                    Document document =exchange.getIn().getBody(Document.class);
                    embeddingStoreIngestor.ingest(document);
                });*/

    }
}
