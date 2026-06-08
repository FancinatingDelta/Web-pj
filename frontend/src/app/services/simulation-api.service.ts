import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  EvaluationResult,
  EvaluationScenario,
  PresetModel,
  RuleConfig,
  SimulationStepResponse,
} from '../models/simulation.model';

@Injectable({
  providedIn: 'root',
})
export class SimulationApiService {
  private readonly baseUrl = '/api';

  constructor(private readonly http: HttpClient) {}

  step(grid: number[][], ruleConfig: RuleConfig): Observable<SimulationStepResponse> {
    return this.http.post<SimulationStepResponse>(`${this.baseUrl}/simulation/step`, {
      grid,
      ruleConfig,
    });
  }

  getPresets(): Observable<PresetModel[]> {
    return this.http.get<PresetModel[]>(`${this.baseUrl}/presets`);
  }

  getEvaluationScenarios(): Observable<EvaluationScenario[]> {
    return this.http.get<EvaluationScenario[]>(`${this.baseUrl}/evaluation/scenarios`);
  }

  checkScenario(scenarioId: string, answerGrid: number[][]): Observable<EvaluationResult> {
    return this.http.post<EvaluationResult>(`${this.baseUrl}/evaluation/check`, {
      scenarioId,
      answerGrid,
    });
  }
}
