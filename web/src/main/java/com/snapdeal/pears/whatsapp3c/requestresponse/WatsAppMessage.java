package com.snapdeal.pears.whatsapp3c.requestresponse;


public class WatsAppMessage implements Comparable<WatsAppMessage> {

	private String message;

	private boolean inboundmsg;

	private long producerTs;

	private String watsapp_id;

	private int id;

	private boolean read;

	public WatsAppMessage(String message, String watsapp_id, int id, boolean sender) {
		this.id = id;
		this.message = message;
		this.watsapp_id = watsapp_id;
		this.producerTs = System.currentTimeMillis();
		this.read = false;
		this.inboundmsg = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isInboundmsg() {
		return inboundmsg;
	}

	public void setInboundmsg(boolean inboundmsg) {
		this.inboundmsg = inboundmsg;
	}

	public long getProducerTs() {
		return producerTs;
	}

	public void setProducerTs(long producerTs) {
		this.producerTs = producerTs;
	}

	public String getWatspp_id() {
		return watsapp_id;
	}

	public void setWatspp_id(String watspp_id) {
		this.watsapp_id = watspp_id;
	}

	public String getWatsapp_id() {
		return watsapp_id;
	}

	public void setWatsapp_id(String watsapp_id) {
		this.watsapp_id = watsapp_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	@Override
	public int compareTo(WatsAppMessage o) {
		return this.id - o.id;
	}

	@Override
	public String toString() {
		return "WatsAppMessage [message=" + message + ", inboundmsg=" + inboundmsg + ", producerTs=" + producerTs
				+ ", watsapp_id=" + watsapp_id + ", id=" + id + ", read=" + read + "]";
	}

}
