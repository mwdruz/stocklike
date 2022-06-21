package org.test.stocklike.data.repository;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.vavr.control.Either;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.GzipSource;
import okio.Okio;

@Component
@Profile("real")
class DownloaderWeb implements Downloader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final OkHttpClient CLIENT;
    
    static {
        var cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieJar cookieJar = new JavaNetCookieJar(cookieManager);
        CLIENT = new OkHttpClient().newBuilder()
                                   .cookieJar(cookieJar)
                                   .addInterceptor(new DecompressingInterceptor())
                                   .build();
    }
    
    public Either<String, Document> download(String url)
    {
        var logger = LogManager.getLogger();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger::info);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Headers headers = DownloaderHeaders.initial();
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            String errorMsg = "Unable to parse URL: " + url;
            LOGGER.error(errorMsg);
            return Left(errorMsg);
        }
        
        Request request = new Request.Builder().url(httpUrl).headers(headers).build();
        
        try (Response response = CLIENT.newCall(request).execute()) {
            return Right(Jsoup.parse(response.body().string()));
        } catch (IOException e) {
            LOGGER.error(e);
            return Left(e.getMessage());
        }
    }
    
    @Override
    public Either<String, List<Document>> downloadPages(String url, int pagesCount,
                                                        IntFunction<String> makePageSuffix)
    {
        List<Document> retList = new ArrayList<>();
        for (int i = 0; i < pagesCount; i++) {
            String uriWithPage = url + makePageSuffix.apply(pagesCount);
            var maybeDoc = download(uriWithPage);
            if (maybeDoc.isLeft()) return Left(maybeDoc.getLeft());
            download(uriWithPage).peek(retList::add);
        }
        return Right(retList);
    }
    
    private static class DecompressingInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException
        {
            var networkResponse = chain.proceed(chain.request());
            var headers = networkResponse.headers();
            if ("gzip".equals(headers.get("Content-Encoding"))) {
                var gzipBuffer = Okio.buffer(new GzipSource(networkResponse.body().source()));
                var responseBuilder = networkResponse.newBuilder();
                var strippedHeaders = networkResponse.headers().newBuilder()
                                                     .removeAll("Content-Encoding")
                                                     .removeAll("Content-Length")
                                                     .build();
                responseBuilder.headers(strippedHeaders);
                var contentType = networkResponse.body().contentType();
                var contentLength = -1L;
                return responseBuilder.body(ResponseBody.create(gzipBuffer,
                                                                contentType,
                                                                contentLength))
                                      .build();
            } else return networkResponse;
        }
    }
}
