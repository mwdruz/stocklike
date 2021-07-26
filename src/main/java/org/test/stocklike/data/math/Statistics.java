package org.test.stocklike.data.math;

import java.util.List;
import java.util.Map;

import org.test.stocklike.domain.entity.OfferPrice;

public interface Statistics {
    void loadData(List<OfferPrice> offerPrices);
    
    Map<String, Double> getHgram(double binWidth);
}
