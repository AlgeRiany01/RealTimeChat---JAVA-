package server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Set;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Database db;
    private RoomManager roomManager;

    private String username;
    private int userId;
    private int currentRoomId = -1;

    public ClientHandler(Socket socket, Database db, RoomManager roomManager) {
        this.socket = socket;
        this.db = db;
        this.roomManager = roomManager;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {

            writer.println("ENTER_USERNAME");
            username = reader.readLine();
            userId = db.addOrGetUser(username);


            writer.println("ROOMS_LIST " + String.join(",", db.getAllRooms()));
            writer.println("SELECT_ROOM");


            String roomName = reader.readLine();
            currentRoomId = db.addOrGetRoom(roomName);
            db.addMemberToRoom(userId, currentRoomId);


            roomManager.addMember(currentRoomId, this);

     
            writer.println("OLD_MESSAGES_START");
            for (String msg : db.getMessagesForRoom(currentRoomId)) {
                writer.println("NEW_MESSAGE " + msg);
            }
            writer.println("OLD_MESSAGES_END");

     
            writer.println("JOINED_ROOM " + roomName);

      
            roomManager.broadcastNotification(currentRoomId, username + " has joined the room.");

         
            broadcastUserList();

    
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("/logout")) break;

                db.addMessage(userId, currentRoomId, line);
                roomManager.broadcastMessage(currentRoomId,  line);
                broadcastUserList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
               
                roomManager.broadcastNotification(currentRoomId, username + " has left the room.");

                roomManager.removeMember(currentRoomId, this);

                broadcastUserList();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToClient(String msg) {
        writer.println(msg);
    }

    public String getUsername() {
        return username;
    }

   
    private void broadcastUserList() {
        Set<String> users = roomManager.getCurrentUsersInRoom(currentRoomId);
        String message = "USERS_LIST " + String.join(",", users);
        roomManager.broadcastUserList(currentRoomId, message);
    }
}
