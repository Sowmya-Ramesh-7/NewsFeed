package com.newsfeed.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class NewsSourceDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        if (node.isTextual()) {
            return node.asText();
        }if (node.hasNonNull("id")) {
            return node.get("id").asText();
        } else if (node.hasNonNull("name")) {
            return node.get("name").asText();
        } else {
            return null;
        }
    }
}
