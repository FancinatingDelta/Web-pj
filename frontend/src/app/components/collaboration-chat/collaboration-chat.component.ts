import { CommonModule } from '@angular/common';
import {
  AfterViewChecked,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChatMessage } from '../../models/collaboration.model';

@Component({
  selector: 'app-collaboration-chat',
  imports: [CommonModule, FormsModule],
  templateUrl: './collaboration-chat.component.html',
  styleUrl: './collaboration-chat.component.css',
})
export class CollaborationChatComponent implements AfterViewChecked {
  @Input() messages: ChatMessage[] = [];
  @Input() users: string[] = [];
  @Input() username = '';

  @Output() sendMessage = new EventEmitter<string>();

  @ViewChild('chatScroll') private chatScroll: ElementRef | null = null;

  newMessage = '';

  onSend(): void {
    const content = this.newMessage.trim();
    if (!content) {
      return;
    }
    this.sendMessage.emit(content);
    this.newMessage = '';
  }

  ngAfterViewChecked(): void {
    if (this.chatScroll) {
      this.chatScroll.nativeElement.scrollTop = this.chatScroll.nativeElement.scrollHeight;
    }
  }
}
