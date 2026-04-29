package users;

/**
 * Abstract base class for all users in the system.
 */
public abstract class User {

    protected int id;
    protected String name;
    protected String email;
    protected String password;

    // ── Getters ───────────────────────────────────────────────

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // ── Setters ───────────────────────────────────────────────

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}