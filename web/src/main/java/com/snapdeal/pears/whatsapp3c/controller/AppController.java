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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.snapdeal.pears.whatsapp3c.constants.Commands;
import com.snapdeal.pears.whatsapp3c.model.InputMessage;
import com.snapdeal.pears.whatsapp3c.model.Reply;
import com.snapdeal.pears.whatsapp3c.model.ReplyMedia;
import com.snapdeal.pears.whatsapp3c.requestresponse.ConversationList;
import com.snapdeal.pears.whatsapp3c.requestresponse.GetLastMessagesResponse;
import com.snapdeal.pears.whatsapp3c.requestresponse.UserMessage;
import com.snapdeal.pears.whatsapp3c.service.MessageService;

/**
 * Created by ditesh on 2/9/15.
 */
@SuppressWarnings("deprecation")
@Controller
public class AppController {

    private static final Logger       LOG    = LoggerFactory.getLogger(AppController.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Gson         gson   = new Gson();

    //TODO ditesh
    @RequestMapping(value = "/unlockConversation/{phone}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Boolean unlockConversation(@PathVariable("phone") String phone) {
        if (phone.equals("1"))
            return true;
        else if (phone.equals("2"))
            return true;
        else
            return false;
    }

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
        list.addConversation(new ConversationList.Conversation("1", "Hi there! m 1", 999999l));
        list.addConversation(new ConversationList.Conversation("2", "Hi there! m 2", 999977l));
        return list;
    }

    //TODO ditesh
    @RequestMapping(value = "/lockConversation/{phone}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Boolean lockConversation(@PathVariable("phone") String phone) {
        if (phone.equals("1"))
            return true;
        else if (phone.equals("2"))
            return false;
        else
            return false;
    }

    //TODO ditesh (respect count value)
    @RequestMapping(value = "/getLastMessages/{phone}/{count}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    GetLastMessagesResponse getLastMessages(@PathVariable("phone") String phone, @PathVariable("count") String count) {
        if (phone.equals("1")) {
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            response.addMessages(new UserMessage(3l, "message no. 3", 999999l, true));
            response.addMessages(new UserMessage(2l, "message no. 2", 999988l, false));
            response.addMessages(new UserMessage(1l, "message no. 1", 999977l, true));
            return response;
        } else if (phone.equals("2")) {
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            response.addMessages(new UserMessage(5l, "message no. 5", 999999l, true));
            response.addMessages(new UserMessage(4l, "message no. 4", 999988l, false));
            response.addMessages(new UserMessage(3l, "message no. 3", 999977l, true));
            response.addMessages(new UserMessage(2l, "message no. 2", 999966l, false));
            response.addMessages(new UserMessage(1l, "message no. 1", 999955l, true));
            return response;
        } else {
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            return response;
        }
    }

    //TODO ditesh (respect count value)
    @RequestMapping(value = "/getMessages/{phone}/{startOffset}/{endOffset}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    GetLastMessagesResponse getMessages(@PathVariable("phone") String phone, @PathVariable("startOffset") Long startOffset, @PathVariable("endOffset") Long endOffset) {
        if (phone.equals("1")) {
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            response.addMessages(new UserMessage(3l, "message no. 3", 999999l, true));
            response.addMessages(new UserMessage(2l, "message no. 2", 999988l, false));
            response.addMessages(new UserMessage(1l, "message no. 1", 999977l, true));
            return response;
        } else if (phone.equals("2")) {
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            response.addMessages(new UserMessage(5l, "message no. 5", 999999l, true));
            response.addMessages(new UserMessage(4l, "message no. 4", 999988l, false));
            response.addMessages(new UserMessage(3l, "message no. 3", 999977l, true));
            response.addMessages(new UserMessage(2l, "message no. 2", 999966l, false));
            response.addMessages(new UserMessage(1l, "message no. 1", 999955l, true));
            return response;
        } else {
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            return response;
        }
    }

    //TODO ditesh
    @RequestMapping(value = "/sendCCMessage/{phone}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Long sendCCMessage(@PathVariable("phone") String phone, @RequestParam("message") String message) {
        LOG.info("Received message for phone number {}, message {}", phone, message);
        return 10l;
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @RequestMapping(value = "/processMessage", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public String processMessage(@RequestBody String input) throws IOException {
        Map<String, Object> data = mapper.readValue(input, Map.class);
        String message = data.get("message").toString();
        String caller = data.get("caller").toString();
        String messageId = data.get("messageid").toString();
        Reply toReturn = new Reply();
        toReturn.setMessage("WhaCha. Welcome to Snapdeal.");
        toReturn.setNumber(caller);
        message = message.trim().toLowerCase();
        List<ReplyMedia> medias = new ArrayList<ReplyMedia>();
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
        return "done";
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
        HttpPost postRequest = new HttpPost("http://localhost:8989/" + api);
        StringEntity input;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            input = new StringEntity(message);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            httpClient.execute(postRequest);
            httpClient.close();
        } catch (UnsupportedEncodingException e) {
            // do nothing
        } catch (ClientProtocolException e) {
            // do nothing
        } catch (IOException e) {
            // do nothing
        }
    }
}
