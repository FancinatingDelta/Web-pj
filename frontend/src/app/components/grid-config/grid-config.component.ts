import { Component, ElementRef, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-grid-config',
  imports: [FormsModule],
  templateUrl: './grid-config.component.html',
  styleUrl: './grid-config.component.css',
  host: { '(focusout)': 'onFocusOut()' }
})
export class GridConfigComponent implements OnChanges {
  @Input() rows = 20;
  @Input() cols = 20;
  @Input() is2D = true;
  @Output() confirmed = new EventEmitter<{ rows: number; cols: number }>();

  draftRows = this.rows;
  draftCols = this.cols;

  constructor(private readonly el: ElementRef) {}

  ngOnChanges(): void {
    this.draftRows = this.rows;
    this.draftCols = this.cols;
  }

  onConfirm(): void {
    this.draftRows = Math.max(1, Math.min(100, this.draftRows));
    this.draftCols = Math.max(1, Math.min(200, this.draftCols));
    this.confirmed.emit({ rows: this.draftRows, cols: this.draftCols });
  }

  onFocusOut(): void {
    setTimeout(() => {
      if (!this.el.nativeElement.contains(document.activeElement)) {
        this.draftRows = this.rows;
        this.draftCols = this.cols;
      }
    });
  }
}
