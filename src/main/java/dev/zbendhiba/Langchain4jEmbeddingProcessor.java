package dev.zbendhiba;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;
import dev.langchain4j.data.segment.TextSegment;
import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@ApplicationScoped
public class Langchain4jEmbeddingProcessor implements Processor {

    /**
     * The embedding store (the database).
     * The bean is provided by the quarkus-langchain4j-redis extension.
     */


    @Override
    public void process(Exchange exchange) throws Exception {

        // extract information from Camel Exchange
        String body = exchange.getIn().getBody(String.class);
        String name = exchange.getIn().getHeader("name", String.class);

        // Create Langchain4j Document from Exchange properties
        Map<String, String> map = new HashMap<>();
        map.put("name", name);

        Metadata metadata = new Metadata(map);
        Document document = new Document(body, metadata);

        // Split
        DocumentSplitter splitter = recursive(50, 0,
                new OpenAiTokenizer(GPT_3_5_TURBO));
        List<TextSegment> segments = splitter.split(document);
        exchange.getIn().setBody(segments);

    }
}
