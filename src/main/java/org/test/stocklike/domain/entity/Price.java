package org.test.stocklike.domain.entity;

public class Price {
    public enum Currency {
        PLN,
        EUR,
        USD
    }
    
    private final int intValue;
    private final Currency currency;
    
    public Price(double value, Currency currency)
    {
        if (!valueIsValid(value))
            throw new IllegalArgumentException("not a valid price: " + value);
        this.intValue = priceToInt(value);
        this.currency = currency;
    }
    
    public Price(double value) { this(value, Currency.PLN); }
    
    private int priceToInt(double value)
    {
        StringBuilder retStr = new StringBuilder(String.valueOf(value));
        int dotPosition = retStr.indexOf(".");
        int strLen = retStr.length();
        
        if (dotPosition != -1)
            retStr.deleteCharAt(dotPosition);
        
        if (dotPosition == -1 ||
            dotPosition == strLen - 1)
            retStr.append("00");
        else if (dotPosition == strLen - 2)
            retStr.append("0");
        
        return Integer.parseInt(retStr.toString());
    }
    
    private boolean valueIsValid(double value)
    {
        String priceRegex = "(\\d+|\\d+\\.|\\d*\\.\\d{1,2})";
        String valStr = String.valueOf(value);
        return valStr.matches(priceRegex);
    }
    
    public double getValue() { return intValue / 100.0f; }
    
    public int getIntValue() { return intValue; }
    
    @Override
    public String toString() { return String.format("Price(%.2f %s)", getValue(), currency); }
}
