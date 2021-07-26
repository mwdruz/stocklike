package org.test.stocklike.domain.entity;

import java.util.List;

public class OfferCategory {
    private final List<Integer> path;
    private final String name;
    
    public OfferCategory(List<Integer> path, String name)
    {
        this.path = path;
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getURLPath()
    {
        int lastItem = path.get(path.size() - 1);
        return name + "-" + lastItem;
    }
}
