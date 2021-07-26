package org.test.stocklike.domain.boundary.dto;

import java.util.ArrayList;
import java.util.List;

public class PricesHgramRequest {
    private String query;
    private List<String> categories;
    private boolean checkNow;
    private boolean checkNew;
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
        
        public Builder setQuery(String str)
        {
            object.query = str;
            return this;
        }
        
        public Builder setCategories(List<String> categories)
        {
            object.categories.addAll(categories);
            return this;
        }
        
        public Builder setCheckNow(Boolean check)
        {
            object.checkNow = check;
            return this;
        }
        
        public Builder setCheckNew(Boolean check)
        {
            object.checkNew = check;
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
    
    public static Builder builder() {return new Builder();}
    
    public String getQuery()
    {
        return query;
    }
    
    public List<String> getCategories()
    {
        return new ArrayList<>(categories);
    }
    
    public boolean isCheckNow()
    {
        return checkNow;
    }
    
    public boolean isCheckNew()
    {
        return checkNew;
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
               ", now=" + checkNow +
               ", new=" + checkNew +
               ", xMin=" + xRangeMin +
               ", xMax=" + xRangeMax +
               ", bin=" + binWidth +
               ')';
    }
}
