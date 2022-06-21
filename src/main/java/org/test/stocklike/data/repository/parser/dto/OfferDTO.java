package org.test.stocklike.data.repository.parser.dto;

import java.util.List;

import org.test.stocklike.domain.entity.Offer;
import org.test.stocklike.domain.entity.Price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OfferDTO(long id,
                       String type,
                       String url,
                       TitleDTO title,
                       PriceDTO price,
                       CategoryPathDTO assortmentCategory,
                       boolean isSponsored) {
    
    public Offer toOffer()
    {
        return new Offer(id, url, title.text, price.toPrice(), assortmentCategory.toCategoryPath());
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record TitleDTO(String text) {}
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record PriceDTO(PriceInnerDTO normal) {
        private Price toPrice() { return new Price(normal.amount, normal.getCurrency()); }
    
        private record PriceInnerDTO(double amount, String currency) {
            private Price.Currency getCurrency() { return Price.Currency.valueOf(currency); }
        }
    }
    
    private record CategoryPathDTO(int id, List<String> path) {
        public List<String> toCategoryPath()
        {
            return path;
        }
    }
}
