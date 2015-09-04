package com.snapdeal.pears.whatsapp3c.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.snapdeal.pears.whatsapp3c.model.InputMessage;
import com.snapdeal.pears.whatsapp3c.model.Reply;
import com.snapdeal.pears.whatsapp3c.model.ReplyMedia;
import com.snapdeal.pears.whatsapp3c.requestresponse.ConversationList;
import com.snapdeal.pears.whatsapp3c.service.MessageService;

/**
 * Created by ditesh on 2/9/15.
 */
@Controller
public class AppController {

    private static final ObjectMapper mapper     = new ObjectMapper();

    private static final Gson         gson       = new Gson();

    private DefaultHttpClient         httpClient = new DefaultHttpClient();

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
        Reply toReturn = new Reply();
        toReturn.setMessage("Wassup. Welcome to Snapdeal.");
        toReturn.setNumber(caller);
        List<ReplyMedia> medias = new ArrayList<ReplyMedia>();
        message = message.trim().toLowerCase();
        if (getCommands().contains(message.split(":")[0].trim())) {
            String command = message.split(":")[0].trim();
            if (Commands.order.name().equals(command)) {
                String orderId = message.split(":")[1].trim().split(",")[0];
                String emailId = message.split(":")[1].trim().split(",")[1];
                medias = MessageService.getOrderStatus(orderId, emailId);
                if (medias.size() == 1 && (medias.get(0).getPath() == null)) {
                    toReturn.setMessage(medias.get(0).getCaption());
                    medias.clear();
                }
            }
            if (Commands.search.name().equals(command)) {
                String keyword = message.split(":")[1].trim();
                medias = MessageService.getSearchResults(keyword);
            }
            if (Commands.trending.name().equals(command)) {
                medias = MessageService.getTrendingProducts();
            }
        }
        if (medias.size() > 0) {
            for (ReplyMedia media : medias) {
                media.setNumber(caller);
                sendReply(gson.toJson(media), "replymedia");
            }
        } else {
            sendReply(gson.toJson(toReturn), "reply");
        }
    }

    public List<String> getCommands() {
        List<String> commands = new ArrayList<String>();
        for (Commands c : Commands.values()) {
            commands.add(c.name());
        }
        return commands;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AppController controller = new AppController();
        InputMessage message = new InputMessage();
        message.setCaller("919999700996");
        message.setMessageid("1");
        message.setMessage("order:6829047157,lokesh.chhaparwal@snapdeal.com");
        controller.processMessage(gson.toJson(message));
        message.setMessage("search:samsung galaxy s duos");
        controller.processMessage(gson.toJson(message));
        message.setMessage("trending");
        controller.processMessage(gson.toJson(message));
    }

    public void sendReply(String message, String api) {
        HttpPost postRequest = new HttpPost("http://10.20.61.106:8989/" + api);
        StringEntity input;
        try {
            input = new StringEntity(message);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            httpClient.execute(postRequest);
        } catch (UnsupportedEncodingException e) {
            // do nothing
        } catch (ClientProtocolException e) {
            // do nothing
        } catch (IOException e) {
            // do nothing
        }
    }

}
