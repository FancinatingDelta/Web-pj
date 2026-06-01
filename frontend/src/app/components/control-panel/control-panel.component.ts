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

  @Output() singleStep = new EventEmitter<void>();
  @Output() toggleRun = new EventEmitter<void>();
  @Output() reset = new EventEmitter<void>();
  @Output() undo = new EventEmitter<void>();
  @Output() speedChange = new EventEmitter<number>();
}
