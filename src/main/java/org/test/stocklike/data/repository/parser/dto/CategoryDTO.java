package org.test.stocklike.data.repository.parser.dto;

import java.util.function.UnaryOperator;

import org.test.stocklike.domain.entity.Category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CategoryDTO(String id,
                          String name,
                          int count,
                          String link) {
    
    public Category toCategory()
    {
        return new Category(id, name, count, createLinkMaker(link));
    }
    
    private UnaryOperator<String> createLinkMaker(String link)
    {
        int indexOfEq = link.lastIndexOf("=");
        return str -> link.substring(0, indexOfEq + 1) + str;
    }
}
