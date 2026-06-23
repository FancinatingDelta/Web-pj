import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges } from '@angular/core';
import { MultiStateRuleName } from '../../models/multi-state.model';

interface LegendEntry {
  state: number;
  color: string;
  label: string;
}

const STATE_COLORS = ['#e5e7eb', '#22c55e', '#3b82f6', '#ef4444', '#eab308'];

const STATE_LABELS: Record<MultiStateRuleName, string[]> = {
  brians_brain: ['死', '激发', '不应期'],
  wire_world: ['空', '电子头', '电子尾', '导体'],
  cyclic_ca: ['状态 0', '状态 1', '状态 2', '状态 3', '状态 4'],
  generations: ['代数 0(死)', '代数 1', '代数 2', '代数 3', '代数 4'],
  three_state_1d: ['状态 0', '状态 1', '状态 2'],
};

@Component({
  selector: 'app-multi-state-legend',
  imports: [CommonModule],
  templateUrl: './multi-state-legend.component.html',
  styleUrl: './multi-state-legend.component.css',
})
export class MultiStateLegendComponent implements OnChanges {
  @Input() ruleName: MultiStateRuleName = 'brians_brain';
  @Input() numStates = 3;

  entries: LegendEntry[] = [];

  ngOnChanges(): void {
    const labels = STATE_LABELS[this.ruleName] || STATE_LABELS['brians_brain'];
    this.entries = [];
    for (let i = 0; i < this.numStates; i++) {
      this.entries.push({
        state: i,
        color: STATE_COLORS[i],
        label: labels[i] || `状态 ${i}`,
      });
    }
  }
}
