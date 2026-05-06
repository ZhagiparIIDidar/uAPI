package kz.uapi.domain.Reseach;

import java.time.LocalDateTime;
import java.util.List;

public class ResearchPaper {

    private int paperId;
    private String title;
    private String content;
    private String authorLogin;
    private int projectId;
    private LocalDateTime publishedDate;
    private int citations;
    private List<String> keywords;
    private String metrics;
    private List<String> figures;

    public ResearchPaper(int paperId, String title, String content, String authorLogin,
            int projectId, LocalDateTime publishedDate, int citations,
            List<String> keywords, String metrics, List<String> figures) {
        this.paperId = paperId;
        this.title = title;
        this.content = content;
        this.authorLogin = authorLogin;
        this.projectId = projectId;
        this.publishedDate = publishedDate;
        this.citations = citations;
        this.keywords = keywords;
        this.metrics = metrics;
        this.figures = figures;
    }

    public int getPaperId() {
        return paperId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public int getProjectId() {
        return projectId;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public int getCitations() {
        return citations;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getMetrics() {
        return metrics;
    }

    public List<String> getFigures() {
        return figures;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setCitations(int citations) {
        this.citations = citations;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public void setFigures(List<String> figures) {
        this.figures = figures;
    }

    @Override
    public String toString() {
        return paperId + " | " + title + " | " + authorLogin + " | Citations: " + citations;
    }
}
