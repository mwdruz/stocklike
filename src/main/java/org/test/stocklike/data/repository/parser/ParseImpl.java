package org.test.stocklike.data.repository.parser;

import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.entity.Offer;

import io.vavr.control.Either;

@Component
public class ParseImpl implements Parse {
    private int cachedCategoriesHash;
    private Either<String, List<Category>> cachedCategories = null;
    private int cachedPagesHash;
    private Either<String, Integer> cachedPages = null;
    private int cachedOffersHash;
    private Either<String, List<Offer>> cachedOffers = null;
    
    @Override
    public Either<String, List<Category>> parseCategories(Document doc)
    {
        if (doc.outerHtml().hashCode() != cachedCategoriesHash)
            cachedCategories = new ParserCategories().parse(doc);
        cachedCategoriesHash = doc.outerHtml().hashCode();
        return cachedCategories;
    }
    
    @Override
    public Either<String, Integer> parsePagesCount(Document doc)
    {
        if (doc.outerHtml().hashCode() != cachedPagesHash)
            cachedPages = new ParserPagesCount().parse(doc);
        cachedPagesHash = doc.outerHtml().hashCode();
        return cachedPages;
    }
    
    @Override
    public Either<String, List<Offer>> parseOffers(Document doc)
    {
        if (doc.outerHtml().hashCode() != cachedOffersHash)
            cachedOffers = new ParserOffers().parse(doc);
        cachedOffersHash = doc.hashCode();
        return cachedOffers;
    }
}
