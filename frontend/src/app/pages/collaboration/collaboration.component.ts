import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CollaborationChatComponent } from '../../components/collaboration-chat/collaboration-chat.component';
import { CollaborationLobbyComponent } from '../../components/collaboration-lobby/collaboration-lobby.component';
import { GridCanvasComponent } from '../../components/grid-canvas/grid-canvas.component';
import { GridConfigComponent } from '../../components/grid-config/grid-config.component';
import { RuleEditorComponent } from '../../components/rule-editor/rule-editor.component';
import { ChatMessage, RoomInfo, RoomState } from '../../models/collaboration.model';
import { RuleConfig } from '../../models/simulation.model';
import { AuthService } from '../../services/auth.service';
import { CollaborationService } from '../../services/collaboration.service';

@Component({
  selector: 'app-collaboration',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    GridCanvasComponent,
    GridConfigComponent,
    RuleEditorComponent,
    CollaborationLobbyComponent,
    CollaborationChatComponent,
  ],
  templateUrl: './collaboration.component.html',
  styleUrl: './collaboration.component.css',
})
export class CollaborationComponent implements OnInit, OnDestroy {
  connected = false;
  username: string | null = null;

  rooms: RoomInfo[] = [];
  roomState: RoomState | null = null;
  chatMessages: ChatMessage[] = [];
  isRunning = false;
  speedMs = 400;

  private subscriptions: Subscription[] = [];
  private runnerTimer: ReturnType<typeof setInterval> | null = null;

  constructor(
    private readonly collab: CollaborationService,
    private readonly authService: AuthService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.username = this.authService.getCurrentUser();
    if (!this.username) {
      this.router.navigate(['/login']);
      return;
    }

    this.collab.connect();

    this.subscriptions.push(
      this.collab.connected$.subscribe((c) => (this.connected = c)),
      this.collab.rooms$.subscribe((r) => (this.rooms = r)),
      this.collab.state$.subscribe((s) => (this.roomState = s)),
      this.collab.chat$.subscribe((msg) => {
        this.chatMessages = [...this.chatMessages, msg];
      }),
    );
  }

  ngOnDestroy(): void {
    this.stopAutoRun();
    this.subscriptions.forEach((s) => s.unsubscribe());
    this.collab.disconnect();
  }

  onCreateRoom(roomName: string): void {
    if (!this.username) return;
    const roomId = crypto.randomUUID().substring(0, 8);
    this.collab.createRoom(roomId, roomName, this.username);
  }

  onJoinRoom(roomId: string): void {
    if (!this.username) return;
    this.collab.joinRoom(roomId, this.username);
  }

  leaveCurrentRoom(): void {
    if (!this.roomState || !this.username) return;
    this.stopAutoRun();
    const roomId = this.roomState.roomId;
    this.roomState = null;
    this.chatMessages = [];
    this.collab.leaveRoom(roomId, this.username);
  }

  toggleCell(pos: { row: number; col: number }): void {
    if (!this.roomState || !this.username) return;
    this.collab.cellToggle(this.roomState.roomId, this.username, pos.row, pos.col);
  }

  runSingleStep(): void {
    if (!this.roomState || !this.username) return;
    this.collab.step(this.roomState.roomId, this.username);
  }

  toggleAutoRun(): void {
    if (this.isRunning) {
      this.stopAutoRun();
      return;
    }
    if (!this.roomState || !this.username) return;
    this.isRunning = true;
    const roomId = this.roomState.roomId;
    const username = this.username;
    this.runnerTimer = setInterval(() => {
      this.collab.step(roomId, username);
    }, this.speedMs);
  }

  onSpeedChange(speed: number): void {
    this.speedMs = speed;
    if (this.isRunning) {
      this.stopAutoRun();
      this.toggleAutoRun();
    }
  }

  onRuleChange(nextConfig: RuleConfig): void {
    if (!this.roomState || !this.username) return;
    this.collab.ruleChange(this.roomState.roomId, this.username, nextConfig);
  }

  onGridConfirm(dims: { rows: number; cols: number }): void {
    if (!this.roomState || !this.username) return;
    this.collab.gridResize(this.roomState.roomId, this.username, dims.rows, dims.cols);
  }

  randomizeGrid(): void {
    if (!this.roomState || !this.username) return;
    this.collab.randomize(this.roomState.roomId, this.username);
  }

  clearGrid(): void {
    if (!this.roomState || !this.username) return;
    this.collab.clear(this.roomState.roomId, this.username);
  }

  onSendChat(content: string): void {
    if (!this.roomState || !this.username) return;
    this.collab.sendChat(this.roomState.roomId, this.username, content);
  }

  get is2D(): boolean {
    return this.roomState?.ruleConfig?.automataType === 'LIFE_GAME_2D';
  }

  get displayProbability(): number {
    const p = this.roomState?.ruleConfig?.probability;
    return p ? Math.round(p * 100) : 100;
  }

  private stopAutoRun(): void {
    this.isRunning = false;
    if (this.runnerTimer) {
      clearInterval(this.runnerTimer);
      this.runnerTimer = null;
    }
  }
}
