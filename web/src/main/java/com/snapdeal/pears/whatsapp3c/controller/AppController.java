package com.snapdeal.pears.whatsapp3c.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.snapdeal.pears.whatsapp3c.constants.Commands;
import com.snapdeal.pears.whatsapp3c.model.Reply;
import com.snapdeal.pears.whatsapp3c.requestresponse.ConversationList;
import com.snapdeal.pears.whatsapp3c.service.MessageService;

/**
 * Created by ditesh on 2/9/15.
 */
@Controller
public class AppController {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Gson         gson   = new Gson();

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET, produces = "text/html")
    public @ResponseBody
    String createRequestPage() {
        return "Health OK";
    }

    //TODO ditesh
    @RequestMapping(value = "/getUnreadConversations", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ConversationList getUnreadConversations() {
        ConversationList list = new ConversationList();
        list.addConversation(new ConversationList.Conversation("12345", "Hi there", 999999l));
        return list;
    }

    @RequestMapping(value = "/processMessage", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public void processMessage(@RequestBody String input) throws IOException {
        Map<String, Object> data = mapper.readValue(input, Map.class);
        String message = data.get("message").toString();
        String caller = data.get("caller").toString();
        String messageId = data.get("messageid").toString();
        String toReturn = "Wassup. Welcome to Snapdeal.";
        message = message.trim().toLowerCase();
        if (getCommands().contains(message.split(":")[0].trim())) {
            String command = message.split(":")[0].trim();
            if (Commands.order.name().equals(command)) {
                String orderId = message.split(":")[1].trim().split(",")[0];
                String emailId = message.split(":")[1].trim().split(",")[1];
                toReturn = MessageService.getOrderStatus(orderId, emailId);
            }
            if (Commands.search.name().equals(command)) {
                String keyword = message.split(":")[1].trim();
                toReturn = MessageService.getSearchResults(keyword);
            }
            if (Commands.trending.name().equals(command)) {
                toReturn = MessageService.getTrendingProducts();
            }
        }
        Reply reply = new Reply();
        reply.setMessage(toReturn);
        reply.setNumber(caller);
        sendReply(gson.toJson(reply));
    }

    public List<String> getCommands() {
        List<String> commands = new ArrayList<String>();
        for (Commands c : Commands.values()) {
            commands.add(c.name());
        }
        return commands;
    }

    public static void main(String[] args) throws IOException {
        AppController controller = new AppController();
        String message = "{\"message\":\"hi back\",\"number\":\"919999700996\"}";
        controller.sendReply(message);
        message = "{\"caption\":\"hi back\",\"number\":\"919999700996\",\"path\":\"http://n1.sdlcdn.com/imgs/a/j/u/166x194/Samsung-S-Duos-2-White--SDL668211406-1-6437f.jpg\"}";
        controller.sendReplyMedia(message);
    }

    public void sendReply(String message) throws ClientProtocolException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost("http://10.20.61.106:8989/reply");
        StringEntity input = new StringEntity(message);
        input.setContentType("application/json");
        postRequest.setEntity(input);
        httpClient.execute(postRequest);
        httpClient.getConnectionManager().shutdown();
    }

    public void sendReplyMedia(String message) throws ClientProtocolException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost("http://10.20.61.106:8989/replymedia");
        StringEntity input = new StringEntity(message);
        input.setContentType("application/json");
        postRequest.setEntity(input);
        httpClient.execute(postRequest);
        httpClient.getConnectionManager().shutdown();
    }

}
