package com.snapdeal.pears.whatsapp3c.requestresponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ditesh on 3/9/15.
 */
public class ConversationList {

	private List<Conversation> conversations = new ArrayList<Conversation>();

	public List<Conversation> getConversations() {
		return conversations;
	}

	// TODO add and sort
	public void addConversation(Conversation con) {
		conversations.add(con);
	}

	public static class Conversation {
		private long phone;
		private String lastUnreadMessage;
		private Long lastUnreadTS;

		public Conversation(long phone, String lastUnreadMessage, Long lastUnreadTS) {
			this.phone = phone;
			this.lastUnreadMessage = lastUnreadMessage;
			this.lastUnreadTS = lastUnreadTS;
		}

		public long getPhone() {
			return phone;
		}

		public void setPhone(long phone) {
			this.phone = phone;
		}

		public String getLastUnreadMessage() {
			return lastUnreadMessage;
		}

		public void setLastUnreadMessage(String lastUnreadMessage) {
			this.lastUnreadMessage = lastUnreadMessage;
		}

		public Long getLastUnreadTS() {
			return lastUnreadTS;
		}

		public void setLastUnreadTS(Long lastUnreadTS) {
			this.lastUnreadTS = lastUnreadTS;
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("Conversation{");
			sb.append("phone='").append(phone).append('\'');
			sb.append(", lastUnreadMessage='").append(lastUnreadMessage).append('\'');
			sb.append(", lastUnreadTS=").append(lastUnreadTS);
			sb.append('}');
			return sb.toString();
		}
	}
}
