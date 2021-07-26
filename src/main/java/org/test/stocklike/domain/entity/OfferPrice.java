package org.test.stocklike.domain.entity;

public class OfferPrice {
    public enum Currency {
        PLN,
        EUR,
        USD
    }
    
    private final int value;
    private final Currency currency;
    
    public OfferPrice(int value, Currency currency)
    {
        this.value = value;
        this.currency = currency;
    }
    
    public OfferPrice(int value)
    {
        this(value, Currency.PLN);
    }
    
    public float getValue()
    {
        return value / 10.0f;
    }
    
    @Override
    public String toString()
    {
        return "OfferPrice(" + getValue() + " " + currency + ")";
    }
}
