import java.io.*;
import java.util.*;

/**
 * Represents a single lesson within a course.
 */
public class Lesson {

    private int lessonId;
    private Date date;
    private String topic;
    private LessonType type;

    /**
     * Default constructor
     */
    public Lesson() {}

    public Lesson(int lessonId, String topic, Date date, LessonType type) {
        this.lessonId = lessonId;
        this.topic = topic;
        this.date = date;
        this.type = type;
    }


    public int getLessonId()          { return lessonId; }
    public void setLessonId(int id)   { this.lessonId = id; }
    public Date getDate()             { return date; }
    public void setDate(Date date)    { this.date = date; }
    public void setTopic(String t)    { this.topic = t; }
    public void setType(LessonType t) { this.type = t; }

    
    public LessonType getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "Lesson{id=" + lessonId + ", topic=" + topic +
               ", type=" + type + ", date=" + date + "}";
    }
}
