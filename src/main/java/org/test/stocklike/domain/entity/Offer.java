package org.test.stocklike.domain.entity;

public class Offer {
    private final int offerId;
    private final String url;
    private final String title;
    private final OfferPrice offerPrice;
    private final OfferCategory offerCategory;
    
    public Offer(int offerId, String url, String title, OfferPrice offerPrice,
                 OfferCategory offerCategory)
    {
        this.offerId = offerId;
        this.url = url;
        this.title = title;
        this.offerPrice = offerPrice;
        this.offerCategory = offerCategory;
    }
    
    public int getOfferId()
    {
        return offerId;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public OfferPrice getOfferPrice()
    {
        return offerPrice;
    }
    
    public OfferCategory getOfferCategory()
    {
        return offerCategory;
    }
}
