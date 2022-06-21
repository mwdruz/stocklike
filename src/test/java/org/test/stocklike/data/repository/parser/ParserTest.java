package org.test.stocklike.data.repository.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import java.util.function.UnaryOperator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.util.Archivist;

class ParserTest {
    private static final short EXPECTED_PAGES_COUNT = 5;
    
    private static final List<Category> EXPECTED_CATEGORIES = List.of(
            new Category("260019", "Karty graficzne", 180,
                         catLinkMaker("/kategoria/podzespoly-komputerowe-karty-graficzne-260019")),
            new Category("2", "Komputery", 283,
                         catLinkMaker("/kategoria/komputery"))
                                                                     );
    private static final short EXPECTED_OFFERS_COUNT = 60;
    
    private static UnaryOperator<String> catLinkMaker(String base)
    {
        return str -> base + "?string=" + str;
    }
    
    @Test
    void parsePages() throws IOException
    {
        // given
        var maybeArchive = Archivist.retrieveIndex();
        if (maybeArchive.isLeft()) fail(maybeArchive.getLeft());
        String webPageServed = maybeArchive.get();
        Document doc = Jsoup.parse(webPageServed);
        Parse parse = new ParseImpl();
        // when
        var parsedPagesCountOrFail = parse.parsePagesCount(doc);
        // then
        int parsedPagesCount = parsedPagesCountOrFail.getOrElse(Assertions::fail);
        
        assertEquals(EXPECTED_PAGES_COUNT, parsedPagesCount);
    }
    
    @Test
    void parseCategories() throws IOException
    {
        // given
        String webPageServed = Archivist.retrieveIndex().get();
        Document doc = Jsoup.parse(webPageServed);
        Parse parse = new ParseImpl();
        // when
        var categoriesOrFail = parse.parseCategories(doc);
        // then
        var categories = categoriesOrFail.getOrElse(Assertions::fail);
        assertEquals(EXPECTED_CATEGORIES, categories);
    }
    
    @Test
    void parseOffers() throws IOException
    {
        // given
        String webPageServed = Archivist.retrieveIndex().get();
        Document doc = Jsoup.parse(webPageServed);
        Parse parse = new ParseImpl();
        // when
        var offersOrFail = parse.parseOffers(doc);
        // then
        var offers = offersOrFail.getOrElse(Assertions::fail);
        assertEquals(EXPECTED_OFFERS_COUNT, offers.size());
    }
}
