package staff;

import users.User;

/**
 * import users.*;
 * 
 * Represents a super administrator with full system access.
 */
public class SuperAdmin extends User {

    private String accessLevel;
    private boolean canCreateUsers;
    private boolean canDeleteUsers;
    private boolean canModifyGrades;
    private String lastLoginDate;

    // ── Constructors ──────────────────────────────────────────

    public SuperAdmin() {
        super();
        this.accessLevel = "FULL";
        this.canCreateUsers = true;
        this.canDeleteUsers = true;
        this.canModifyGrades = true;
    }

    public SuperAdmin(int id, String name, String email, String password) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // ── Getters ───────────────────────────────────────────────

    public String getAccessLevel() {
        return accessLevel;
    }

    public boolean canCreateUsers() {
        return canCreateUsers;
    }

    public boolean canDeleteUsers() {
        return canDeleteUsers;
    }

    public boolean canModifyGrades() {
        return canModifyGrades;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    // ── Setters ───────────────────────────────────────────────

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setCanCreateUsers(boolean canCreateUsers) {
        this.canCreateUsers = canCreateUsers;
    }

    public void setCanDeleteUsers(boolean canDeleteUsers) {
        this.canDeleteUsers = canDeleteUsers;
    }

    public void setCanModifyGrades(boolean canModifyGrades) {
        this.canModifyGrades = canModifyGrades;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}