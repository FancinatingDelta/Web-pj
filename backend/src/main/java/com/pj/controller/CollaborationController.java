package com.pj.controller;

import com.pj.domain.RoomInfo;
import com.pj.domain.RoomState;
import com.pj.domain.RuleConfig;
import com.pj.service.RoomManageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CollaborationController {

    private final RoomManageService roomManageService;
    private final SimpMessagingTemplate messagingTemplate;

    public CollaborationController(RoomManageService roomManageService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.roomManageService = roomManageService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/api/rooms")
    public List<RoomInfo> getRooms() {
        return roomManageService.getRoomList();
    }

    @MessageMapping("/room/create")
    public void createRoom(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");
        String roomName = (String) payload.getOrDefault("roomName", "未命名房间");
        String username = (String) payload.get("username");
        int rows = payload.containsKey("rows") ? ((Number) payload.get("rows")).intValue() : 20;
        int cols = payload.containsKey("cols") ? ((Number) payload.get("cols")).intValue() : 20;

        RuleConfig ruleConfig = new RuleConfig();
        ruleConfig.setAutomataType(com.pj.domain.AutomataType.LIFE_GAME_2D);
        ruleConfig.setNeighborhoodType(com.pj.domain.NeighborhoodType.MOORE);
        ruleConfig.setSurviveMin(2);
        ruleConfig.setSurviveMax(3);
        ruleConfig.setBirthValue(3);
        ruleConfig.setRuleNumber(0);
        ruleConfig.setProbability(1.0);

        RoomState room = roomManageService.createRoom(roomId, roomName, username, rows, cols, ruleConfig);

        broadcastState(room.getRoomId());
        broadcastRoomList();
    }

    @MessageMapping("/room/join")
    public void joinRoom(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");
        String username = (String) payload.get("username");

        RoomState room = roomManageService.joinRoom(roomId, username);
        if (room == null) {
            return;
        }
        broadcastState(roomId);
        broadcastRoomList();
    }

    @MessageMapping("/room/leave")
    public void leaveRoom(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");
        String username = (String) payload.get("username");

        RoomState room = roomManageService.leaveRoom(roomId, username);
        if (room != null) {
            broadcastState(roomId);
        }
        broadcastRoomList();
    }

    @MessageMapping("/room/cell-toggle")
    public void toggleCell(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");
        int row = ((Number) payload.get("row")).intValue();
        int col = ((Number) payload.get("col")).intValue();

        RoomState room = roomManageService.toggleCell(roomId, row, col);
        if (room != null) {
            broadcastState(roomId);
        }
    }

    @MessageMapping("/room/step")
    public void step(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");

        RoomState room = roomManageService.step(roomId);
        if (room != null) {
            broadcastState(roomId);
        }
    }

    @MessageMapping("/room/rule-change")
    public void ruleChange(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");
        Map<String, Object> rcMap = (Map<String, Object>) payload.get("ruleConfig");

        RuleConfig ruleConfig = mapToRuleConfig(rcMap);
        RoomState room = roomManageService.updateRule(roomId, ruleConfig);
        if (room != null) {
            broadcastState(roomId);
        }
    }

    @MessageMapping("/room/grid-resize")
    public void gridResize(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");
        int rows = ((Number) payload.get("rows")).intValue();
        int cols = ((Number) payload.get("cols")).intValue();

        RoomState room = roomManageService.resizeGrid(roomId, rows, cols);
        if (room != null) {
            broadcastState(roomId);
        }
    }

    @MessageMapping("/room/randomize")
    public void randomize(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");

        RoomState room = roomManageService.randomize(roomId);
        if (room != null) {
            broadcastState(roomId);
        }
    }

    @MessageMapping("/room/clear")
    public void clear(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");

        RoomState room = roomManageService.clear(roomId);
        if (room != null) {
            broadcastState(roomId);
        }
    }

    @MessageMapping("/room/chat")
    public void chat(@Payload Map<String, Object> payload) {
        String roomId = (String) payload.get("roomId");
        String username = (String) payload.get("username");
        String content = (String) payload.get("content");

        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/chat",
                Map.of("username", username, "content", content, "timestamp", System.currentTimeMillis()));
    }

    private void broadcastState(String roomId) {
        RoomState room = roomManageService.getRoom(roomId);
        if (room != null) {
            messagingTemplate.convertAndSend("/topic/room/" + roomId + "/state", room);
        }
    }

    private void broadcastRoomList() {
        messagingTemplate.convertAndSend("/topic/rooms", roomManageService.getRoomList());
    }

    private RuleConfig mapToRuleConfig(Map<String, Object> map) {
        RuleConfig rc = new RuleConfig();
        if (map.containsKey("automataType")) {
            rc.setAutomataType(com.pj.domain.AutomataType.valueOf((String) map.get("automataType")));
        }
        if (map.containsKey("neighborhoodType")) {
            rc.setNeighborhoodType(com.pj.domain.NeighborhoodType.valueOf((String) map.get("neighborhoodType")));
        }
        if (map.containsKey("surviveMin")) {
            rc.setSurviveMin(((Number) map.get("surviveMin")).intValue());
        }
        if (map.containsKey("surviveMax")) {
            rc.setSurviveMax(((Number) map.get("surviveMax")).intValue());
        }
        if (map.containsKey("birthValue")) {
            rc.setBirthValue(((Number) map.get("birthValue")).intValue());
        }
        if (map.containsKey("ruleNumber")) {
            rc.setRuleNumber(((Number) map.get("ruleNumber")).intValue());
        }
        if (map.containsKey("probability")) {
            rc.setProbability(((Number) map.get("probability")).doubleValue());
        }
        return rc;
    }
}
