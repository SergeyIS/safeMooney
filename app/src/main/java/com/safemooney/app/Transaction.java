package com.safemooney.app;

/**
 * Created by root on 7/27/17.
 */

public class Transaction {

    private String username;
    private String count;
    private byte[] image;
    private  boolean isMe;

    public Transaction(String username, String count, byte[] image, boolean isMe){
        this.username = username;
        this.count = count;
        this.image = image;
        this.isMe = isMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getIsMe() {
        return isMe;
    }

    public void setIsMe(boolean me) {
        isMe = me;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
