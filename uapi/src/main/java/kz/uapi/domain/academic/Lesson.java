package kz.uapi.domain.academic;

import kz.uapi.enums.LessonType;

/**
 * Класс, представляющий отдельное занятие (лекцию, практику или семинар) в
 * рамках курса.
 * Содержит информацию о теме, используемых материалах и типе занятия.
 * 
 * <p>
 * Каждое занятие принадлежит одному курсу и имеет определённый тип
 * ({@link LessonType}).
 * </p>
 * 
 * @author UAPI System
 * @version 1.0
 * @since 1.0
 * @see LessonType
 * @see Course
 */
public class Lesson {

    private int lessonId;
    private int courseId;
    private String topic;
    private String usedMaterials;
    private LessonType lessonType;

    public Lesson(int lessonId, int courseId, String topic,
            String usedMaterials, LessonType lessonType) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.topic = topic;
        this.usedMaterials = usedMaterials;
        this.lessonType = lessonType;
    }

    public int getLessonId() {
        return lessonId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTopic() {
        return topic;
    }

    public String getUsedMaterials() {
        return usedMaterials;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setUsedMaterials(String usedMaterials) {
        this.usedMaterials = usedMaterials;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    @Override
    public String toString() {
        return lessonId + " | " + topic + " | " + lessonType;
    }
}
