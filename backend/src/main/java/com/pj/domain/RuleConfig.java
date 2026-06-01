package com.pj.domain;

public class RuleConfig {
    private AutomataType automataType;
    private NeighborhoodType neighborhoodType;
    private int surviveMin;
    private int surviveMax;
    private int birthValue;
    private int ruleNumber;

    public AutomataType getAutomataType() {
        return automataType;
    }

    public void setAutomataType(AutomataType automataType) {
        this.automataType = automataType;
    }

    public NeighborhoodType getNeighborhoodType() {
        return neighborhoodType;
    }

    public void setNeighborhoodType(NeighborhoodType neighborhoodType) {
        this.neighborhoodType = neighborhoodType;
    }

    public int getSurviveMin() {
        return surviveMin;
    }

    public void setSurviveMin(int surviveMin) {
        this.surviveMin = surviveMin;
    }

    public int getSurviveMax() {
        return surviveMax;
    }

    public void setSurviveMax(int surviveMax) {
        this.surviveMax = surviveMax;
    }

    public int getBirthValue() {
        return birthValue;
    }

    public void setBirthValue(int birthValue) {
        this.birthValue = birthValue;
    }

    public int getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(int ruleNumber) {
        this.ruleNumber = ruleNumber;
    }
}
