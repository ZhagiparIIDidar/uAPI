package kz.uapi.domain.logs;

import java.time.LocalDateTime;

public class Log {

    private int logId;
    private String userLogin;
    private String action;
    private LocalDateTime date;

    public Log(int logId, String userLogin, String action, LocalDateTime date) {
        this.logId = logId;
        this.userLogin = userLogin;
        this.action = action;
        this.date = date;
    }

    public int getLogId() {
        return logId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return logId + " | " + userLogin + " | " + action + " | " + date;
    }
}