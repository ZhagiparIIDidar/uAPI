package kz.uapi.domain.Marks;

import java.time.LocalDateTime;

/**
 * Класс, представляющий оценку (отметку) студента за курс.
 * Содержит информацию об оценке, дате выставления и привязке к студенту и
 * курсу.
 * 
 * <p>
 * Оценка хранит числовое значение (grade) в диапазоне, зависящем от системы
 * оценивания учреждения.
 * </p>
 * 
 * @author UAPI System
 * @version 1.0
 * @since 1.0
 */
public class Mark {

    private int markId;
    private String studentLogin;
    private int courseId;
    private double grade;
    private LocalDateTime date;

    public Mark(int markId, String studentLogin, int courseId, double grade, LocalDateTime date) {
        this.markId = markId;
        this.studentLogin = studentLogin;
        this.courseId = courseId;
        this.grade = grade;
        this.date = date;
    }

    public int getMarkId() {
        return markId;
    }

    public String getStudentLogin() {
        return studentLogin;
    }

    public int getCourseId() {
        return courseId;
    }

    public double getGrade() {
        return grade;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public void setStudentLogin(String studentLogin) {
        this.studentLogin = studentLogin;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return markId + " | " + studentLogin + " | Course: " + courseId + " | Grade: " + grade;
    }
}