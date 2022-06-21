package org.test.stocklike.domain.boundary.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.test.stocklike.domain.entity.Hgram;

public class PricesHgramResponse {
    public enum PricesHgramResponseType {ERROR, CATEGORIES, HGRAM}
    
    private final PricesHgramResponseType type;
    private String message;
    private final List<String> categories = new ArrayList<>();
    private Hgram hgram;
    
    private PricesHgramResponse(List<String> categories)
    {
        type = PricesHgramResponseType.CATEGORIES;
        this.categories.addAll(categories);
    }
    
    private PricesHgramResponse(String errorMessage)
    {
        type = PricesHgramResponseType.ERROR;
        this.message = errorMessage;
    }
    
    public PricesHgramResponse(Hgram hgram)
    {
        type = PricesHgramResponseType.HGRAM;
        this.hgram = hgram;
    }
    
    public static PricesHgramResponse ofCategories(List<String> categoryNames)
    {
        return new PricesHgramResponse(categoryNames);
    }
    
    public static PricesHgramResponse ofError(String message)
    {
        return new PricesHgramResponse(message);
    }
    
    public static PricesHgramResponse ofHgram(Hgram hgram)
    {
        return new PricesHgramResponse(hgram);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricesHgramResponse that = (PricesHgramResponse) o;
        return type == that.type && Objects.equals(message, that.message) &&
               Objects.equals(categories, that.categories) &&
               Objects.equals(hgram, that.hgram);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(type, message, categories, hgram);
    }
    
    public PricesHgramResponseType getType()
    {
        return type;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public List<String> getCategories()
    {
        return new ArrayList<>(categories);
    }
    
    public Hgram getHgram()
    {
        return hgram;
    }
    
    @Override
    public String toString()
    {
        String inside = switch (type) {
            case ERROR -> "message='" + message + '\'';
            case CATEGORIES -> "categories=" + categories;
            case HGRAM -> "hgram=" + hgram;
        };
        return "PricesHgramResponse(" +
               "type=" + type + ", " + inside + ")";
    }
}
