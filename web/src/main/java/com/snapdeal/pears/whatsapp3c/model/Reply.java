/**
 *  Copyright 2015 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.pears.whatsapp3c.model;

/**
 * @version 1.0, 04-Sep-2015
 * @author loki
 */
public class Reply {
    String message;
    String number;

    public String getMessage() {
        return message;
    }

    public String getNumber() {
        return number;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
