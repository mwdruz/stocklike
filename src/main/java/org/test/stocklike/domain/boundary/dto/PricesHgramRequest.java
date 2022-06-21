package org.test.stocklike.domain.boundary.dto;

import java.util.ArrayList;
import java.util.List;

public class PricesHgramRequest {
    private WebQuery query;
    private List<String> categories;
    private double xRangeMin;
    private double xRangeMax;
    private double binWidth;
    
    public static class Builder {
        private final PricesHgramRequest object;
        
        private Builder()
        {
            object = new PricesHgramRequest();
            object.categories = new ArrayList<>();
        }
        
        public PricesHgramRequest build()
        {
            return object;
        }
        
        public Builder setQuery(WebQuery query)
        {
            object.query = query;
            return this;
        }
        
        public Builder setCategories(List<String> categories)
        {
            object.categories.addAll(categories);
            return this;
        }
        
        public Builder setXRangeMin(double d)
        {
            object.xRangeMin = d;
            return this;
        }
        
        public Builder setXRangeMax(double d)
        {
            object.xRangeMax = d;
            return this;
        }
        
        public Builder setBinWidth(double d)
        {
            object.binWidth = d;
            return this;
        }
    }
    
    public static Builder builder() { return new Builder(); }
    
    public WebQuery getQuery()
    {
        return query;
    }
    
    public List<String> getCategories()
    {
        return new ArrayList<>(categories);
    }
    
    public double getXRangeMin()
    {
        return xRangeMin;
    }
    
    public double getXRangeMax()
    {
        return xRangeMax;
    }
    
    public double getBinWidth()
    {
        return binWidth;
    }
    
    @Override
    public String toString()
    {
        return "PricesHgramRequest(" +
               "query='" + query + '\'' +
               ", categories=" + categories +
               ", xMin=" + xRangeMin +
               ", xMax=" + xRangeMax +
               ", bin=" + binWidth +
               ')';
    }
}
