package com.example.pocketstore_customerapp.enterShop.chatFeed;

public class Chat {
    private String chatId;
    private String userName;
    private String userId;
    private String chatBody;
    private String time;

    public Chat() { }

    public Chat(String chatId, String userName, String userId, String chatBody, String time) {
        this.chatId = chatId;
        this.userName = userName;
        this.userId = userId;
        this.chatBody = chatBody;
        this.time = time;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatBody() {
        return chatBody;
    }

    public void setChatBody(String chatBody) {
        this.chatBody = chatBody;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
