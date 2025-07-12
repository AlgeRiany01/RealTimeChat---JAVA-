package server;

import java.util.*;

public class RoomManager {
    private Map<Integer, Set<ClientHandler>> roomMembers = new HashMap<>();

    public synchronized void addMember(int roomId, ClientHandler client) {
        roomMembers.computeIfAbsent(roomId, k -> new HashSet<>()).add(client);
        broadcastNotification(roomId, "User " + client.getUsername() + " has joined the room.");
    }

    public synchronized void removeMember(int roomId, ClientHandler client) {
        Set<ClientHandler> members = roomMembers.get(roomId);
        if (members != null) {
            members.remove(client);
            broadcastNotification(roomId, "User " + client.getUsername() + " has left the room.");
            if (members.isEmpty()) roomMembers.remove(roomId);
        }
    }

    public synchronized void broadcastMessage(int roomId, String message) {
        Set<ClientHandler> members = roomMembers.get(roomId);
        if (members != null) {
            for (ClientHandler client : members) {
                client.sendMessageToClient("NEW_MESSAGE " + message);
            }
        }
    }

    public synchronized void broadcastNotification(int roomId, String notification) {
        Set<ClientHandler> members = roomMembers.get(roomId);
        if (members != null) {
            for (ClientHandler client : members) {
                client.sendMessageToClient("NOTIFICATION " + notification);
            }
        }
    }

    // دالة جديدة لإرسال قائمة المستخدمين مع البادئة USERS_LIST فقط
    public synchronized void broadcastUserList(int roomId, String userListMessage) {
        Set<ClientHandler> members = roomMembers.get(roomId);
        if (members != null) {
            for (ClientHandler client : members) {
                client.sendMessageToClient(userListMessage);
            }
        }
    }

    public synchronized Set<String> getCurrentUsersInRoom(int roomId) {
        Set<ClientHandler> members = roomMembers.get(roomId);
        Set<String> usernames = new HashSet<>();
        if (members != null) {
            for (ClientHandler client : members) {
                usernames.add(client.getUsername());
            }
        }
        return usernames;
    }
}
