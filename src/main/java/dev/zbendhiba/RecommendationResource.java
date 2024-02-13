package dev.zbendhiba;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;


public class RecommendationResource {

    @Inject
    RecommendationService recommendationService;

    @Inject
    RedisEmbeddingStore store;

    @Inject
    EmbeddingModel embeddingModel;

    @Named("embeddingStoreIngestor")
    EmbeddingStoreIngestor  embeddingStoreIngestor() {
        return EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .documentSplitter(recursive(500, 0))
                .build();
    }

}
