package com.pj.domain;

public class PresetModel {
    private String id;
    private String title;
    private String description;
    private RuleConfig ruleConfig;
    private int[][] initialGrid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RuleConfig getRuleConfig() {
        return ruleConfig;
    }

    public void setRuleConfig(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    public int[][] getInitialGrid() {
        return initialGrid;
    }

    public void setInitialGrid(int[][] initialGrid) {
        this.initialGrid = initialGrid;
    }
}
