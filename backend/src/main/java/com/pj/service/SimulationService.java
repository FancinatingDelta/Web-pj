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
            nextGrid = nextForRule(grid, ruleConfig.getRuleNumber(), ruleConfig.getProbability());
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

    /**
     * Trim empty border rows and columns so pattern comparison is
     * position-independent (e.g. a 2x2 block at (1,1) matches at (5,5)).
     */
    public int[][] normalizeGrid(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        int top = 0, bottom = rows - 1, left = 0, right = cols - 1;

        while (top < rows && rowAllZero(grid, top)) top++;
        while (bottom >= top && rowAllZero(grid, bottom)) bottom--;
        while (left < cols && colAllZero(grid, left)) left++;
        while (right >= left && colAllZero(grid, right)) right--;

        if (top > bottom || left > right) {
            return new int[][]{{0}};
        }

        int newRows = bottom - top + 1;
        int newCols = right - left + 1;
        int[][] trimmed = new int[newRows][newCols];
        for (int r = 0; r < newRows; r++) {
            for (int c = 0; c < newCols; c++) {
                trimmed[r][c] = grid[top + r][left + c];
            }
        }
        return trimmed;
    }

    private boolean rowAllZero(int[][] grid, int row) {
        for (int c = 0; c < grid[row].length; c++) {
            if (grid[row][c] != 0) return false;
        }
        return true;
    }

    private boolean colAllZero(int[][] grid, int col) {
        for (int r = 0; r < grid.length; r++) {
            if (grid[r][col] != 0) return false;
        }
        return true;
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
        double p = ruleConfig.getProbability();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int neighbors = countNeighbors(grid, r, c, ruleConfig.getNeighborhoodType());
                int nextState;
                if (grid[r][c] == 1) {
                    nextState = (neighbors >= ruleConfig.getSurviveMin() && neighbors <= ruleConfig.getSurviveMax()) ? 1 : 0;
                } else {
                    nextState = (neighbors == ruleConfig.getBirthValue()) ? 1 : 0;
                }
                if (nextState != grid[r][c] && Math.random() >= p) {
                    nextState = grid[r][c];
                }
                next[r][c] = nextState;
            }
        }
        return next;
    }

    private int[][] nextForRule(int[][] grid, int ruleNumber, double probability) {
        int cols = grid[0].length;
        int[][] next = new int[1][cols];
        for (int c = 0; c < cols; c++) {
            int left = (c == 0) ? 0 : grid[0][c - 1];
            int center = grid[0][c];
            int right = (c == cols - 1) ? 0 : grid[0][c + 1];
            int pattern = (left << 2) | (center << 1) | right;
            int nextState = (ruleNumber >> pattern) & 1;
            if (nextState != grid[0][c] && Math.random() >= probability) {
                nextState = grid[0][c];
            }
            next[0][c] = nextState;
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
