import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subscription, interval, switchMap } from 'rxjs';
import { ControlPanelComponent } from '../../components/control-panel/control-panel.component';
import { GridCanvasComponent } from '../../components/grid-canvas/grid-canvas.component';
import { RuleEditorComponent } from '../../components/rule-editor/rule-editor.component';
import { TutorialPanelComponent } from '../../components/tutorial-panel/tutorial-panel.component';
import {
  EvaluationResult,
  EvaluationScenario,
  PresetModel,
  RuleConfig
} from '../../models/simulation.model';
import { SimulationApiService } from '../../services/simulation-api.service';

@Component({
  selector: 'app-simulator',
  imports: [
    CommonModule,
    FormsModule,
    GridCanvasComponent,
    RuleEditorComponent,
    ControlPanelComponent,
    TutorialPanelComponent
  ],
  templateUrl: './simulator.component.html',
  styleUrl: './simulator.component.css'
})
export class SimulatorComponent implements OnInit, OnDestroy {
  ruleConfig: RuleConfig = {
    automataType: 'LIFE_GAME_2D',
    neighborhoodType: 'MOORE',
    surviveMin: 2,
    surviveMax: 3,
    birthValue: 3,
    ruleNumber: 0
  };
  grid: number[][] = this.createGrid(20, 20);
  history: number[][][] = [];
  generation = 0;
  isRunning = false;
  speedMs = 400;
  selectedCell: { row: number; col: number } | null = null;
  changedCount = 0;

  presets: PresetModel[] = [];
  scenarios: EvaluationScenario[] = [];
  selectedScenarioId = '';
  evaluationResult: EvaluationResult | null = null;

  private runnerSub: Subscription | null = null;

  constructor(private readonly api: SimulationApiService) {}

  ngOnInit(): void {
    this.loadPresets();
    this.loadScenarios();
  }

  ngOnDestroy(): void {
    this.stopAutoRun();
  }

  onRuleChange(nextConfig: RuleConfig): void {
    this.ruleConfig = nextConfig;
    if (this.ruleConfig.automataType === 'LIFE_GAME_2D') {
      if (this.grid.length !== 20 || this.grid[0].length !== 20) {
        this.grid = this.createGrid(20, 20);
      }
    } else {
      this.grid = [new Array(61).fill(0)];
      this.grid[0][30] = 1;
    }
    this.resetEvolutionStats();
  }

  toggleCell(position: { row: number; col: number }): void {
    this.grid[position.row][position.col] = this.grid[position.row][position.col] === 1 ? 0 : 1;
    this.grid = this.grid.map((row) => [...row]);
  }

  randomizeGrid(): void {
    if (this.ruleConfig.automataType !== 'LIFE_GAME_2D') {
      this.grid = [this.grid[0].map(() => (Math.random() > 0.7 ? 1 : 0))];
      return;
    }
    this.grid = this.grid.map((row) => row.map(() => (Math.random() > 0.7 ? 1 : 0)));
  }

  clearGrid(): void {
    this.grid = this.grid.map((row) => row.map(() => 0));
    this.resetEvolutionStats();
  }

  runSingleStep(): void {
    this.history.push(this.cloneGrid(this.grid));
    this.api.step(this.grid, this.ruleConfig).subscribe((response) => {
      this.grid = response.nextGrid;
      this.changedCount = response.changedCount;
      this.generation += 1;
    });
  }

  toggleAutoRun(): void {
    if (this.isRunning) {
      this.stopAutoRun();
      return;
    }
    this.isRunning = true;
    this.runnerSub = interval(this.speedMs)
      .pipe(switchMap(() => this.api.step(this.grid, this.ruleConfig)))
      .subscribe((response) => {
        this.history.push(this.cloneGrid(this.grid));
        this.grid = response.nextGrid;
        this.changedCount = response.changedCount;
        this.generation += 1;
      });
  }

  onSpeedChange(speed: number): void {
    this.speedMs = speed;
    if (this.isRunning) {
      this.stopAutoRun();
      this.toggleAutoRun();
    }
  }

  undoStep(): void {
    if (this.history.length === 0) {
      return;
    }
    this.grid = this.history.pop() ?? this.grid;
    this.generation = Math.max(0, this.generation - 1);
  }

  resetFromInitial(): void {
    this.stopAutoRun();
    this.clearGrid();
  }

  applyPreset(presetId: string): void {
    const preset = this.presets.find((item) => item.id === presetId);
    if (!preset) {
      return;
    }
    this.ruleConfig = { ...preset.ruleConfig };
    this.grid = this.cloneGrid(preset.initialGrid);
    this.resetEvolutionStats();
  }

  loadScenarioToCanvas(): void {
    const scenario = this.scenarios.find((item) => item.id === this.selectedScenarioId);
    if (!scenario) {
      return;
    }
    this.ruleConfig = { ...scenario.ruleConfig };
    this.grid = this.cloneGrid(scenario.initialGrid);
    this.resetEvolutionStats();
    this.evaluationResult = null;
  }

  checkCurrentScenario(): void {
    if (!this.selectedScenarioId) {
      return;
    }
    this.api.checkScenario(this.selectedScenarioId, this.grid).subscribe((result) => {
      this.evaluationResult = result;
    });
  }

  private loadPresets(): void {
    this.api.getPresets().subscribe((list) => {
      this.presets = list;
    });
  }

  private loadScenarios(): void {
    this.api.getEvaluationScenarios().subscribe((list) => {
      this.scenarios = list;
      if (list.length > 0) {
        this.selectedScenarioId = list[0].id;
      }
    });
  }

  private createGrid(rows: number, cols: number): number[][] {
    return Array.from({ length: rows }, () => Array.from({ length: cols }, () => 0));
  }

  private cloneGrid(grid: number[][]): number[][] {
    return grid.map((row) => [...row]);
  }

  private stopAutoRun(): void {
    this.isRunning = false;
    this.runnerSub?.unsubscribe();
    this.runnerSub = null;
  }

  private resetEvolutionStats(): void {
    this.stopAutoRun();
    this.generation = 0;
    this.changedCount = 0;
    this.history = [];
  }
}
