package org.test.stocklike.domain.entity;

import java.util.List;

public record Offer(long id, String url, String title, Price price, List<String> categoryPath) { }
