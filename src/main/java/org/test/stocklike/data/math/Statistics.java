package org.test.stocklike.data.math;

import java.util.List;

import org.test.stocklike.domain.entity.Hgram;
import org.test.stocklike.domain.entity.Price;

public interface Statistics {
    void loadData(List<Price> prices);
    
    Hgram getHgram(double binWidth);
}
