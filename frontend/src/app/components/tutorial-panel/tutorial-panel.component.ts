import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { NeighborhoodType } from '../../models/simulation.model';

@Component({
  selector: 'app-tutorial-panel',
  templateUrl: './tutorial-panel.component.html',
  styleUrl: './tutorial-panel.component.css'
})
export class TutorialPanelComponent implements OnChanges {
  @Output() finishTutorial = new EventEmitter<void>();
  @Input() grid: number[][] = [];
  @Input() selectedCell: { row: number; col: number } | null = null;
  @Input() neighborhoodType: NeighborhoodType = 'MOORE';
  @Input() surviveMin = 2;
  @Input() surviveMax = 3;
  @Input() birthValue = 3;

  currentStep = 0;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedCell']) {
      this.currentStep = this.selectedCell ? 1 : 0;
    }
  }

  get neighborCoords(): { row: number; col: number }[] {
    if (!this.selectedCell || this.grid.length === 0) return [];
    const { row, col } = this.selectedCell;
    const offsets =
      this.neighborhoodType === 'VON_NEUMANN'
        ? [[-1, 0], [1, 0], [0, -1], [0, 1]]
        : [[-1, -1], [-1, 0], [-1, 1], [0, -1], [0, 1], [1, -1], [1, 0], [1, 1]];
    return offsets
      .map(([dr, dc]) => ({ row: row + dr, col: col + dc }))
      .filter(({ row: nr, col: nc }) =>
        nr >= 0 && nr < this.grid.length && nc >= 0 && nc < this.grid[0].length
      );
  }

  get aliveNeighborCount(): number {
    return this.neighborCoords.filter(({ row, col }) => this.grid[row][col] === 1).length;
  }

  get neighborTypeLabel(): string {
    return this.neighborhoodType === 'MOORE' ? 'Moore（8邻域）' : 'von Neumann（4邻域）';
  }

  get step1Text(): string {
    if (!this.selectedCell) return '';
    const { row, col } = this.selectedCell;
    const state = this.grid[row][col] === 1 ? '存活' : '死亡';
    const neighbors = this.aliveNeighborCount;
    return `选中细胞 (${row}, ${col})，当前状态：${state}。在 ${this.neighborTypeLabel} 下，共有 ${this.neighborCoords.length} 个邻居，其中 ${neighbors} 个存活。`;
  }

  get step2Text(): string {
    const neighbors = this.aliveNeighborCount;
    const state = this.selectedCell ? this.grid[this.selectedCell.row][this.selectedCell.col] : 0;
    let rule = '';
    if (state === 1) {
      rule = `存活规则：邻居数需在 [${this.surviveMin}, ${this.surviveMax}] 之间。当前邻居数 ${neighbors}`;
      if (neighbors < this.surviveMin) {
        rule += ` < ${this.surviveMin}，不满足存活条件。`;
      } else if (neighbors > this.surviveMax) {
        rule += ` > ${this.surviveMax}，不满足存活条件。`;
      } else {
        rule += `，满足存活条件。`;
      }
    } else {
      rule = `复活规则：邻居数需等于 ${this.birthValue}。当前邻居数 ${neighbors}`;
      if (neighbors === this.birthValue) {
        rule += `，满足复活条件。`;
      } else {
        rule += `，不满足复活条件。`;
      }
    }
    return rule;
  }

  get step3Text(): string {
    const neighbors = this.aliveNeighborCount;
    const state = this.selectedCell ? this.grid[this.selectedCell.row][this.selectedCell.col] : 0;
    if (state === 1) {
      if (neighbors < this.surviveMin) return `预测：细胞将因 邻居不足 而死亡。`;
      if (neighbors > this.surviveMax) return `预测：细胞将因 过度拥挤 而死亡。`;
      return `预测：细胞将继续存活。`;
    }
    if (neighbors === this.birthValue) return `预测：细胞将因 满足复活条件 而复活！`;
    return `预测：细胞将保持死亡状态。`;
  }

  get predictedState(): 'alive' | 'dead' | 'birth' {
    const neighbors = this.aliveNeighborCount;
    const state = this.selectedCell ? this.grid[this.selectedCell.row][this.selectedCell.col] : 0;
    if (state === 1) {
      return (neighbors >= this.surviveMin && neighbors <= this.surviveMax) ? 'alive' : 'dead';
    }
    return neighbors === this.birthValue ? 'birth' : 'dead';
  }

  nextStep(): void {
    if (this.currentStep < 3) {
      this.currentStep++;
    } else {
      this.finish();
    }
  }

  prevStep(): void {
    if (this.currentStep > 1) this.currentStep--;
  }

  finish(): void {
    this.currentStep = 0;
    this.finishTutorial.emit();
  }
}
