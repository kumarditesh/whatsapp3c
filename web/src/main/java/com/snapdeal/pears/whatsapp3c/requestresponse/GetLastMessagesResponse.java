package com.snapdeal.pears.whatsapp3c.requestresponse;

import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ditesh on 4/9/15.
 */
public class GetLastMessagesResponse {
    
    private List<UserMessage> messages = new ArrayList<UserMessage>();

    public List<UserMessage> getMessages() {
        return messages;
    }

    public void addMessages(UserMessage message) {
        messages.add(message);
    }
}
