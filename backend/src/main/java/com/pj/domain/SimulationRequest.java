package com.pj.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class SimulationRequest {
    @NotNull
    private int[][] grid;
    @NotNull
    @Valid
    private RuleConfig ruleConfig;

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
}
