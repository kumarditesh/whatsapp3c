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
import org.springframework.util.StringUtils;
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
import com.snapdeal.pears.whatsapp3c.requestresponse.WatsAppMessage;
import com.snapdeal.pears.whatsapp3c.service.MessageService;

/**
 * Created by ditesh on 2/9/15.
 */
@SuppressWarnings("deprecation")
@Controller
public class AppController {

    private static final Logger       LOG     = LoggerFactory.getLogger(AppController.class);
    private static final ObjectMapper mapper  = new ObjectMapper();

    private static final Gson         gson    = new Gson();

    private final MessageService      service = new MessageService();

    private static final String       ip      = System.getProperty("address");

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET, produces = "text/html")
    public @ResponseBody
    String createRequestPage() {
        return "Health OK";
    }

    @RequestMapping(value = "/getUnreadConversations", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ConversationList getUnreadConversations() {
        return service.prepareConversationList();
    }

    @RequestMapping(value = "/lockConversation/{phone}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Boolean lockConversation(@PathVariable("phone") String phone) {
        return service.lockConversation(phone);

    }

    @RequestMapping(value = "/unlockConversation/{phone}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Boolean unlockConversation(@PathVariable("phone") String phoneNumber) {
        service.unlockConversation(phoneNumber);
        return true;
    }

    @RequestMapping(value = "/getLastMessages/{phone}/{count}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    GetLastMessagesResponse getLastMessages(@PathVariable("phone") String phone, @PathVariable("count") Integer count) {
        List<WatsAppMessage> watsAppMessages = service.getMessagesHolder().get(phone);
        List<WatsAppMessage> sendMessages = null;
        int totalMessages = watsAppMessages.size();
        if (count > totalMessages) {
            sendMessages = service.getMessages(phone, 0, totalMessages);
        } else {
            sendMessages = service.getMessages(phone, totalMessages - count, totalMessages);
        }

        GetLastMessagesResponse response = new GetLastMessagesResponse(sendMessages);
        return response;
    }

    @RequestMapping(value = "/getMessages/{phone}/{startOffset}/{endOffset}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    GetLastMessagesResponse getMessages(@PathVariable("phone") String phone, @PathVariable("startOffset") Integer startOffset, @PathVariable("endOffset") Integer endOffset) {
        List<WatsAppMessage> messages = service.getMessages(phone, startOffset, endOffset);
        GetLastMessagesResponse response = new GetLastMessagesResponse(messages);
        return response;
    }

    @RequestMapping(value = "/sendCCMessage/{phone}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Long sendCCMessage(@PathVariable("phone") String phone, @RequestParam("message") String message) {
        String default_messageid = "123";
        Reply r = new Reply();
        r.setNumber(phone);
        r.setMessage(message);
        sendReply(gson.toJson(r), "reply");
        return (Long) service.postMessage(phone, message, default_messageid, false);
    }

    @RequestMapping(value = "/postMessage/{phone}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Long postMessage(@PathVariable("phone") String phone, @RequestParam("message") String message, @RequestParam("messageId") String messageId) {
        return service.postMessage(phone, message, messageId, true);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @RequestMapping(value = "/processMessage", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public String processMessage(@RequestBody String input) throws IOException {
        LOG.info("processing message {}", input);
        String api = "reply";
        Map<String, Object> data = mapper.readValue(input, Map.class);
        String message = data.get("message").toString();
        String caller = data.get("caller").toString();
        if (caller.contains("@")) {
            caller = caller.split("@")[0];
        }
        String messageId = data.get("messageid").toString();
        Reply toReturn = new Reply();
        toReturn.setMessage("WhaCha. Welcome to Snapdeal.");
        toReturn.setNumber(caller);
        message = message.trim().toLowerCase();
        List<ReplyMedia> medias = new ArrayList<ReplyMedia>();
        boolean img = false;
        if (getCommands().contains(message.split(":")[0].trim())) {
            String command = message.split(":")[0].trim();
            if (Commands.orderimg.name().equals(command)) {
                String orderId = message.split(":")[1].trim().split(",")[0];
                String emailId = message.split(":")[1].trim().split(",")[1];
                medias = MessageService.getOrderStatus(orderId, emailId);
                if (medias.size() == 1 && (medias.get(0).getPath() == null)) {
                    toReturn.setMessage(medias.get(0).getCaption());
                    medias.clear();
                }
                api = "replyorder";
                img = true;
            }
            if (Commands.searchimg.name().equals(command)) {
                String keyword = message.split(":")[1].trim();
                medias = MessageService.getSearchResults(keyword);
                api = "replysearch";
                img = true;
            }
            if (Commands.trendingimg.name().equals(command)) {
                medias = MessageService.getTrendingProducts();
                api = "replytrend";
                img = true;
            }
            if (Commands.order.name().equals(command)) {
                String orderId = message.split(":")[1].trim().split(",")[0];
                String emailId = message.split(":")[1].trim().split(",")[1];
                medias = MessageService.getOrderStatus(orderId, emailId);
                if (medias.size() == 1 && (medias.get(0).getPath() == null)) {
                    toReturn.setMessage(medias.get(0).getCaption());
                    medias.clear();
                }
                if (medias.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (ReplyMedia rm : medias) {
                        sb.append(rm.getCaption() + "\n \n \n");
                    }
                    toReturn.setMessage(sb.toString());
                }
            }
            if (Commands.search.name().equals(command)) {
                String keyword = message.split(":")[1].trim();
                medias = MessageService.getSearchResults(keyword);
                StringBuilder sb = new StringBuilder();
                for (ReplyMedia rm : medias) {
                    sb.append(rm.getCaption() + "\n \n \n");
                }
                toReturn.setMessage(sb.toString());
            }
            if (Commands.trending.name().equals(command)) {
                medias = MessageService.getTrendingProducts();
                StringBuilder sb = new StringBuilder();
                for (ReplyMedia rm : medias) {
                    sb.append(rm.getCaption() + "\n \n \n");
                }
                toReturn.setMessage(sb.toString());
            }
        }
        if (medias.size() > 0 && img) {
            for (ReplyMedia media : medias) {
                media.setNumber(caller);
            }
            sendReply(gson.toJson(medias.subList(0, 2)), api);
        } else {
            if (!"WhaCha. Welcome to Snapdeal.".equals(toReturn.getMessage())) {
                sendReply(gson.toJson(toReturn), api);
            } else {
                service.postMessage(caller, message, messageId, true);
            }
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
        LOG.info("Sending {} {}", api, message);
        String IP = (StringUtils.isEmpty(ip)) ? "10.20.61.106:8989" : ip;
        HttpPost postRequest = new HttpPost("http://" + IP + "/" + api);
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
