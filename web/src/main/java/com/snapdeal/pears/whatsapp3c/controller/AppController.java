package com.snapdeal.pears.whatsapp3c.controller;

import com.snapdeal.pears.whatsapp3c.requestresponse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ditesh on 2/9/15.
 */
@Controller
public class AppController {

    private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

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
        list.addConversation(new ConversationList.Conversation("1","Hi there! m 1",999999l));
        list.addConversation(new ConversationList.Conversation("2","Hi there! m 2",999977l));
        return list;
    }

    //TODO ditesh
    @RequestMapping(value = "/lockConversation/{phone}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Boolean lockConversation(@PathVariable("phone") String phone) {
        if (phone.equals("1"))
            return true;
        else if (phone.equals("2"))
            return false;
        else return false;
    }

    //TODO ditesh (respect count value)
    @RequestMapping(value = "/getLastMessages/{phone}/{count}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    GetLastMessagesResponse getLastMessages(@PathVariable("phone")String phone,
                                            @PathVariable("count") String count) {
        if (phone.equals("1")){
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            response.addMessages(new UserMessage(3l,"message no. 3",999999l,true));
            response.addMessages(new UserMessage(2l,"message no. 2",999988l,true));
            response.addMessages(new UserMessage(1l,"message no. 1",999977l,true));
            return response;
        }else if (phone.equals("2")){
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            response.addMessages(new UserMessage(5l,"message no. 5",999999l,true));
            response.addMessages(new UserMessage(4l,"message no. 4",999988l,true));
            response.addMessages(new UserMessage(3l,"message no. 3",999977l,true));
            response.addMessages(new UserMessage(2l,"message no. 2",999966l,true));
            response.addMessages(new UserMessage(1l,"message no. 1",999955l,true));
            return response;
        }else {
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            return response;
        }
    }

    //TODO ditesh (respect count value)
    @RequestMapping(value = "/getMessages/{phone}/{startOffset}/{endOffset}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    GetLastMessagesResponse getMessages(@PathVariable("phone")String phone,
                                        @PathVariable("startOffset") Long startOffset,
                                        @PathVariable("endOffset") Long endOffset) {
        if (phone.equals("1")){
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            response.addMessages(new UserMessage(3l,"message no. 3",999999l,true));
            response.addMessages(new UserMessage(2l,"message no. 2",999988l,true));
            response.addMessages(new UserMessage(1l,"message no. 1",999977l,true));
            return response;
        } else if (phone.equals("2")){
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            response.addMessages(new UserMessage(5l,"message no. 5",999999l,true));
            response.addMessages(new UserMessage(4l,"message no. 4",999988l,true));
            response.addMessages(new UserMessage(3l,"message no. 3",999977l,true));
            response.addMessages(new UserMessage(2l,"message no. 2",999966l,true));
            response.addMessages(new UserMessage(1l,"message no. 1",999955l,true));
            return response;
        }else {
            GetLastMessagesResponse response = new GetLastMessagesResponse();
            return response;
        }
    }

    //TODO ditesh
    @RequestMapping(value = "/sendCCMessage/{phone}", method = RequestMethod.GET ,produces = "application/json")
    public @ResponseBody Long sendCCMessage(@PathVariable("phone") String phone,
                                                  @RequestParam("message") String message) {
        LOG.info("Received message for phone number {}, message {}",phone, message);
        return 10l;
    }
    
}
