package com.snapdeal.pears.whatsapp3c.requestresponse;

/**
 * Created by ditesh on 4/9/15.
 */
public class GetLastMessagesRequest {
    
    private String phone;
    private Integer count;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GetLastMessagesRequest{");
        sb.append("phone='").append(phone).append('\'');
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
