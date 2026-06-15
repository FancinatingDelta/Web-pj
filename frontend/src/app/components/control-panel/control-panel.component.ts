import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-control-panel',
  imports: [FormsModule],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})
export class ControlPanelComponent {
  @Input() generation = 0;
  @Input() isRunning = false;
  @Input() speedMs = 400;
  @Input() historyCount = 0;
  @Input() currentGeneration = 0;
  @Input() editMode = true;

  @Output() editModeChange = new EventEmitter<boolean>();
  @Output() singleStep = new EventEmitter<void>();
  @Output() toggleRun = new EventEmitter<void>();
  @Output() reset = new EventEmitter<void>();
  @Output() undo = new EventEmitter<void>();
  @Output() speedChange = new EventEmitter<number>();
  @Output() jumpTo = new EventEmitter<number>();
  @Output() randomize = new EventEmitter<void>();
  @Output() clear = new EventEmitter<void>();
  @Output() exportGrid = new EventEmitter<void>();
  @Output() fileSelected = new EventEmitter<File>();

  get historyIndices(): number[] {
    return Array.from({ length: this.historyCount }, (_, i) => i);
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.fileSelected.emit(input.files[0]);
      input.value = '';
    }
  }
}
