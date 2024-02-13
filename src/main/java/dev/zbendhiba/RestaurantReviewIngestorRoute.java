package dev.zbendhiba;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

@ApplicationScoped
public class RestaurantReviewIngestorRoute extends RouteBuilder {

    @Inject
    @Named("embeddingStoreIngestor")
    private EmbeddingStoreIngestor embeddingStoreIngestor;


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
                .process(exchange -> {
                    Document document =exchange.getIn().getBody(Document.class);
                    embeddingStoreIngestor.ingest(document);
                });
    }
}

