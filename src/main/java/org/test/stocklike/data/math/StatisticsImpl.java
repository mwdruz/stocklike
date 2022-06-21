package org.test.stocklike.data.math;

import static io.vavr.API.Tuple;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;
import org.test.stocklike.domain.entity.Hgram;
import org.test.stocklike.domain.entity.Price;

import io.vavr.Tuple2;

@Component
public final class StatisticsImpl implements Statistics {
    private List<Double> priceSeries;
    
    @Override
    public void loadData(List<Price> prices)
    {
        priceSeries = prices.stream()
                            .map(Price::getValue)
                            .sorted()
                            .toList();
    }
    
    @Override
    public Hgram getHgram(double binWidth)
    {
        if (priceSeries.isEmpty()) return Hgram.fromList(binWidth, List.of());
        
        final double minPrice = priceSeries.get(0);
        final double maxPrice = priceSeries.get(priceSeries.size() - 1);
        final double minBin = Math.floor(minPrice);
        if (binWidth <= 0) binWidth = binWidthSuggestion(minPrice, maxPrice);
        LogManager.getLogger().info("creating histogram with bin width: {}", binWidth);
        
        double maxBin = minBin;
        int numberOfBins = 1;
        while (maxBin + binWidth <= maxPrice) {
            maxBin += binWidth;
            numberOfBins += 1;
        }
        
        double[] binsArray = new double[numberOfBins];
        int[] counterArray = new int[numberOfBins];
        int currentBinIndex = 0;
        binsArray[0] = minBin;
        for (double price : priceSeries) {
            while (price >= binsArray[currentBinIndex] + binWidth) {
                binsArray[currentBinIndex + 1] = binsArray[currentBinIndex] + binWidth;
                currentBinIndex += 1;
            }
            counterArray[currentBinIndex] += 1;
        }
        
        List<Tuple2<Double, Integer>> listOfTuples = new ArrayList<>();
        currentBinIndex = 0;
        while (currentBinIndex < numberOfBins) {
            listOfTuples.add(Tuple(binsArray[currentBinIndex],
                                   counterArray[currentBinIndex]));
            currentBinIndex += 1;
        }
        return Hgram.fromList(binWidth, listOfTuples);
    }
    
    private double binWidthSuggestion(double minPrice, double maxPrice)
    {
        double minBin = Math.floor(minPrice);
        double maxBin = Math.ceil(maxPrice);
        LogManager.getLogger().info("minB: {}, maxB: {}", minBin, maxBin);
// ???  if (maxPrice == maxBin) maxBin += 1;
        if (maxBin - minBin <= 10) return 1.0;
        else return (maxBin - minBin) / 10;
    }
}
