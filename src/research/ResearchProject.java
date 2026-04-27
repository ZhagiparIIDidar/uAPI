import java.io.*;
import java.util.*;

/**
 * Represents a research project with participants and papers.
 */
public class ResearchProject {

    private int projectId;
    private String topic;
    private List<User> participants = new ArrayList<>();
    private List<ResearchPaper> papers = new ArrayList<>();

    /**
     * Default constructor
     */
    public ResearchProject() {}

    public ResearchProject(int projectId, String topic) {
        this.projectId = projectId;
        this.topic = topic;
    }
    public int getProjectId()    { return projectId; }
    public String getTopic()     { return topic; }
    public void setTopic(String t) { this.topic = t; }

   
    public void addParticipant(User user) {
        if (user != null && !participants.contains(user)) {
            participants.add(user);
            System.out.println(user.getName() + " added to project: " + topic);
        }
    }

    /**
     * Removes a participant from this project.
     */
    public void removeParticipant(User user) {
        if (participants.remove(user))
            System.out.println(user.getName() + " removed from project: " + topic);
        else
            System.out.println("Participant not found.");
    }

    /**
     * Adds a research paper to this project.
     */
    public void addPaper(ResearchPaper paper) {
        if (paper != null && !papers.contains(paper)) {
            papers.add(paper);
            System.out.println("Paper added: " + paper.getTitle());
        }
    }

    /**
     * Returns the list of participants.
     */
    public List<User> getParticipants() {
        return participants;
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    @Override
    public String toString() {
        return "ResearchProject{id=" + projectId + ", topic=" + topic +
               ", participants=" + participants.size() + ", papers=" + papers.size() + "}";
    }
}