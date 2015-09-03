package com.snapdeal.pears.whatsapp3c.controller;

import com.snapdeal.pears.whatsapp3c.requestresponse.ConversationList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ditesh on 2/9/15.
 */
@Controller
public class AppController {

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
        list.addConversation(new ConversationList.Conversation("12345","Hi there",999999l));
        return list;
    }
}
