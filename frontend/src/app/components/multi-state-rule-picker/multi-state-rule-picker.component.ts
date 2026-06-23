import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MultiStateRuleConfig, MultiStateRuleName, RuleMeta } from '../../models/multi-state.model';

const RULES: RuleMeta[] = [
  {
    ruleName: 'brians_brain',
    label: "Brian's Brain",
    description: '经典三态元胞自动机：死→激发(2个邻居)→不应期→死。产生复杂滑翔机。',
    defaultNumStates: 3,
    stateLabels: ['死', '激发', '不应期'],
  },
  {
    ruleName: 'wire_world',
    label: 'WireWorld',
    description: '四态数字电路模拟：空/电子头/电子尾/导体。可构建逻辑门。',
    defaultNumStates: 4,
    stateLabels: ['空', '电子头', '电子尾', '导体'],
  },
  {
    ruleName: 'cyclic_ca',
    label: 'Cyclic CA',
    description: '循环优势规则：状态循环竞争，产生螺旋波图案。状态数与阈值可调。',
    defaultNumStates: 4,
    stateLabels: ['状态 0', '状态 1', '状态 2', '状态 3', '状态 4'],
  },
  {
    ruleName: 'generations',
    label: 'Generations',
    description: "多代生命游戏：活细胞经历代数后死亡。类似 Brian's Brain 的泛化。",
    defaultNumStates: 3,
    stateLabels: ['代数 0(死)', '代数 1', '代数 2', '代数 3', '代数 4'],
  },
  {
    ruleName: 'three_state_1d',
    label: '3-State 1D',
    description: '三态一维元胞自动机，基于邻域总和的规则映射。',
    defaultNumStates: 3,
    stateLabels: ['状态 0', '状态 1', '状态 2'],
  },
];

@Component({
  selector: 'app-multi-state-rule-picker',
  imports: [CommonModule, FormsModule],
  templateUrl: './multi-state-rule-picker.component.html',
  styleUrl: './multi-state-rule-picker.component.css',
})
export class MultiStateRulePickerComponent {
  @Input() config!: MultiStateRuleConfig;
  @Output() configChange = new EventEmitter<MultiStateRuleConfig>();

  rules = RULES;

  selectedRuleMeta: RuleMeta = RULES[0];

  onRuleSelect(ruleName: MultiStateRuleName): void {
    const meta = RULES.find((r) => r.ruleName === ruleName);
    if (!meta) return;
    this.selectedRuleMeta = meta;

    const updated: MultiStateRuleConfig = {
      ...this.config,
      ruleName,
      numStates: meta.defaultNumStates,
      threshold: 3,
      birthValue: 3,
    };
    this.configChange.emit(updated);
  }

  onNumStatesChange(value: number): void {
    const updated: MultiStateRuleConfig = { ...this.config, numStates: value };
    this.configChange.emit(updated);
  }

  onThresholdChange(value: number): void {
    const updated: MultiStateRuleConfig = { ...this.config, threshold: value };
    this.configChange.emit(updated);
  }

  onBirthValueChange(value: number): void {
    const updated: MultiStateRuleConfig = { ...this.config, birthValue: value };
    this.configChange.emit(updated);
  }

  onProbabilityChange(value: number): void {
    const updated: MultiStateRuleConfig = { ...this.config, probability: value };
    this.configChange.emit(updated);
  }

  onNeighborhoodChange(value: string): void {
    const updated: MultiStateRuleConfig = {
      ...this.config,
      neighborhoodType: value as 'MOORE' | 'VON_NEUMANN',
    };
    this.configChange.emit(updated);
  }

  is1D(): boolean {
    return this.config.ruleName === 'three_state_1d';
  }
}
