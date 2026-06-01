package com.pj.service;

import com.pj.domain.AutomataType;
import com.pj.domain.NeighborhoodType;
import com.pj.domain.PresetModel;
import com.pj.domain.RuleConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresetService {

    public List<PresetModel> listPresets() {
        return List.of(buildGliderPreset(), buildRule30Preset(), buildRule110Preset());
    }

    private PresetModel buildGliderPreset() {
        RuleConfig config = new RuleConfig();
        config.setAutomataType(AutomataType.LIFE_GAME_2D);
        config.setNeighborhoodType(NeighborhoodType.MOORE);
        config.setSurviveMin(2);
        config.setSurviveMax(3);
        config.setBirthValue(3);
        config.setRuleNumber(0);

        int[][] grid = new int[20][20];
        grid[1][2] = 1;
        grid[2][3] = 1;
        grid[3][1] = 1;
        grid[3][2] = 1;
        grid[3][3] = 1;

        PresetModel model = new PresetModel();
        model.setId("life-game");
        model.setTitle("生命游戏 - 滑翔机");
        model.setDescription("经典 Conway 生命游戏规则 B3/S23。");
        model.setRuleConfig(config);
        model.setInitialGrid(grid);
        return model;
    }

    private PresetModel buildRule30Preset() {
        RuleConfig config = new RuleConfig();
        config.setAutomataType(AutomataType.RULE_30_1D);
        config.setNeighborhoodType(NeighborhoodType.MOORE);
        config.setRuleNumber(30);

        int[][] grid = new int[1][61];
        grid[0][30] = 1;

        PresetModel model = new PresetModel();
        model.setId("rule-30");
        model.setTitle("Rule 30 - 单点初始");
        model.setDescription("一维元胞自动机 Rule 30，展示混沌图样。");
        model.setRuleConfig(config);
        model.setInitialGrid(grid);
        return model;
    }

    private PresetModel buildRule110Preset() {
        RuleConfig config = new RuleConfig();
        config.setAutomataType(AutomataType.RULE_110_1D);
        config.setNeighborhoodType(NeighborhoodType.MOORE);
        config.setRuleNumber(110);

        int[][] grid = new int[1][61];
        grid[0][30] = 1;
        grid[0][29] = 1;

        PresetModel model = new PresetModel();
        model.setId("rule-110");
        model.setTitle("Rule 110 - 双点初始");
        model.setDescription("一维元胞自动机 Rule 110，可用于演示复杂结构。");
        model.setRuleConfig(config);
        model.setInitialGrid(grid);
        return model;
    }
}
