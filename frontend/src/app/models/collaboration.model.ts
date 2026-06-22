import { RuleConfig } from './simulation.model';

export interface RoomState {
  roomId: string;
  roomName: string;
  users: string[];
  grid: number[][];
  ruleConfig: RuleConfig;
  generation: number;
  changedCount: number;
  createdAt: number;
}

export interface RoomInfo {
  roomId: string;
  roomName: string;
  userCount: number;
  createdAt: number;
}

export interface ChatMessage {
  username: string;
  content: string;
  timestamp: number;
}

export interface RoomCreatedEvent {
  roomId: string;
  username: string;
}
