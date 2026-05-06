package kz.uapi.domain.Marks;

public class Transcript {

    private int transcriptId;
    private String studentLogin;
    private double gpa;

    public Transcript(int transcriptId, String studentLogin, double gpa) {
        this.transcriptId = transcriptId;
        this.studentLogin = studentLogin;
        this.gpa = gpa;
    }

    public int getTranscriptId() {
        return transcriptId;
    }

    public String getStudentLogin() {
        return studentLogin;
    }

    public double getGpa() {
        return gpa;
    }

    public void setTranscriptId(int transcriptId) {
        this.transcriptId = transcriptId;
    }

    public void setStudentLogin(String studentLogin) {
        this.studentLogin = studentLogin;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return transcriptId + " | " + studentLogin + " | GPA: " + gpa;
    }
}
