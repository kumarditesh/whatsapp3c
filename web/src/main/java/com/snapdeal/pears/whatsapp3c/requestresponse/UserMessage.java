package com.snapdeal.pears.whatsapp3c.requestresponse;

/**
 * Created by ditesh on 4/9/15.
 */
public class UserMessage {
    
    private Long sid;
    private String message;
    private Long time;
    private Boolean inboundmsg;

    public UserMessage(Long sid, String message, Long time, Boolean inboundmsg) {
        this.sid = sid;
        this.message = message;
        this.time = time;
        this.inboundmsg = inboundmsg;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserMessage{");
        sb.append("sid=").append(sid);
        sb.append(", message='").append(message).append('\'');
        sb.append(", time=").append(time);
        sb.append(", inboundmsg=").append(inboundmsg);
        sb.append('}');
        return sb.toString();
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean getInboundmsg() {
        return inboundmsg;
    }

    public void setInboundmsg(Boolean inboundmsg) {
        this.inboundmsg = inboundmsg;
    }
}
