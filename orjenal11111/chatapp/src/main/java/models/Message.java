package models;

import java.sql.Timestamp;

public class Message {
    private int id;
    private User user;
    private Room room;
    private String content;
    private Timestamp sentAt;

    public Message(int id, User user, Room room, String content, Timestamp sentAt) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.content = content;
        this.sentAt = sentAt;
    }

    // getters
    public int getId() { return id; }
    public User getUser() { return user; }
    public Room getRoom() { return room; }
    public String getContent() { return content; }
    public Timestamp getSentAt() { return sentAt; }
}
