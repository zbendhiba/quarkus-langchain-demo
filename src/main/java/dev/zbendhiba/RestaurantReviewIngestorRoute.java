package dev.zbendhiba;

import java.util.List;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class RestaurantReviewIngestorRoute extends RouteBuilder {

    @Inject
    RedisEmbeddingStore store;

    /**
     * The embedding model (how the vector of a document is computed).
     * The bean is provided by the LLM (like openai) extension.
     */
    @Inject
    EmbeddingModel embeddingModel;
    @Override
    public void configure() throws Exception {

        // temporary Routes

        // REST endpoint to add a bio
        rest("data")
                .post("/langchain4j-split-ingest/")
                .to("direct:langchain4j-split-ingest");

        from("direct:langchain4j-split-ingest")
                .wireTap("direct:process-langchain4j-split-ingest")
                .transform().simple("Thanks");

        from("direct:process-langchain4j-split-ingest")
                .process(new Langchain4jEmbeddingProcessor())
                .to("direct:processTokenizedPart");

        // Embed paragraphs into Vector Database
        from("direct:processTokenizedPart")
                .log("youpi")
                .process(exchange -> {
                    embed(exchange.getIn().getBody(List.class));
                });



    }

    public void embed(List<TextSegment> textSegments )  {
        List<Embedding> embeddings = embeddingModel.embedAll(textSegments).content();
        store.addAll(embeddings, textSegments);
    }
}

