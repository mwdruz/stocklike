package org.test.stocklike.data.math;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;
import org.test.stocklike.domain.entity.OfferPrice;

@Component
public class StatisticsImpl implements Statistics {
    private List<Integer> pricesSeries;
    
    @Override
    public void loadData(List<OfferPrice> offerPrices)
    {
        pricesSeries = offerPrices.stream()
                                  .map(price -> Math.round(price.getValue() * 100))
                                  .toList();
    }
    
    @Override
    public Map<String, Double> getHgram(double binWidth)
    {
        LogManager.getLogger().info("creating histogram");
        
        return new TreeMap<>(Map.of("a", 2.0,
                                    "b", 5.0,
                                    "c", 1.0));
    }
}
