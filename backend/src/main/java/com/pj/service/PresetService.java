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
        model.setDescription("滑翔机是康威生命游戏中最著名的移动模式，由 5 个活细胞组成。在 B3/S23 规则下，它会斜向穿越网格，每 4 代循环一次并平移一格。这一模式在生命游戏中具有基础性地位，常被用作信息传递的基本单元。");
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
        model.setDescription("Rule 30 是由 Stephen Wolfram 于 1983 年提出的一维元胞自动机规则，以产生混沌、非周期性的图样而著称。其演化结果在中心列呈现出高度随机性，被用于 Mathematica 的随机数生成器。从单一活细胞出发即可生成复杂的分形结构，是初等元胞自动机中最具视觉冲击力的规则之一。");
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
        model.setDescription("Rule 110 是初等元胞自动机中唯一被证明具有图灵完备性的规则，由 Matthew Cook 在 2004 年完成证明。这意味着在理论上，Rule 110 可以执行任何可计算函数。其演化图样呈现出丰富的局部结构，包括稳定背景、周期性粒子和移动结构之间的复杂相互作用，是连接简单规则与通用计算的重要桥梁。");
        model.setRuleConfig(config);
        model.setInitialGrid(grid);
        return model;
    }
}
