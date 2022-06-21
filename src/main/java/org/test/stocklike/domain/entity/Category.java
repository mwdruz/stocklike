package org.test.stocklike.domain.entity;

import java.util.Objects;
import java.util.function.UnaryOperator;

public record Category(String id, String name, int count, UnaryOperator<String> linkMaker) {
    public String getQueryLink(String queryString) { return linkMaker.apply(queryString); }
    
    @Override
    public String toString()
    {
        return String.format("Category([%s], %s: %d, str->%s)",
                             id, name, count, linkMaker.apply("str"));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return count == category.count && Objects.equals(id, category.id) &&
               Objects.equals(name, category.name) &&
               Objects.equals(linkMaker.apply("exampleString"),
                              category.linkMaker.apply("exampleString"));
    }
    
    @Override
    public int hashCode() { return Objects.hash(id, name, count, linkMaker); }
}
