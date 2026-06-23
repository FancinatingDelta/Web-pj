package com.pj.controller;

import com.pj.domain.MultiStateRuleConfig;
import com.pj.domain.SimulationStepResponse;
import com.pj.service.MultiStateSimulationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/multi-state")
public class MultiStateSimulationController {

    private final MultiStateSimulationService simulationService;

    public MultiStateSimulationController(MultiStateSimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/step")
    public SimulationStepResponse step(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<List<Integer>> gridRaw = (List<List<Integer>>) body.get("grid");

        int rows = gridRaw.size();
        int cols = gridRaw.get(0).size();
        int[][] grid = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = gridRaw.get(r).get(c);
            }
        }

        MultiStateRuleConfig config = parseConfig((Map<String, Object>) body.get("config"));
        return simulationService.nextStep(grid, config);
    }

    private MultiStateRuleConfig parseConfig(Map<String, Object> raw) {
        MultiStateRuleConfig config = new MultiStateRuleConfig();
        if (raw.containsKey("ruleName")) {
            config.setRuleName((String) raw.get("ruleName"));
        }
        if (raw.containsKey("numStates")) {
            config.setNumStates(((Number) raw.get("numStates")).intValue());
        }
        if (raw.containsKey("neighborhoodType")) {
            Object nt = raw.get("neighborhoodType");
            if (nt instanceof String) {
                config.setNeighborhoodType("VON_NEUMANN".equals(nt) ? 1 : 0);
            } else if (nt instanceof Number) {
                config.setNeighborhoodType(((Number) nt).intValue());
            }
        }
        if (raw.containsKey("probability")) {
            config.setProbability(((Number) raw.get("probability")).doubleValue());
        }
        if (raw.containsKey("threshold")) {
            config.setThreshold(((Number) raw.get("threshold")).intValue());
        }
        if (raw.containsKey("birthValue")) {
            config.setBirthValue(((Number) raw.get("birthValue")).intValue());
        }
        return config;
    }
}
