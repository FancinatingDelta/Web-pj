package com.pj.domain;

public class RoomInfo {

    private String roomId;
    private String roomName;
    private int userCount;
    private long createdAt;

    public RoomInfo() {
    }

    public RoomInfo(String roomId, String roomName, int userCount, long createdAt) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.userCount = userCount;
        this.createdAt = createdAt;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
