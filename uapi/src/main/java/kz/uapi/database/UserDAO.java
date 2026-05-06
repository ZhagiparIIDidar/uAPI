package kz.uapi.database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import kz.uapi.abs_class.*;
import kz.uapi.enums.*;
import kz.uapi.users.Employee.*;
import kz.uapi.users.Student;

public class UserDAO {

    public static User getUserByLogin(String login) {
        String query = "SELECT * FROM users WHERE login = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                switch (role) {
                    case "STUDENT" -> {
                        return getStudent(rs, conn);
                    }
                    case "TEACHER" -> {
                        return getTeacher(rs, conn);
                    }
                    case "MANAGER" -> {
                        return getManager(rs, conn);
                    }
                    case "ADMIN" -> {
                        return getAdmin(rs, conn);
                    }
                    case "RESEARCHER" -> {
                        return getResearcherEmployee(rs, conn);
                    }
                    default -> {
                        return null;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }

    private static Student getStudent(ResultSet rs, Connection conn) throws SQLException {
        String login = rs.getString("login");
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE login = ?");
        stmt.setString(1, login);
        ResultSet sr = stmt.executeQuery();

        if (sr.next()) {
            return new Student(
                    login,
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    LocalDateTime.parse(rs.getString("created_at")),
                    sr.getDouble("gpa"),
                    sr.getInt("year"),
                    sr.getString("major"),
                    sr.getInt("credits"),
                    sr.getInt("is_researcher") == 1,
                    sr.getString("supervisor_login"));
        }
        return null;
    }

    private static Teacher getTeacher(ResultSet rs, Connection conn) throws SQLException {
        String login = rs.getString("login");
        PreparedStatement emp = conn.prepareStatement("SELECT * FROM employees WHERE login = ?");
        emp.setString(1, login);
        ResultSet er = emp.executeQuery();

        PreparedStatement tch = conn.prepareStatement("SELECT * FROM teachers WHERE login = ?");
        tch.setString(1, login);
        ResultSet tr = tch.executeQuery();

        if (er.next() && tr.next()) {
            List<String> specs = Arrays.asList(tr.getString("specializations").split(","));
            return new Teacher(
                    login,
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    LocalDateTime.parse(rs.getString("created_at")),
                    er.getString("department"),
                    er.getDouble("salary"),
                    TeacherTitle.valueOf(tr.getString("title")),
                    tr.getDouble("rating"),
                    tr.getInt("h_index"),
                    specs);
        }
        return null;
    }

    private static Manager getManager(ResultSet rs, Connection conn) throws SQLException {
        String login = rs.getString("login");
        PreparedStatement emp = conn.prepareStatement("SELECT * FROM employees WHERE login = ?");
        emp.setString(1, login);
        ResultSet er = emp.executeQuery();

        PreparedStatement mgr = conn.prepareStatement("SELECT * FROM managers WHERE login = ?");
        mgr.setString(1, login);
        ResultSet mr = mgr.executeQuery();

        if (er.next() && mr.next()) {
            return new Manager(
                    login,
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    LocalDateTime.parse(rs.getString("created_at")),
                    er.getString("department"),
                    er.getDouble("salary"),
                    ManagerType.valueOf(mr.getString("manager_type")));
        }
        return null;
    }

    private static Admin getAdmin(ResultSet rs, Connection conn) throws SQLException {
        String login = rs.getString("login");
        PreparedStatement emp = conn.prepareStatement("SELECT * FROM employees WHERE login = ?");
        emp.setString(1, login);
        ResultSet er = emp.executeQuery();

        PreparedStatement adm = conn.prepareStatement("SELECT * FROM admins WHERE login = ?");
        adm.setString(1, login);
        ResultSet ar = adm.executeQuery();

        if (er.next() && ar.next()) {
            return new Admin(
                    login,
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    LocalDateTime.parse(rs.getString("created_at")),
                    er.getString("department"),
                    er.getDouble("salary"),
                    ar.getInt("access_level"));
        }
        return null;
    }

    private static ResearcherEmployee getResearcherEmployee(ResultSet rs, Connection conn) throws SQLException {
        String login = rs.getString("login");
        PreparedStatement emp = conn.prepareStatement("SELECT * FROM employees WHERE login = ?");
        emp.setString(1, login);
        ResultSet er = emp.executeQuery();

        PreparedStatement res = conn.prepareStatement("SELECT * FROM researcher_employees WHERE login = ?");
        res.setString(1, login);
        ResultSet rr = res.executeQuery();

        if (er.next() && rr.next()) {
            List<String> specs = Arrays.asList(rr.getString("specializations").split(","));
            return new ResearcherEmployee(
                    login,
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    LocalDateTime.parse(rs.getString("created_at")),
                    er.getString("department"),
                    er.getDouble("salary"),
                    rr.getInt("h_index"),
                    specs);
        }
        return null;
    }
}