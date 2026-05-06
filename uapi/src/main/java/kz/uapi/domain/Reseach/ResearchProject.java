package kz.uapi.domain.Reseach;

import java.util.List;

public class ResearchProject {

    private int projectId;
    private String title;
    private String description;
    private String supervisorLogin;
    private List<String> members;
    private String status;

    public ResearchProject(int projectId, String title, String description,
            String supervisorLogin, List<String> members, String status) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.supervisorLogin = supervisorLogin;
        this.members = members;
        this.status = status;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSupervisorLogin() {
        return supervisorLogin;
    }

    public List<String> getMembers() {
        return members;
    }

    public String getStatus() {
        return status;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSupervisorLogin(String supervisorLogin) {
        this.supervisorLogin = supervisorLogin;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return projectId + " | " + title + " | " + status + " | Supervisor: " + supervisorLogin;
    }
}
