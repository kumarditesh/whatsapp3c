package com.snapdeal.pears.whatsapp3c.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.pears.whatsapp3c.constants.Commands;
import com.snapdeal.pears.whatsapp3c.requestresponse.ConversationList;
import com.snapdeal.pears.whatsapp3c.service.MessageService;

/**
 * Created by ditesh on 2/9/15.
 */
@Controller
public class AppController {

    private static final ObjectMapper mapper = new ObjectMapper();

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
    public String processMessage(@RequestBody String input) throws IOException {
        Map<String, Object> data = mapper.readValue(input, Map.class);
        String message = data.get("message").toString();
        String caller = data.get("caller").toString();
        String messageId = data.get("messageid").toString();
        message = message.trim().toLowerCase();
        if (getCommands().contains(message.split(":")[0].trim())) {
            if (Commands.order.name().equals(message.split(":")[0].trim())) {
                String orderId = message.split(":")[1].trim().split(",")[0];
                String emailId = message.split(":")[1].trim().split(",")[1];
                return MessageService.getOrderStatus(orderId, emailId);
            }
        }
        return "hi back";
    }

    public List<String> getCommands() {
        List<String> commands = new ArrayList<String>();
        for (Commands c : Commands.values()) {
            commands.add(c.name());
        }
        return commands;
    }
}
