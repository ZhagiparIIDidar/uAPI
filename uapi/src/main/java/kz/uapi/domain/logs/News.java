package kz.uapi.domain.logs;

import java.time.LocalDateTime;

public class News {

    private int newsId;
    private String title;
    private String content;
    private LocalDateTime date;
    private String authorLogin;

    public News(int newsId, String title, String content, LocalDateTime date, String authorLogin) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.authorLogin = authorLogin;
    }

    public int getNewsId() {
        return newsId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    @Override
    public String toString() {
        return newsId + " | " + title + " | " + date + " | " + authorLogin;
    }
}