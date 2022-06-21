package org.test.stocklike.data.repository;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.test.stocklike.util.Archivist;

import io.vavr.control.Either;

@Component
@Profile("file")
public class DownloaderFile implements Downloader {
    private static final Logger LOGGER = LogManager.getLogger();
    
    @Override
    public Either<String, Document> download(String url)
    {
        LOGGER.info("Requested page: {}", url);
        try {
            return Archivist.retrieveIndex().map(Jsoup::parse);
        } catch (IOException e) {
            return Left(e.getMessage());
        }
    }
    
    @Override
    public Either<String, List<Document>> downloadPages(String url, int pagesCount,
                                                        IntFunction<String> makePageSuffix)
    {
        LOGGER.info("Requested {} pages starting from: \"{}\"", pagesCount, url);
        try {
            List<Document> docs = new ArrayList<>();
            for (int i = 0; i < pagesCount; i++) {
                var maybeDoc = Archivist.retrieveIndex(i);
                if (maybeDoc.isLeft()) return Left(maybeDoc.getLeft());
                else docs.add(Jsoup.parse(maybeDoc.get()));
            }
            return Right(docs);
        } catch (IOException e) {
            return Left(e.getMessage());
        }
    }
}
