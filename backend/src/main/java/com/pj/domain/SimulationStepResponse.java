package com.pj.domain;

public class SimulationStepResponse {
    private int[][] nextGrid;
    private int changedCount;

    public SimulationStepResponse() {
    }

    public SimulationStepResponse(int[][] nextGrid, int changedCount) {
        this.nextGrid = nextGrid;
        this.changedCount = changedCount;
    }

    public int[][] getNextGrid() {
        return nextGrid;
    }

    public void setNextGrid(int[][] nextGrid) {
        this.nextGrid = nextGrid;
    }

    public int getChangedCount() {
        return changedCount;
    }

    public void setChangedCount(int changedCount) {
        this.changedCount = changedCount;
    }
}
