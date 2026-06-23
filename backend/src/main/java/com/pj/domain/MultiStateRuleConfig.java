package com.pj.domain;

public class MultiStateRuleConfig {

    public static final String RULE_BRIANS_BRAIN = "brians_brain";
    public static final String RULE_WIRE_WORLD = "wire_world";
    public static final String RULE_CYCLIC_CA = "cyclic_ca";
    public static final String RULE_GENERATIONS = "generations";
    public static final String RULE_THREE_STATE_1D = "three_state_1d";

    private String ruleName = RULE_BRIANS_BRAIN;
    private int numStates = 3;
    private int neighborhoodType = 0; // 0=MOORE, 1=VON_NEUMANN
    private double probability = 1.0;
    private int threshold = 3;        // used by cyclic_ca
    private int birthValue = 3;       // used by generations

    public MultiStateRuleConfig() {}

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public int getNumStates() { return numStates; }
    public void setNumStates(int numStates) { this.numStates = numStates; }

    public int getNeighborhoodType() { return neighborhoodType; }
    public void setNeighborhoodType(int neighborhoodType) { this.neighborhoodType = neighborhoodType; }

    public double getProbability() { return probability; }
    public void setProbability(double probability) { this.probability = probability; }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }

    public int getBirthValue() { return birthValue; }
    public void setBirthValue(int birthValue) { this.birthValue = birthValue; }
}
