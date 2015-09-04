package com.snapdeal.pears.whatsapp3c.requestresponse;

/**
 * Created by ditesh on 4/9/15.
 */
public class GetMessagesRequest {

    private String phone;
    private Long startOffset;
    private Long endOffset;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(Long startOffset) {
        this.startOffset = startOffset;
    }

    public Long getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(Long endOffset) {
        this.endOffset = endOffset;
    }
}
