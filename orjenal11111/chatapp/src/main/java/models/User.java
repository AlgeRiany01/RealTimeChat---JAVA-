package models;

public class User {
    private int id;
    private String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    // getters
    public int getId() { return id; }
    public String getUsername() { return username; }

    // setters (إذا احتجت)
    public void setUsername(String username) { this.username = username; }
}
