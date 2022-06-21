package org.test.stocklike.data.repository.parser;

import java.io.IOException;

import org.jsoup.nodes.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class ParserPagesCount extends ParserAbstract<Integer> {
    private static final String DATA_BOX_NAME = "listing";
    
    @Override
    protected Integer process(Document doc) throws ParsingException, IOException
    {
        String dataBox = extractDataBox(doc, DATA_BOX_NAME);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(dataBox);
        JsonNode pagesNode = rootNode.at("/props/searchMeta/lastAvailablePage");
        
        if (!pagesNode.canConvertToInt())
            throw new ParsingException(
                    String.format("can't extract \"lastAvailablePage\" from databox \"%s\"",
                                  DATA_BOX_NAME));
        else return pagesNode.asInt();
    }
}
