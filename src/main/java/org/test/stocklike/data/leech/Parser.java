package org.test.stocklike.data.leech;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.jsoup.Jsoup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Parser {
    public static void main(String[] args) throws IOException, InterruptedException
    {
        final var url = "https://allegro.pl/listing?string=rtx%203090&order=p";
        final var url2 =
                "https://allegro.pl/kategoria/podzespoly-komputerowe-karty-graficzne-260019" +
                "?stan=nowe&offerTypeBuyNow=1&string=rtx3070&order=p";
        
        final var uri = URI.create(url);
        final var request = HttpRequest.newBuilder(uri)
                                       .headers(
                                               "User-Agent",
                                               "Mozilla/5.0 (X11; Linux x86_64; rv:90.0) " +
                                               "Gecko/20100101 Firefox/90.0",
                                               "Accept",
                                               "text/html,application/xhtml+xml,application/xml;" +
                                               "q=0.9,image/webp,*/*;q=0.8",
                                               "Accept-Language", "en-US,en;q=0.5",
                                               "Cookie",
                                               "wdctx=v2" +
                                               ".jgQdrr8qpvt2Xle5AbanH2UjiBQQK19yshSzYkG__7ULk52sUblDZQrh9DYofC5sTwo6n6eAgJux3HJIod_bJKlqZdWzKNgxoeRv0vcp5Vz9t8Sg7woI9MkD27awE2n1MDHD4aHxz7bFiA1RXT34N-h1SrAct4OyWy1566uhYQ; _cmuid=f196f0f4-e6e2-414c-80b6-fe0769ff50c9; datadome=ORLn0wRlZpwcAi.8nj5WodeaKZcUegy0LFVvR0ueGDNvaYar2IFnjuZUX-wlWA~ixVqe5ELgW607cZ-Y2sVRyDQdDO7-lSQamJkZGAVDcB; gdpr_permission_given=1; QXLSESSID=a65765b6f9c27fbb45b5449678e0ee9f2e46fab2030b7b//02; ws3=LFOckrucavv49OwotmxXj1fpMkTnt4U27"
                                               )
                                       .build();
        final var content = HttpClient.newHttpClient()
                                      .send(request, HttpResponse.BodyHandlers.ofString())
                                      .body();
        final var doc = Jsoup.parse(content);
        final var dataBoxId = doc.select("div[data-box-name=items-v3]").attr("data-box-id");
        final var outerJson = doc.select("script[data-serialize-box-id=" + dataBoxId + "]").html();
        
        
        final var mapper = new ObjectMapper();
        final var listingStr = mapper.readTree(outerJson)
                                     .get("__listing_StoreState").asText();
        final var listing = mapper.readTree(listingStr);
        final ArrayNode elementsArray = (ArrayNode) listing.at("/items/elements");

        elementsArray.iterator();
        for (int i = 0; i< elementsArray.size(); i++) {
            String type = elementsArray.get(i).get("type").asText();
            System.out.println(type);
            if (type.equals("label")) {
                elementsArray.remove(i);
            }
        }
    
        for (JsonNode element : elementsArray) {
            System.out.println(element);
        }
        
        final var result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listing);
        try (var stream = new FileOutputStream("result.json")) {
            stream.write(result.getBytes());
        }
    }
}
