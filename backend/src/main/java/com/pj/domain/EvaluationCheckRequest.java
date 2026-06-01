package com.pj.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EvaluationCheckRequest {
    @NotBlank
    private String scenarioId;
    @NotNull
    private int[][] answerGrid;

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public int[][] getAnswerGrid() {
        return answerGrid;
    }

    public void setAnswerGrid(int[][] answerGrid) {
        this.answerGrid = answerGrid;
    }
}
