/**
 *  Copyright 2015 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.pears.whatsapp3c.model;

/**
 * @version 1.0, 04-Sep-2015
 * @author loki
 */
public class ReplyMedia {
    String number;
    String caption;
    String path;

    public String getNumber() {
        return number;
    }

    public String getCaption() {
        return caption;
    }

    public String getPath() {
        return path;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
