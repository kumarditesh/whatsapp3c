package com.snapdeal.pears.whatsapp3c.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.snapdeal.pears.whatsapp3c.model.ReplyMedia;

/**
 * Created by ditesh on 2/9/15.
 */
public class MessageService {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        System.out.println("Search");
        System.out.println(getSearchResults("samsung galaxy s duos").toString());
        System.out.println("Order Status");
        System.out.println(getOrderStatus("6829047157", "lokesh.chhaparwal@jasperindia.com").toString());
        System.out.println("Trending Products");
        System.out.println(getTrendingProducts().toString());
    }

    public static List<ReplyMedia> getSearchResults(String keyword) throws IOException {
        List<ReplyMedia> rms = new ArrayList<ReplyMedia>();
        Document doc = Jsoup.connect("http://www.snapdeal.com/search?keyword=" + keyword + "&noOfResults=4").get();
        Elements els = doc.getElementsByClass("productWrapper");
        for (Element el : els) {
            StringBuilder sb = new StringBuilder();
            ReplyMedia rm = new ReplyMedia();
            Elements els1 = el.getElementsByClass("product-price");
            Elements els2 = el.getElementsByClass("product-title");
            Elements els3 = el.getElementsByClass("hoverProductImage");
            String url = "";
            String name = "";
            String price = "";
            String imageUrl = "";
            for (Element el2 : els2) {
                Elements childEls2 = el2.getElementsByAttribute("href");
                url = childEls2.get(0).attr("href");
                name = childEls2.get(0).childNodes().get(0).toString();
            }
            for (Element el3 : els3) {
                Elements childEls3 = el3.getElementsByAttribute("href");
                imageUrl = childEls3.get(0).childNodes().get(1).attributes().get("src");
            }
            for (Element el1 : els1) {
                price = el1.getElementsByAttribute("id").get(0).childNodes().get(0).toString();
            }
            rm.setPath(imageUrl);
            sb.append(name + "\n" + price + "\n" + url);
            rm.setCaption(sb.toString());
            rms.add(rm);
        }
        return rms;
    }

    public static List<ReplyMedia> getOrderStatus(String orderId, String emailId) throws IOException {
        List<ReplyMedia> rms = new ArrayList<ReplyMedia>();
        Document orderStatus = Jsoup.connect("http://www.snapdeal.com/showOrderStatus?orderId=" + orderId + "&searchKey=" + emailId).get();
        Map<String, Object> data = mapper.readValue(orderStatus.body().childNodes().get(0).toString(), Map.class);
        if ("fail".equals(data.get("status"))) {
            ReplyMedia rm = new ReplyMedia();
            rm.setCaption(data.get("message").toString());
            rms.add(rm);
        } else {
            Document doc = Jsoup.connect("http://www.snapdeal.com/" + data.get("message")).get();
            Elements els = doc.getElementsByClass("suborderSummary");
            for (Element el : els) {
                ReplyMedia rm = new ReplyMedia();
                StringBuilder sb = new StringBuilder();
                Elements els1 = el.getElementsByClass("trackingDetails");
                Elements els2 = el.getElementsByClass("subOrdSumDetails");
                String name = els2.get(0).getElementsByClass("subOrdName").get(0).childNodes().get(0).childNodes().get(0).toString().trim();
                String status = "";
                String date = "";
                String imageUrl = el.getElementsByClass("subordSumImage").get(0).getElementsByAttribute("href").get(0).childNodes().get(1).attributes().get("lazySrc");
                for (Element el1 : els1) {
                    if (el1.childNodeSize() > 1) {
                        status = el1.childNodes().get(1).childNodes().get(1).childNodes().get(0).toString();
                        if ("Delivered".equals(status)) {
                            date = el1.childNodes().get(3).childNodes().get(1).childNodes().get(0).toString();
                        }
                        sb.append(status).append("\n").append(name).append("\n").append(date).append("\n");
                    }
                }
                rm.setCaption(sb.toString());
                rm.setPath(imageUrl);
                rms.add(rm);
            }
        }
        return rms;
    }

    public static List<ReplyMedia> getTrendingProducts() throws IOException {
        List<ReplyMedia> rms = new ArrayList<ReplyMedia>();
        Document tp = Jsoup.connect("http://www.snapdeal.com/acors/getTP?start=0&count=4").get();
        List<Map<String, Object>> data = mapper.readValue(tp.body().childNodes().get(0).toString(), List.class);
        for (Map<String, Object> p : data) {
            StringBuilder sb = new StringBuilder();
            ReplyMedia rm = new ReplyMedia();
            String url = p.get("pageUrl").toString();
            url = "http://www.snapdeal.com/" + url;
            String imageUrl = "http://n1.sdlcdn.com/" + p.get("image").toString();
            String price = "Rs " + p.get("displayPrice").toString();
            String name = p.get("name").toString();
            rm.setPath(imageUrl);
            sb.append(name + "\n" + price + "\n" + url);
            rm.setCaption(sb.toString());
            rms.add(rm);
        }
        return rms;
    }
}
