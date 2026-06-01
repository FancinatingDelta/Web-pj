import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutomataType, RuleConfig } from '../../models/simulation.model';

@Component({
  selector: 'app-rule-editor',
  imports: [FormsModule],
  templateUrl: './rule-editor.component.html',
  styleUrl: './rule-editor.component.css'
})
export class RuleEditorComponent {
  @Input({ required: true }) ruleConfig!: RuleConfig;
  @Output() ruleConfigChange = new EventEmitter<RuleConfig>();

  readonly automataOptions: { value: AutomataType; label: string }[] = [
    { value: 'LIFE_GAME_2D', label: '生命游戏 (2D)' },
    { value: 'RULE_30_1D', label: 'Rule 30 (1D)' },
    { value: 'RULE_110_1D', label: 'Rule 110 (1D)' }
  ];

  onAutomataTypeChanged(): void {
    if (this.ruleConfig.automataType === 'LIFE_GAME_2D') {
      this.ruleConfig.ruleNumber = 0;
      this.ruleConfig.surviveMin = 2;
      this.ruleConfig.surviveMax = 3;
      this.ruleConfig.birthValue = 3;
    } else if (this.ruleConfig.automataType === 'RULE_30_1D') {
      this.ruleConfig.ruleNumber = 30;
    } else {
      this.ruleConfig.ruleNumber = 110;
    }
    this.emitChange();
  }

  emitChange(): void {
    this.ruleConfigChange.emit({ ...this.ruleConfig });
  }
}
