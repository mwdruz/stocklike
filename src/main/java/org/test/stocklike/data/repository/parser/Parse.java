package org.test.stocklike.data.repository.parser;

import java.util.List;

import org.jsoup.nodes.Document;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.entity.Offer;

import io.vavr.control.Either;

public interface Parse {
    Either<String, List<Category>> parseCategories(Document doc);
    
    Either<String, Integer> parsePagesCount(Document doc);
    
    Either<String, List<Offer>> parseOffers(Document doc);
}
