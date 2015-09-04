package com.snapdeal.pears.whatsapp3c.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * Created by ditesh on 2/9/15.
 */
public class MessageService {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        System.out.println("Search");
        System.out.println(getSearchResults("samsung galaxy s duos"));
        System.out.println("Order Status");
        getOrderStatus("6829047157", "lokesh.chhaparwal@jasperindia.com");
        System.out.println("Trending Products");
        getTrendingProducts();
    }

    public static String getSearchResults(String keyword) throws IOException {
        StringBuilder sb = new StringBuilder();
        Document doc = Jsoup.connect("http://www.snapdeal.com/search?keyword=" + keyword + "&noOfResults=4").get();
        Elements els = doc.getElementsByClass("hoverProductWrapper");
        for (Element el : els) {
            Elements els1 = el.getElementsByClass("product-price");
            Elements els2 = el.getElementsByClass("product-title");
            String url = "";
            String name = "";
            String price = "";
            for (Element el2 : els2) {
                Elements childEls2 = el2.getElementsByAttribute("href");
                url = childEls2.get(0).attr("href");
                name = childEls2.get(0).childNodes().get(0).toString();
            }
            for (Element el1 : els1) {
                price = el1.getElementsByAttribute("id").get(0).childNodes().get(0).toString();
            }
            sb.append(url + " " + name + " " + price).append('\n');
        }
        return sb.toString();
    }

    public static String getOrderStatus(String orderId, String emailId) throws IOException {
        StringBuilder sb = new StringBuilder();
        Document orderStatus = Jsoup.connect("http://www.snapdeal.com/showOrderStatus?orderId=" + orderId + "&searchKey=" + emailId).get();
        Map<String, Object> data = mapper.readValue(orderStatus.body().childNodes().get(0).toString(), Map.class);
        if ("fail".equals(data.get("status"))) {
            sb.append(data.get("message"));
        } else {
            Document doc = Jsoup.connect("http://www.snapdeal.com/" + data.get("message")).get();
            Elements els = doc.getElementsByClass("suborderSummary");
            for (Element el : els) {
                Elements els1 = el.getElementsByClass("trackingDetails");
                Elements els2 = el.getElementsByClass("subOrdSumDetails");
                String name = els2.get(0).getElementsByClass("subOrdName").get(0).childNodes().get(0).childNodes().get(0).toString().trim();
                String status = "";
                String date = "";
                for (Element el1 : els1) {
                    if (el1.childNodeSize() > 1) {
                        status = el1.childNodes().get(1).childNodes().get(1).childNodes().get(0).toString();
                        if ("Delivered".equals(status)) {
                            date = el1.childNodes().get(3).childNodes().get(1).childNodes().get(0).toString();
                        }
                        sb.append(name).append(" ").append(status).append(" ").append(date).append('\n');
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String getTrendingProducts() throws IOException {
        StringBuilder sb = new StringBuilder();
        Document tp = Jsoup.connect("http://www.snapdeal.com/acors/getTP?start=0&count=4").get();
        List<Map<String, Object>> data = mapper.readValue(tp.body().childNodes().get(0).toString(), List.class);
        for (Map<String, Object> p : data) {
            String url = p.get("pageUrl").toString();
            url = "http://www.snapdeal.com/" + url;
            String price = p.get("displayPrice").toString();
            String name = p.get("name").toString();
            sb.append(url + " " + name + " " + price).append('\n');
        }
        return sb.toString();
    }
}
