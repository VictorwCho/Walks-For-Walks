package com.bcit.walksforwalks;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String messageStringTime;

    public ChatMessage(String mText, String mUser) {
        messageText = mText;
        messageUser = mUser;

        messageTime = new Date().getTime();
    }

    public ChatMessage(String mText, String mUser, String mTime) {
        messageText = mText;
        messageUser = mUser;
        messageStringTime = mTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageStringTime() {
        return messageStringTime;
    }

    public void setMessageStringTime(String messageStringTime) {
        this.messageStringTime = messageStringTime;
    }
}
