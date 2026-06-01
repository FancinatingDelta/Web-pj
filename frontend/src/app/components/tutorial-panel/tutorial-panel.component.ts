import { Component, Input } from '@angular/core';
import { NeighborhoodType } from '../../models/simulation.model';

@Component({
  selector: 'app-tutorial-panel',
  templateUrl: './tutorial-panel.component.html',
  styleUrl: './tutorial-panel.component.css'
})
export class TutorialPanelComponent {
  @Input() grid: number[][] = [];
  @Input() selectedCell: { row: number; col: number } | null = null;
  @Input() neighborhoodType: NeighborhoodType = 'MOORE';
  @Input() surviveMin = 2;
  @Input() surviveMax = 3;
  @Input() birthValue = 3;

  get explanation(): string {
    if (!this.selectedCell || this.grid.length === 0) {
      return '点击画布中的一个细胞，可查看其邻域统计与下一步状态推断。';
    }
    const { row, col } = this.selectedCell;
    const currentState = this.grid[row][col];
    const neighbors = this.countNeighbors(row, col);
    if (currentState === 1) {
      if (neighbors < this.surviveMin) {
        return `当前细胞存活，邻居数为 ${neighbors}，低于生存下限 ${this.surviveMin}，下一步会死亡。`;
      }
      if (neighbors > this.surviveMax) {
        return `当前细胞存活，邻居数为 ${neighbors}，高于生存上限 ${this.surviveMax}，下一步会死亡。`;
      }
      return `当前细胞存活，邻居数为 ${neighbors}，位于生存区间 [${this.surviveMin}, ${this.surviveMax}]，下一步继续存活。`;
    }
    if (neighbors === this.birthValue) {
      return `当前细胞死亡，邻居数为 ${neighbors}，满足复活条件 ${this.birthValue}，下一步会复活。`;
    }
    return `当前细胞死亡，邻居数为 ${neighbors}，不满足复活条件 ${this.birthValue}，下一步保持死亡。`;
  }

  private countNeighbors(row: number, col: number): number {
    const offsets =
      this.neighborhoodType === 'VON_NEUMANN'
        ? [
            [-1, 0],
            [1, 0],
            [0, -1],
            [0, 1]
          ]
        : [
            [-1, -1],
            [-1, 0],
            [-1, 1],
            [0, -1],
            [0, 1],
            [1, -1],
            [1, 0],
            [1, 1]
          ];
    let count = 0;
    offsets.forEach(([dr, dc]) => {
      const nr = row + dr;
      const nc = col + dc;
      if (nr >= 0 && nr < this.grid.length && nc >= 0 && nc < this.grid[0].length) {
        count += this.grid[nr][nc];
      }
    });
    return count;
  }
}
