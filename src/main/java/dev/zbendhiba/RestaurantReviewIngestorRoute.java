package dev.zbendhiba;


import dev.langchain4j.data.document.Document;
import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

@ApplicationScoped
public class RestaurantReviewIngestorRoute extends RouteBuilder {

    @Inject
    RedisEmbeddingStore store;

    @Inject
    EmbeddingModel embeddingModel;

    @Override
    public void configure() throws Exception {

        getContext().getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
        getContext().getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");

        restConfiguration()
                .bindingMode(RestBindingMode.json)
                .componentProperty("lazyStartProducer", "true")
                .dataFormatProperty("autoDiscoverObjectMapper", "true");


        // TODO : Camel ?!?
        var  embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                    .embeddingStore(store)
                    .embeddingModel(embeddingModel)
                    .documentSplitter(recursive(500, 0))
                    .build();

        rest("restaurant")
                .post("/review/")
                .to("direct:process-ingest");

        from("direct:process-ingest")
                .wireTap("direct:convert-to-document")
                .transform().simple("Thanks");

        from("direct:convert-to-document")
                .process(new Langchain4jDocumentProcessor())
                .to("direct:ingestDocument");

        // Embed paragraphs into Vector Database
        from("direct:ingestDocument")
                .process(exchange -> {
                    Document document =exchange.getIn().getBody(Document.class);
                    embeddingStoreIngestor.ingest(document);
                });
    }
}

