package com.pj.service;

import com.pj.domain.AutomataType;
import com.pj.domain.NeighborhoodType;
import com.pj.domain.RuleConfig;
import com.pj.domain.SimulationStepResponse;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

    public SimulationStepResponse nextStep(int[][] grid, RuleConfig ruleConfig) {
        int[][] nextGrid;
        if (ruleConfig.getAutomataType() == AutomataType.LIFE_GAME_2D) {
            nextGrid = nextForLifeGame(grid, ruleConfig);
        } else {
            nextGrid = nextForRule(grid, ruleConfig.getRuleNumber());
        }
        return new SimulationStepResponse(nextGrid, calculateChangedCount(grid, nextGrid));
    }

    public int[][] runSteps(int[][] grid, RuleConfig ruleConfig, int steps) {
        int[][] current = copyGrid(grid);
        for (int i = 0; i < steps; i++) {
            current = nextStep(current, ruleConfig).getNextGrid();
        }
        return current;
    }

    public int calculateDiffCount(int[][] expected, int[][] actual) {
        if (expected.length != actual.length || expected[0].length != actual[0].length) {
            return Integer.MAX_VALUE;
        }
        int diff = 0;
        for (int r = 0; r < expected.length; r++) {
            for (int c = 0; c < expected[r].length; c++) {
                if (expected[r][c] != actual[r][c]) {
                    diff++;
                }
            }
        }
        return diff;
    }

    private int[][] nextForLifeGame(int[][] grid, RuleConfig ruleConfig) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] next = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int neighbors = countNeighbors(grid, r, c, ruleConfig.getNeighborhoodType());
                if (grid[r][c] == 1) {
                    next[r][c] = (neighbors >= ruleConfig.getSurviveMin() && neighbors <= ruleConfig.getSurviveMax()) ? 1 : 0;
                } else {
                    next[r][c] = (neighbors == ruleConfig.getBirthValue()) ? 1 : 0;
                }
            }
        }
        return next;
    }

    private int[][] nextForRule(int[][] grid, int ruleNumber) {
        int cols = grid[0].length;
        int[][] next = new int[1][cols];
        for (int c = 0; c < cols; c++) {
            int left = (c == 0) ? 0 : grid[0][c - 1];
            int center = grid[0][c];
            int right = (c == cols - 1) ? 0 : grid[0][c + 1];
            int pattern = (left << 2) | (center << 1) | right;
            next[0][c] = (ruleNumber >> pattern) & 1;
        }
        return next;
    }

    private int countNeighbors(int[][] grid, int row, int col, NeighborhoodType neighborhoodType) {
        int count = 0;
        int[][] offsets = neighborhoodType == NeighborhoodType.VON_NEUMANN
                ? new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}
                : new int[][]{
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] offset : offsets) {
            int nr = row + offset[0];
            int nc = col + offset[1];
            if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[0].length) {
                count += grid[nr][nc];
            }
        }
        return count;
    }

    private int calculateChangedCount(int[][] source, int[][] target) {
        int changed = 0;
        for (int r = 0; r < source.length; r++) {
            for (int c = 0; c < source[r].length; c++) {
                if (source[r][c] != target[r][c]) {
                    changed++;
                }
            }
        }
        return changed;
    }

    private int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, grid[0].length);
        }
        return copy;
    }
}
