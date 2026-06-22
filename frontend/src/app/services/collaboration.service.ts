import { Injectable } from '@angular/core';
import { Client, IFrame, Message, StompSubscription } from '@stomp/stompjs';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { ChatMessage, RoomInfo, RoomState } from '../models/collaboration.model';
import { RuleConfig } from '../models/simulation.model';

@Injectable({ providedIn: 'root' })
export class CollaborationService {
  private client: Client | null = null;
  private stateSub: StompSubscription | null = null;
  private chatSub: StompSubscription | null = null;
  private roomsSub: StompSubscription | null = null;

  private readonly stateSubject = new BehaviorSubject<RoomState | null>(null);
  readonly state$: Observable<RoomState | null> = this.stateSubject.asObservable();

  private readonly chatSubject = new Subject<ChatMessage>();
  readonly chat$: Observable<ChatMessage> = this.chatSubject.asObservable();

  private readonly roomsSubject = new BehaviorSubject<RoomInfo[]>([]);
  readonly rooms$: Observable<RoomInfo[]> = this.roomsSubject.asObservable();

  private readonly connectedSubject = new BehaviorSubject<boolean>(false);
  readonly connected$: Observable<boolean> = this.connectedSubject.asObservable();

  private currentRoomId: string | null = null;

  connect(): void {
    if (this.client?.connected) {
      return;
    }

    const wsUrl = this.buildWebSocketUrl();
    this.client = new Client({
      brokerURL: wsUrl,
      reconnectDelay: 3000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
    });

    this.client.onConnect = (_frame: IFrame) => {
      this.connectedSubject.next(true);

      this.roomsSub = this.client!.subscribe('/topic/rooms', (msg: Message) => {
        const rooms: RoomInfo[] = JSON.parse(msg.body);
        this.roomsSubject.next(rooms);
      });
    };

    this.client.onDisconnect = () => {
      this.connectedSubject.next(false);
      this.stateSubject.next(null);
      this.currentRoomId = null;
    };

    this.client.onWebSocketError = (_error: Event) => {
      // WebSocket connection error — reconnection is handled by stompjs
    };

    this.client.activate();
  }

  disconnect(): void {
    this.stateSub?.unsubscribe();
    this.chatSub?.unsubscribe();
    this.roomsSub?.unsubscribe();
    this.stateSub = null;
    this.chatSub = null;
    this.roomsSub = null;
    this.currentRoomId = null;
    this.stateSubject.next(null);
    this.client?.deactivate();
  }

  createRoom(roomId: string, roomName: string, username: string): void {
    this.subscribeToRoom(roomId);
    this.client?.publish({
      destination: '/app/room/create',
      body: JSON.stringify({ roomId, roomName, username }),
    });
  }

  joinRoom(roomId: string, username: string): void {
    this.subscribeToRoom(roomId);
    this.client?.publish({
      destination: '/app/room/join',
      body: JSON.stringify({ roomId, username }),
    });
  }

  leaveRoom(roomId: string, username: string): void {
    this.unsubscribeFromRoom();
    this.client?.publish({
      destination: '/app/room/leave',
      body: JSON.stringify({ roomId, username }),
    });
  }

  subscribeToRoom(roomId: string): void {
    this.currentRoomId = roomId;

    this.stateSub?.unsubscribe();
    this.chatSub?.unsubscribe();

    this.stateSub = this.client!.subscribe(`/topic/room/${roomId}/state`, (msg: Message) => {
      const state: RoomState = JSON.parse(msg.body);
      this.stateSubject.next(state);
    });

    this.chatSub = this.client!.subscribe(`/topic/room/${roomId}/chat`, (msg: Message) => {
      const chatMsg: ChatMessage = JSON.parse(msg.body);
      this.chatSubject.next(chatMsg);
    });
  }

  unsubscribeFromRoom(): void {
    this.stateSub?.unsubscribe();
    this.chatSub?.unsubscribe();
    this.stateSub = null;
    this.chatSub = null;
    this.currentRoomId = null;
    this.stateSubject.next(null);
  }

  cellToggle(roomId: string, username: string, row: number, col: number): void {
    this.client?.publish({
      destination: '/app/room/cell-toggle',
      body: JSON.stringify({ roomId, username, row, col }),
    });
  }

  step(roomId: string, username: string): void {
    this.client?.publish({
      destination: '/app/room/step',
      body: JSON.stringify({ roomId, username }),
    });
  }

  ruleChange(roomId: string, username: string, ruleConfig: RuleConfig): void {
    this.client?.publish({
      destination: '/app/room/rule-change',
      body: JSON.stringify({ roomId, username, ruleConfig }),
    });
  }

  gridResize(roomId: string, username: string, rows: number, cols: number): void {
    this.client?.publish({
      destination: '/app/room/grid-resize',
      body: JSON.stringify({ roomId, username, rows, cols }),
    });
  }

  randomize(roomId: string, username: string): void {
    this.client?.publish({
      destination: '/app/room/randomize',
      body: JSON.stringify({ roomId, username }),
    });
  }

  clear(roomId: string, username: string): void {
    this.client?.publish({
      destination: '/app/room/clear',
      body: JSON.stringify({ roomId, username }),
    });
  }

  sendChat(roomId: string, username: string, content: string): void {
    this.client?.publish({
      destination: '/app/room/chat',
      body: JSON.stringify({ roomId, username, content }),
    });
  }

  getCurrentRoomId(): string | null {
    return this.currentRoomId;
  }

  private buildWebSocketUrl(): string {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.host;
    return `${protocol}//${host}/ws`;
  }
}
