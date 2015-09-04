package com.snapdeal.pears.whatsapp3c.requestresponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ditesh on 4/9/15.
 */
public class GetLastMessagesResponse {
    
    private List<WatsAppMessage> messages = new ArrayList<WatsAppMessage>();
    
    public GetLastMessagesResponse(List<WatsAppMessage> messages){
    	this.messages = messages;
    }



    public void addMessages(WatsAppMessage message) {
        messages.add(message);
    }



	public List<WatsAppMessage> getMessages() {
		return messages;
	}



	public void setMessages(List<WatsAppMessage> messages) {
		this.messages = messages;
	}
}
