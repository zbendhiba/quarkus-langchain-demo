package dev.zbendhiba;

import java.util.List;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import static java.time.Duration.ofSeconds;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RestaurantReviewIngestorRoute extends RouteBuilder {

    @ConfigProperty(name="open-api-key")
    String openApiKey;

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

        var  embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                    .embeddingStore(store)
                    .embeddingModel(embeddingModel)
                    .documentSplitter(recursive(500, 0))
                    .build();

        // REST endpoint to add a bio
        rest("data")
                .post("/ingest/")
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


        // TODO delete from here
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(openApiKey)
                .modelName(GPT_3_5_TURBO)
                .temperature(0.3)
                .timeout(ofSeconds(3000))
                .build();


        ConversationalRetrievalChain chain = ConversationalRetrievalChain.builder()
                .chatLanguageModel(model)
                .retriever(EmbeddingStoreRetriever.from(store, embeddingModel))
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .promptTemplate(PromptTemplate
                        .from("Answer the following question to the best of your ability: {{question}}\n\nBase your answer on the following information:\n{{information}} in the mlanguage of the {{question}}"))
                .build();


        from("telegram:bots?timeout=30000")
                .log("Text received in Telegram : ${body}")
                // this is just a Hello World, we suppose that we receive only text messages from user
                .filter(simple("${body} != '/start'"))
                .process(e->{
                    IncomingMessage incomingMessage = e.getMessage().getBody(IncomingMessage.class);
                    var openapiMessage = chain.execute(incomingMessage.getText());
                    e.getMessage().setBody(openapiMessage);

                })
                .log("Text to send to user based on response from ChatGPT : ${body}")
                .to("telegram:bots")
                .end();

    }


}

