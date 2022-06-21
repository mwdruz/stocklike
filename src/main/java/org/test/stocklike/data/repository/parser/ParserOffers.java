package org.test.stocklike.data.repository.parser;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;
import org.test.stocklike.data.repository.parser.dto.OfferDTO;
import org.test.stocklike.domain.entity.Offer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

class ParserOffers extends ParserAbstract<List<Offer>> {
    private static final String DATA_BOX_NAME = "items-v3";
    
    @Override
    protected List<Offer> process(Document doc) throws ParsingException, IOException
    {
        String dataBox = extractDataBox(doc, DATA_BOX_NAME);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(dataBox);
        JsonNode storeStateNode = rootNode.get("__listing_StoreState");
        JsonNode storeRootNode = mapper.readTree(storeStateNode.asText());
        ObjectReader reader = mapper.readerFor(new TypeReference<List<OfferDTO>>() { });
        
        JsonNode elementsNode = storeRootNode.at("/items/elements");
        List<OfferDTO> offersDTOList = reader.readValue(elementsNode);
        return offersDTOList.stream()
                            .filter(offerDTO -> !offerDTO.type().equals("label"))
                            .filter(offerDTO -> !offerDTO.isSponsored())
                            .map(OfferDTO::toOffer)
                            .toList();
    }
}
