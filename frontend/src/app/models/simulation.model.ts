export type AutomataType = 'LIFE_GAME_2D' | 'RULE_30_1D' | 'RULE_110_1D';
export type NeighborhoodType = 'MOORE' | 'VON_NEUMANN';

export interface RuleConfig {
  automataType: AutomataType;
  neighborhoodType: NeighborhoodType;
  surviveMin: number;
  surviveMax: number;
  birthValue: number;
  ruleNumber: number;
}

export interface SimulationStepResponse {
  nextGrid: number[][];
  changedCount: number;
}

export interface PresetModel {
  id: string;
  title: string;
  description: string;
  ruleConfig: RuleConfig;
  initialGrid: number[][];
}

export interface EvaluationScenario {
  id: string;
  title: string;
  question: string;
  initialGrid: number[][];
  ruleConfig: RuleConfig;
  steps: number;
  expectedGrid: number[][];
}

export interface EvaluationResult {
  correct: boolean;
  message: string;
  diffCount: number;
}
