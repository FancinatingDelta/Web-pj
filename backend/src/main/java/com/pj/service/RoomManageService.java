package com.pj.service;

import com.pj.domain.RoomInfo;
import com.pj.domain.RoomState;
import com.pj.domain.RuleConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomManageService {

    private final Map<String, RoomState> rooms = new ConcurrentHashMap<>();
    private final SimulationService simulationService;

    public RoomManageService(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    public RoomState createRoom(String roomId, String roomName, String username, int rows, int cols, RuleConfig ruleConfig) {
        int[][] grid = new int[rows][cols];
        RoomState room = new RoomState(roomId, roomName, grid, ruleConfig);
        room.getUsers().add(username);
        rooms.put(roomId, room);
        return room;
    }

    public RoomState joinRoom(String roomId, String username) {
        RoomState room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        room.getUsers().add(username);
        return room;
    }

    public RoomState leaveRoom(String roomId, String username) {
        RoomState room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        room.getUsers().remove(username);
        if (room.getUsers().isEmpty()) {
            rooms.remove(roomId);
            return null;
        }
        return room;
    }

    public RoomState getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public List<RoomInfo> getRoomList() {
        List<RoomInfo> list = new ArrayList<>();
        for (RoomState room : rooms.values()) {
            list.add(new RoomInfo(
                    room.getRoomId(),
                    room.getRoomName(),
                    room.getUsers().size(),
                    room.getCreatedAt()
            ));
        }
        return list;
    }

    public RoomState toggleCell(String roomId, int row, int col) {
        RoomState room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        int[][] grid = room.getGrid();
        if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
            grid[row][col] = grid[row][col] == 1 ? 0 : 1;
        }
        return room;
    }

    public RoomState step(String roomId) {
        RoomState room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        var response = simulationService.nextStep(room.getGrid(), room.getRuleConfig());
        room.setGrid(response.getNextGrid());
        room.setChangedCount(response.getChangedCount());
        room.setGeneration(room.getGeneration() + 1);
        return room;
    }

    public RoomState updateRule(String roomId, RuleConfig ruleConfig) {
        RoomState room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        room.setRuleConfig(ruleConfig);
        return room;
    }

    public RoomState resizeGrid(String roomId, int rows, int cols) {
        RoomState room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        int[][] newGrid = new int[rows][cols];
        int[][] oldGrid = room.getGrid();
        for (int r = 0; r < Math.min(rows, oldGrid.length); r++) {
            for (int c = 0; c < Math.min(cols, oldGrid[0].length); c++) {
                newGrid[r][c] = oldGrid[r][c];
            }
        }
        room.setGrid(newGrid);
        room.setGeneration(0);
        room.setChangedCount(0);
        return room;
    }

    public RoomState randomize(String roomId) {
        RoomState room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        int[][] grid = room.getGrid();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = Math.random() > 0.7 ? 1 : 0;
            }
        }
        room.setGeneration(0);
        room.setChangedCount(0);
        return room;
    }

    public RoomState clear(String roomId) {
        RoomState room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        int[][] grid = room.getGrid();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = 0;
            }
        }
        room.setGeneration(0);
        room.setChangedCount(0);
        return room;
    }
}
