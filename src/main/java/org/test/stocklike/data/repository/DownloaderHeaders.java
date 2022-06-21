package org.test.stocklike.data.repository;

import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;

import okhttp3.Headers;

class DownloaderHeaders {
    private static final String ACCEPT_KEY = "Accept";
    private static final String ACCEPT_VALUE =
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8";
    private static final String ACCEPT_ENCODING_KEY = "Accept-Encoding";
    private static final String ACCEPT_ENCODING_VALUE = "gzip, deflate, br";
    private static final String ACCEPT_LANGUAGE_KEY = "Accept-Language";
    private static final String ACCEPT_LANGUAGE_VALUE = "en-US,en;q=0.5";
    private static final String CACHE_CONTROL_KEY = "Cache-Control";
    private static final String CACHE_CONTROL_VALUE = "no-cache";
    private static final String CONNECTION_KEY = "Connection";
    private static final String CONNECTION_VALUE = "keep-alive";
    private static final String COOKIE_KEY = "Cookie";
    private static final String DNT_KEY = "DNT";
    private static final String DNT_VALUE = "1";
    private static final String HOST_KEY = "Host";
    private static final String HOST_VALUE = "allegro.pl";
    private static final String REFERER_KEY = "Referer";
    
    private static final String SEC_FETCH_DEST_KEY = "Sec-Fetch-Dest";
    private static final String SEC_FETCH_DEST_VALUE = "document";
    private static final String SEC_FETCH_MODE_KEY = "Sec-Fetch-Mode";
    private static final String SEC_FETCH_MODE_VALUE = "navigate";
    private static final String SEC_FETCH_SITE_KEY = "Sec-Fetch-Site";
    private static final String SEC_FETCH_SITE_VALUE_NONE = "none";
    private static final String SEC_FETCH_SITE_VALUE_SAME_ORIGIN = "same-origin";
    private static final String SEC_FETCH_USER_KEY = "Sec-Fetch-User";
    private static final String SEC_FETCH_USER_VALUE = "?1";
    
    private static final String TE_KEY = "TE";
    private static final String TE_VALUE = "trailers";
    private static final String UPGRADE_INSECURE_REQUESTS_KEY = "Upgrade-Insecure-Requests";
    private static final String UPGRADE_INSECURE_REQUESTS_VALUE = "1";
    private static final String USER_AGENT_KEY = "User-Agent";
    private static final String USER_AGENT_VALUE =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:98.0) Gecko/20100101 Firefox/98.0";
    
    public static final BiFunction<String, String, AbstractMap.SimpleEntry<String, String>>
            entryMaker = AbstractMap.SimpleEntry::new;
    
    private static final Map<String, String> initialHeaderMap =
            new TreeMap<>(Map.ofEntries(entryMaker.apply(ACCEPT_KEY, ACCEPT_VALUE),
                                        entryMaker.apply(ACCEPT_ENCODING_KEY,
                                                         ACCEPT_ENCODING_VALUE),
                                        entryMaker.apply(ACCEPT_LANGUAGE_KEY,
                                                         ACCEPT_LANGUAGE_VALUE),
                                        entryMaker.apply(DNT_KEY, DNT_VALUE),
                                        entryMaker.apply(CONNECTION_KEY, CONNECTION_VALUE),
                                        entryMaker.apply(SEC_FETCH_DEST_KEY, SEC_FETCH_DEST_VALUE),
                                        entryMaker.apply(SEC_FETCH_MODE_KEY, SEC_FETCH_MODE_VALUE),
                                        entryMaker.apply(SEC_FETCH_SITE_KEY,
                                                         SEC_FETCH_SITE_VALUE_NONE),
                                        entryMaker.apply(SEC_FETCH_USER_KEY, SEC_FETCH_USER_VALUE),
                                        entryMaker.apply(UPGRADE_INSECURE_REQUESTS_KEY,
                                                         UPGRADE_INSECURE_REQUESTS_VALUE),
                                        entryMaker.apply(USER_AGENT_KEY, USER_AGENT_VALUE)
                                       ));
    
    public static Headers initial()
    {
        return Headers.of(initialHeaderMap);
    }
    
    private DownloaderHeaders() { }
}
