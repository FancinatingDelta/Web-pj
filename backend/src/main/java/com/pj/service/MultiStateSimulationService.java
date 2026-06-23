package com.pj.service;

import com.pj.domain.MultiStateRuleConfig;
import com.pj.domain.SimulationStepResponse;
import org.springframework.stereotype.Service;

@Service
public class MultiStateSimulationService {

    private static final int[][] MOORE_OFFSETS = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1},           {0, 1},
        {1, -1},  {1, 0},  {1, 1}
    };

    private static final int[][] VON_NEUMANN_OFFSETS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    public SimulationStepResponse nextStep(int[][] grid, MultiStateRuleConfig config) {
        int[][] nextGrid;
        switch (config.getRuleName()) {
            case MultiStateRuleConfig.RULE_BRIANS_BRAIN:
                nextGrid = nextBriansBrain(grid, config);
                break;
            case MultiStateRuleConfig.RULE_WIRE_WORLD:
                nextGrid = nextWireWorld(grid, config);
                break;
            case MultiStateRuleConfig.RULE_CYCLIC_CA:
                nextGrid = nextCyclicCA(grid, config);
                break;
            case MultiStateRuleConfig.RULE_GENERATIONS:
                nextGrid = nextGenerations(grid, config);
                break;
            case MultiStateRuleConfig.RULE_THREE_STATE_1D:
                nextGrid = nextThreeState1D(grid, config);
                break;
            default:
                nextGrid = nextBriansBrain(grid, config);
        }
        return new SimulationStepResponse(nextGrid, calculateChangedCount(grid, nextGrid));
    }

    // ==================== Brian's Brain ====================
    // States: 0=dead, 1=firing, 2=refractory
    // 0→1 if exactly 2 firing neighbors; 1→2 always; 2→0 always
    private int[][] nextBriansBrain(int[][] grid, MultiStateRuleConfig config) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] next = new int[rows][cols];
        double p = config.getProbability();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int state = grid[r][c];
                int firingNeighbors = countNeighborsInState(grid, r, c, config, 1);
                int nextState;

                if (state == 0) {
                    nextState = (firingNeighbors == 2) ? 1 : 0;
                } else if (state == 1) {
                    nextState = 2;
                } else {
                    nextState = 0;
                }

                if (nextState != state && Math.random() >= p) {
                    nextState = state;
                }
                next[r][c] = nextState;
            }
        }
        return next;
    }

    // ==================== WireWorld ====================
    // States: 0=empty, 1=electron-head, 2=electron-tail, 3=conductor
    // 0→0; 1→2; 2→3; 3→1 if 1 or 2 head neighbors, else 3
    private int[][] nextWireWorld(int[][] grid, MultiStateRuleConfig config) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] next = new int[rows][cols];
        double p = config.getProbability();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int state = grid[r][c];
                int headNeighbors = countNeighborsInState(grid, r, c, config, 1);
                int nextState;

                if (state == 0) {
                    nextState = 0;
                } else if (state == 1) {
                    nextState = 2;
                } else if (state == 2) {
                    nextState = 3;
                } else {
                    nextState = (headNeighbors == 1 || headNeighbors == 2) ? 1 : 3;
                }

                if (nextState != state && Math.random() >= p) {
                    nextState = state;
                }
                next[r][c] = nextState;
            }
        }
        return next;
    }

    // ==================== Cyclic CA ====================
    // States: 0..N-1 cyclically dominate each other.
    // Cell in state s → (s+1)%N if ≥ threshold neighbors are in state (s+1)%N.
    private int[][] nextCyclicCA(int[][] grid, MultiStateRuleConfig config) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] next = new int[rows][cols];
        int n = config.getNumStates();
        int threshold = config.getThreshold();
        double p = config.getProbability();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int state = grid[r][c];
                int successor = (state + 1) % n;
                int successorNeighbors = countNeighborsInState(grid, r, c, config, successor);
                int nextState = (successorNeighbors >= threshold) ? successor : state;

                if (nextState != state && Math.random() >= p) {
                    nextState = state;
                }
                next[r][c] = nextState;
            }
        }
        return next;
    }

    // ==================== Generations ====================
    // States: 0=dead, 1..N-1 = alive generations (age).
    // 0→1 if alive-neighbor-count == birthValue; k→k+1 (k < N-1); N-1→0
    private int[][] nextGenerations(int[][] grid, MultiStateRuleConfig config) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] next = new int[rows][cols];
        int n = config.getNumStates();
        int birthValue = config.getBirthValue();
        double p = config.getProbability();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int state = grid[r][c];
                int aliveNeighbors = countAliveNeighbors(grid, r, c, config);
                int nextState;

                if (state == 0) {
                    nextState = (aliveNeighbors == birthValue) ? 1 : 0;
                } else if (state < n - 1) {
                    nextState = state + 1;
                } else {
                    nextState = 0;
                }

                if (nextState != state && Math.random() >= p) {
                    nextState = state;
                }
                next[r][c] = nextState;
            }
        }
        return next;
    }

    // ==================== Three-State 1D ====================
    // States: 0, 1, 2. Totalistic rule on 3-cell neighborhood.
    // Sum of (left+center+right) maps to next state via fixed lookup.
    private int[][] nextThreeState1D(int[][] grid, MultiStateRuleConfig config) {
        int cols = grid[0].length;
        int[][] next = new int[1][cols];
        double p = config.getProbability();

        // Totalistic lookup: sum 0..6 → next state (0/1/2)
        int[] lookup = {0, 2, 1, 0, 1, 2, 0};

        for (int c = 0; c < cols; c++) {
            int left = (c == 0) ? 0 : grid[0][c - 1];
            int center = grid[0][c];
            int right = (c == cols - 1) ? 0 : grid[0][c + 1];
            int sum = left + center + right;
            int nextState = lookup[sum];

            if (nextState != center && Math.random() >= p) {
                nextState = center;
            }
            next[0][c] = nextState;
        }
        return next;
    }

    // ==================== Helpers ====================

    private int countNeighborsInState(int[][] grid, int row, int col,
                                      MultiStateRuleConfig config, int targetState) {
        int[][] offsets = (config.getNeighborhoodType() == 1)
                ? VON_NEUMANN_OFFSETS : MOORE_OFFSETS;
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int[] offset : offsets) {
            int nr = row + offset[0];
            int nc = col + offset[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                if (grid[nr][nc] == targetState) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countAliveNeighbors(int[][] grid, int row, int col,
                                    MultiStateRuleConfig config) {
        int[][] offsets = (config.getNeighborhoodType() == 1)
                ? VON_NEUMANN_OFFSETS : MOORE_OFFSETS;
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int[] offset : offsets) {
            int nr = row + offset[0];
            int nc = col + offset[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                if (grid[nr][nc] != 0) {
                    count++;
                }
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
}
