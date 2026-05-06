package kz.uapi.domain.other_obj;

public class Schedule {

    private int scheduleId;
    private String studentLogin;

    public Schedule(int scheduleId, String studentLogin) {
        this.scheduleId = scheduleId;
        this.studentLogin = studentLogin;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public String getStudentLogin() {
        return studentLogin;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setStudentLogin(String studentLogin) {
        this.studentLogin = studentLogin;
    }

    @Override
    public String toString() {
        return scheduleId + " | " + studentLogin;
    }
}