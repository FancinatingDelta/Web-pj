import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MultiStateRuleConfig, MultiStateStepResponse } from '../models/multi-state.model';

@Injectable({ providedIn: 'root' })
export class MultiStateApiService {
  private baseUrl = '/api/multi-state';

  constructor(private http: HttpClient) {}

  step(grid: number[][], config: MultiStateRuleConfig): Observable<MultiStateStepResponse> {
    return this.http.post<MultiStateStepResponse>(`${this.baseUrl}/step`, { grid, config });
  }
}
