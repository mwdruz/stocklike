package org.test.stocklike.domain.boundary.dto;

public class OffersHgramRequest {
    private String query;
    private boolean checkNow;
    private boolean checkNew;
    private double xRangeMin;
    private double xRangeMax;
    private double yRangeMin;
    private double yRangeMax;
    private double binWidth;

    public static class Builder {
        private final OffersHgramRequest object;
        private Builder() {
            object = new OffersHgramRequest();
        }

        public OffersHgramRequest build() {
            return object;
        }

        public Builder setQuery(String str) {
            object.query = str;
            return this;
        }
    
        public Builder setCheckNow(Boolean check) {
            object.checkNow = check;
            return this;
        }
    
        public Builder setCheckNew(Boolean check) {
            object.checkNew = check;
            return this;
        }
    
        public Builder setXRangeMin(double d) {
            object.xRangeMin = d;
            return this;
        }
    
        public Builder setXRangeMax(double d) {
            object.xRangeMax = d;
            return this;
        }
    
        public Builder setYRangeMin(double d) {
            object.yRangeMin = d;
            return this;
        }
    
        public Builder setYRangeMax(double d) {
            object.yRangeMax = d;
            return this;
        }
    
        public Builder setBinWidth(double d) {
            object.binWidth = d;
            return this;
        }

    }

    public static Builder builder()  { return new Builder(); }

    public String getQuery() {
        return query;
    }

    public boolean isCheckNow() {
        return checkNow;
    }

    public boolean isCheckNew() {
        return checkNew;
    }

    public double getXRangeMin() {
        return xRangeMin;
    }

    public double getXRangeMax() {
        return xRangeMax;
    }

    public double getYRangeMin() {
        return yRangeMin;
    }

    public double getYRangeMax() {
        return yRangeMax;
    }

    public double getBinWidth() {
        return binWidth;
    }
}
