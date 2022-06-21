package org.test.stocklike.data.repository;

import java.util.List;
import java.util.function.IntFunction;

import org.jsoup.nodes.Document;

import io.vavr.control.Either;

interface Downloader {
    Either<String, Document> download(String url);
    
    Either<String, List<Document>> downloadPages(String url,
                                 int pagesCount,
                                 IntFunction<String> makePageSuffix);
}
