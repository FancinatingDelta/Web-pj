export type MultiStateRuleName =
  | 'brians_brain'
  | 'wire_world'
  | 'cyclic_ca'
  | 'generations'
  | 'three_state_1d';

export type NeighborhoodType = 'MOORE' | 'VON_NEUMANN';

export interface MultiStateRuleConfig {
  ruleName: MultiStateRuleName;
  numStates: number;
  neighborhoodType: NeighborhoodType;
  probability: number;
  threshold: number;
  birthValue: number;
}

export interface MultiStateStepResponse {
  nextGrid: number[][];
  changedCount: number;
}

export interface RuleMeta {
  ruleName: MultiStateRuleName;
  label: string;
  description: string;
  defaultNumStates: number;
  stateLabels: string[];
}
