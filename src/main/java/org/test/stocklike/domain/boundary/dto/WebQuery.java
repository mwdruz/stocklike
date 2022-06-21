package org.test.stocklike.domain.boundary.dto;

public class WebQuery {
    private String query;
    private boolean stateNew = false;
    private boolean typeNow = false;
    
    public static class WebQueryBuilderStarter {
        public WebQueryBuilder setQueryString(String query)
        {
            return new WebQueryBuilder(query);
        }
    }
    
    public static class WebQueryBuilder {
        private final WebQuery built;
        
        public WebQueryBuilder(String query)
        {
            this.built = new WebQuery();
            built.query = query;
        }
        
        public WebQueryBuilder setStateNew(boolean checkNew)
        {
            built.stateNew = checkNew;
            return this;
        }
        
        public WebQueryBuilder setTypeNow(boolean checkNow)
        {
            built.typeNow = checkNow;
            return this;
        }
        
        public WebQuery build()
        {
            return built;
        }
    }
    
    public static WebQueryBuilderStarter builder()
    {
        return new WebQueryBuilderStarter();
    }
    
    public String getQueryString()
    {
        return query;
    }
    
    public boolean isStateNew()
    {
        return stateNew;
    }
    
    public boolean isTypeNow()
    {
        return typeNow;
    }
    
    @Override
    public String toString()
    {
        return "WebQuery(" +
               "query='" + query + '\'' +
               ", stateNew=" + stateNew +
               ", typeNow=" + typeNow +
               ')';
    }
}
