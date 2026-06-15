import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = '/api/auth';
  private readonly storageKey = 'cellular_automata_user';

  constructor(private readonly http: HttpClient) {}

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, req);
  }

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, req);
  }

  getCurrentUser(): string | null {
    return localStorage.getItem(this.storageKey);
  }

  setCurrentUser(username: string): void {
    localStorage.setItem(this.storageKey, username);
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
  }
}
