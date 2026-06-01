import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NeighborhoodType } from '../../models/simulation.model';

@Component({
  selector: 'app-grid-canvas',
  imports: [CommonModule],
  templateUrl: './grid-canvas.component.html',
  styleUrl: './grid-canvas.component.css'
})
export class GridCanvasComponent {
  @Input() grid: number[][] = [];
  @Input() clickable = true;
  @Input() selectedCell: { row: number; col: number } | null = null;
  @Input() neighborhoodType: NeighborhoodType = 'MOORE';

  @Output() cellToggle = new EventEmitter<{ row: number; col: number }>();
  @Output() cellSelected = new EventEmitter<{ row: number; col: number }>();

  onCellClick(row: number, col: number): void {
    this.cellSelected.emit({ row, col });
    if (this.clickable) {
      this.cellToggle.emit({ row, col });
    }
  }

  isNeighborCell(row: number, col: number): boolean {
    if (!this.selectedCell) {
      return false;
    }
    const dr = Math.abs(this.selectedCell.row - row);
    const dc = Math.abs(this.selectedCell.col - col);
    if (dr === 0 && dc === 0) {
      return false;
    }

    if (this.neighborhoodType === 'VON_NEUMANN') {
      return dr + dc === 1;
    }
    return dr <= 1 && dc <= 1;
  }
}
