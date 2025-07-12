package models;

public class Room {
    private int id;
    private String name;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }

    // setters
    public void setName(String name) { this.name = name; }
}
