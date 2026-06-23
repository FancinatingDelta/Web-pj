import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subscription, interval } from 'rxjs';
import { switchMap, takeWhile } from 'rxjs/operators';
import { MultiStateRuleConfig, MultiStateRuleName } from '../../models/multi-state.model';
import { MultiStateApiService } from '../../services/multi-state-api.service';
import { MultiStateGridComponent } from '../../components/multi-state-grid/multi-state-grid.component';
import { MultiStateLegendComponent } from '../../components/multi-state-legend/multi-state-legend.component';
import { MultiStateRulePickerComponent } from '../../components/multi-state-rule-picker/multi-state-rule-picker.component';

@Component({
  selector: 'app-multi-state-simulator',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MultiStateGridComponent,
    MultiStateLegendComponent,
    MultiStateRulePickerComponent,
  ],
  templateUrl: './multi-state-simulator.component.html',
  styleUrl: './multi-state-simulator.component.css',
})
export class MultiStateSimulatorComponent implements OnInit, OnDestroy {
  config: MultiStateRuleConfig = {
    ruleName: 'brians_brain',
    numStates: 3,
    neighborhoodType: 'MOORE',
    probability: 1,
    threshold: 3,
    birthValue: 3,
  };

  grid: number[][] = [];
  gridRows = 20;
  gridCols = 20;
  generation = 0;
  isRunning = false;
  speedMs = 400;
  changedCount = 0;
  editMode = true;
  selectedCell: { row: number; col: number } | null = null;
  history: number[][][] = [];

  private autoSub?: Subscription;

  constructor(private api: MultiStateApiService) {}

  ngOnInit(): void {
    this.initGrid();
  }

  ngOnDestroy(): void {
    this.stopAuto();
  }

  initGrid(): void {
    const rows = this.is1D ? 1 : this.gridRows;
    const cols = this.gridCols;
    this.grid = Array.from({ length: rows }, () => new Array(cols).fill(0));
    this.generation = 0;
    this.history = [];
    this.changedCount = 0;
  }

  randomize(): void {
    const rows = this.is1D ? 1 : this.gridRows;
    const cols = this.gridCols;
    this.grid = Array.from({ length: rows }, () =>
      Array.from({ length: cols }, () => Math.floor(Math.random() * this.config.numStates)),
    );
    this.generation = 0;
    this.history = [];
    this.changedCount = 0;
  }

  clear(): void {
    this.initGrid();
  }

  onResize(rows: number, cols: number): void {
    this.gridRows = rows;
    this.gridCols = cols;
    this.initGrid();
  }

  onCellToggle(pos: { row: number; col: number }): void {
    const newGrid = this.grid.map((r) => [...r]);
    newGrid[pos.row][pos.col] = (newGrid[pos.row][pos.col] + 1) % this.config.numStates;
    this.grid = newGrid;
  }

  onCellSelect(pos: { row: number; col: number } | null): void {
    this.selectedCell = pos;
  }

  stepOnce(): void {
    this.history.push(this.grid.map((r) => [...r]));
    this.api.step(this.grid, this.config).subscribe({
      next: (res) => {
        this.grid = res.nextGrid;
        this.changedCount = res.changedCount;
        this.generation++;
      },
      error: (err) => console.error('Step failed:', err),
    });
  }

  toggleAuto(): void {
    if (this.isRunning) {
      this.stopAuto();
    } else {
      this.startAuto();
    }
  }

  private startAuto(): void {
    this.isRunning = true;
    this.autoSub = interval(this.speedMs)
      .pipe(
        takeWhile(() => this.isRunning),
        switchMap(() => {
          this.history.push(this.grid.map((r) => [...r]));
          return this.api.step(this.grid, this.config);
        }),
      )
      .subscribe({
        next: (res) => {
          this.grid = res.nextGrid;
          this.changedCount = res.changedCount;
          this.generation++;
        },
        error: (err) => {
          console.error('Auto failed:', err);
          this.stopAuto();
        },
      });
  }

  private stopAuto(): void {
    this.isRunning = false;
    this.autoSub?.unsubscribe();
  }

  onSpeedChange(value: number): void {
    this.speedMs = value;
    if (this.isRunning) {
      this.stopAuto();
      this.startAuto();
    }
  }

  jumpToGeneration(gen: number): void {
    if (gen < 0 || gen >= this.history.length) return;
    this.stopAuto();
    this.grid = this.history[gen].map((r) => [...r]);
    this.generation = gen;
    this.history = this.history.slice(0, gen);
  }

  onConfigChange(newConfig: MultiStateRuleConfig): void {
    const ruleChanged = newConfig.ruleName !== this.config.ruleName;
    this.config = newConfig;
    if (ruleChanged) {
      this.initGrid();
    }
  }

  get is1D(): boolean {
    return this.config.ruleName === 'three_state_1d';
  }
}
