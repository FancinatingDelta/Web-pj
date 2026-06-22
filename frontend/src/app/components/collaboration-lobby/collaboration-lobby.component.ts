import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RoomInfo } from '../../models/collaboration.model';

@Component({
  selector: 'app-collaboration-lobby',
  imports: [CommonModule, FormsModule],
  templateUrl: './collaboration-lobby.component.html',
  styleUrl: './collaboration-lobby.component.css',
})
export class CollaborationLobbyComponent {
  @Input() rooms: RoomInfo[] = [];
  @Input() connected = false;

  @Output() createRequest = new EventEmitter<string>();
  @Output() joinRequest = new EventEmitter<string>();

  newRoomName = '';
  joinRoomId = '';

  onCreate(): void {
    const name = this.newRoomName.trim();
    if (!name) {
      return;
    }
    this.createRequest.emit(name);
    this.newRoomName = '';
  }

  onJoin(): void {
    const id = this.joinRoomId.trim();
    if (!id) {
      return;
    }
    this.joinRequest.emit(id);
    this.joinRoomId = '';
  }
}
