package org.test.stocklike.data.repository.parser;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jsoup.nodes.Document;
import org.test.stocklike.data.repository.parser.dto.CategoryDTO;
import org.test.stocklike.domain.entity.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.vavr.collection.LinkedHashSet;
import io.vavr.control.Try;

class ParserCategories extends ParserAbstract<List<Category>> {
    private static final String DATA_BOX_NAME = "Categories";
    
    ParserCategories() { }
    
    @Override
    protected List<Category> process(Document doc)
            throws ParsingException, IOException
    {
        String dataBox = extractDataBox(doc, DATA_BOX_NAME);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode categoriesNode = mapper.readTree(dataBox).get("categories");
        JsonNode suggestedCategoriesNode =
                mapper.readTree(dataBox).get("suggestedCategories").get(0).get("paths");
        ObjectReader reader = mapper.readerFor(new TypeReference<List<CategoryDTO>>() { });
        
        List<CategoryDTO> categoriesList = reader.readValue(categoriesNode);
        List<CategoryDTO> suggestedCategoriesFlatList =
                flattenSuggestedCategories(suggestedCategoriesNode, reader);
        LinkedHashSet<CategoryDTO> extractedCategories =
                LinkedHashSet.ofAll(suggestedCategoriesFlatList)
                             .addAll(categoriesList);
        
        return extractedCategories.toStream()
                                  .map(CategoryDTO::toCategory)
                                  .toJavaList();
    }
    
    private static List<CategoryDTO> flattenSuggestedCategories(JsonNode suggestedCategoriesNode,
                                                                ObjectReader reader)
    {
        Stream<JsonNode> nodesStream =
                StreamSupport.stream(suggestedCategoriesNode.spliterator(), false)
                             .flatMap(node -> StreamSupport.stream(node.get("path").spliterator(),
                                                                   false));
        /*
            Workaround: Can't simply map nodesStream: node -> reader.readValue(node, ...class)
            because readValue can throw an exception, but checked exceptions not allowed in streams
            Try object swallows checked exceptions
            and if supplier in Try::of fails, Try::get rethrows the exception as an unchecked one
        */
        return nodesStream
                .map(node -> Try.of(() -> reader.readValue(node, CategoryDTO.class)))
                .map(Try::get)
                .toList();
    }
}
