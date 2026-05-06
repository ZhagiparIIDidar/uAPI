package kz.uapi.domain.other_obj;

import java.time.LocalDateTime;

public class Message {

    private int messageId;
    private String senderLogin;
    private String receiverLogin;
    private String content;
    private LocalDateTime date;

    public Message(int messageId, String senderLogin, String receiverLogin,
            String content, LocalDateTime date) {
        this.messageId = messageId;
        this.senderLogin = senderLogin;
        this.receiverLogin = receiverLogin;
        this.content = content;
        this.date = date;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public String getReceiverLogin() {
        return receiverLogin;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public void setReceiverLogin(String receiverLogin) {
        this.receiverLogin = receiverLogin;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return messageId + " | " + senderLogin + " → " + receiverLogin + " | " + content;
    }
}