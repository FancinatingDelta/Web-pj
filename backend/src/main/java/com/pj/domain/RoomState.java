package com.pj.domain;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class RoomState {

    private String roomId;
    private String roomName;
    private Set<String> users = new CopyOnWriteArraySet<>();
    private int[][] grid;
    private RuleConfig ruleConfig;
    private int generation;
    private int changedCount;
    private long createdAt;

    public RoomState() {
    }

    public RoomState(String roomId, String roomName, int[][] grid, RuleConfig ruleConfig) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.grid = grid;
        this.ruleConfig = ruleConfig;
        this.generation = 0;
        this.changedCount = 0;
        this.createdAt = System.currentTimeMillis();
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

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public RuleConfig getRuleConfig() {
        return ruleConfig;
    }

    public void setRuleConfig(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getChangedCount() {
        return changedCount;
    }

    public void setChangedCount(int changedCount) {
        this.changedCount = changedCount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
