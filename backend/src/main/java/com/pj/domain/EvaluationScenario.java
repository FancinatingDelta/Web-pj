package com.pj.domain;

public class EvaluationScenario {
    private String id;
    private String title;
    private String question;
    private int[][] initialGrid;
    private RuleConfig ruleConfig;
    private int steps;
    private int[][] expectedGrid;

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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int[][] getInitialGrid() {
        return initialGrid;
    }

    public void setInitialGrid(int[][] initialGrid) {
        this.initialGrid = initialGrid;
    }

    public RuleConfig getRuleConfig() {
        return ruleConfig;
    }

    public void setRuleConfig(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int[][] getExpectedGrid() {
        return expectedGrid;
    }

    public void setExpectedGrid(int[][] expectedGrid) {
        this.expectedGrid = expectedGrid;
    }
}
