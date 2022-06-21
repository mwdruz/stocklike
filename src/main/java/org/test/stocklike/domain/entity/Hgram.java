package org.test.stocklike.domain.entity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import io.vavr.Tuple2;

public class Hgram {
    private final Map<Double, Integer> hgramMap = new TreeMap<>();
    private final double binWidth;
    
    public Hgram(double binWidth) { this.binWidth = binWidth; }
    
    public static Hgram fromList(double binWidth, List<Tuple2<Double, Integer>> dataTupleList)
    {
        var builder = Hgram.builder(binWidth);
        dataTupleList.forEach(
                tuple -> builder.addEntry(tuple._1, tuple._2)
                             );
        return builder.build();
    }
    
    public List<Tuple2<String, Integer>> getLabeledList()
    {
        return hgramMap.entrySet()
                       .stream()
                       .map(
                               entry -> new Tuple2<>(String.valueOf(entry.getKey()),
                                                     entry.getValue()))
                       .toList();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hgram hgram = (Hgram) o;
        return Double.compare(hgram.binWidth, binWidth) == 0 &&
               hgramMap.equals(hgram.hgramMap);
    }
    
    @Override
    public int hashCode() { return Objects.hash(hgramMap, binWidth); }
    
    public double getBinWidth() { return binWidth; }
    
    private static class HgramBuilder {
        private final Hgram hgram;
        
        private HgramBuilder(double binWidth) { hgram = new Hgram(binWidth); }
        
        private HgramBuilder addEntry(double bin, int count)
        {
            hgram.hgramMap.put(bin, count);
            return this;
        }
        
        private Hgram build() { return hgram; }
    }
    
    private static HgramBuilder builder(double binWidth) { return new HgramBuilder(binWidth); }
    
    @Override
    public String toString() { return "Hgram(" + binWidth + ", " + hgramMap + ')'; }
}
