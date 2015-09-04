/**
 *  Copyright 2015 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.pears.whatsapp3c.model;

/**
 * @version 1.0, 04-Sep-2015
 * @author loki
 */
public class InputMessage {

    String message;
    String caller;
    String messageid;

    public String getMessage() {
        return message;
    }

    public String getCaller() {
        return caller;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

}
