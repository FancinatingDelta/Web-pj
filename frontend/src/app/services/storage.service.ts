import { Injectable } from '@angular/core';
import { RuleConfig } from '../models/simulation.model';

const DB_NAME = 'cellular_automata';
const DB_VERSION = 2;
const DRAFT_STORE = 'draft';
const RECORDS_STORE = 'records';

export interface DraftData {
  grid: number[][];
  ruleConfig: RuleConfig;
  generation: number;
  history: number[][][];
}

export interface SavedRecord extends DraftData {
  id: number;
  name: string;
  username: string;
  createdAt: number;
}

export interface RecordSummary {
  id: number;
  name: string;
  generation: number;
  createdAt: number;
  automataType: string;
}

@Injectable({ providedIn: 'root' })
export class StorageService {
  private db: IDBDatabase | null = null;
  private ready: Promise<boolean>;
  private currentUser: string | null = null;

  constructor() {
    this.ready = this.openDB();
  }

  setCurrentUser(username: string): void {
    this.currentUser = username;
  }

  clearCurrentUser(): void {
    this.currentUser = null;
  }

  // ---- Database ----

  private openDB(): Promise<boolean> {
    return new Promise((resolve) => {
      const request = indexedDB.open(DB_NAME, DB_VERSION);
      request.onupgradeneeded = (event) => {
        const db = (event.target as IDBOpenDBRequest).result;
        if (db.objectStoreNames.contains(DRAFT_STORE)) {
          db.deleteObjectStore(DRAFT_STORE);
        }
        if (db.objectStoreNames.contains(RECORDS_STORE)) {
          db.deleteObjectStore(RECORDS_STORE);
        }
        db.createObjectStore(DRAFT_STORE, { keyPath: 'id' });
        const recordsStore = db.createObjectStore(RECORDS_STORE, {
          keyPath: 'id',
          autoIncrement: true,
        });
        recordsStore.createIndex('byUsername', 'username', { unique: false });
      };
      request.onsuccess = (event) => {
        this.db = (event.target as IDBOpenDBRequest).result;
        resolve(true);
      };
      request.onerror = () => {
        console.warn('IndexedDB 不可用，持久化功能已禁用');
        resolve(false);
      };
    });
  }

  private get draftKey(): string | null {
    return this.currentUser;
  }

  // ---- Draft ----

  async saveDraft(data: DraftData): Promise<void> {
    const key = this.draftKey;
    if (!key || !(await this.ready) || !this.db) return;
    return new Promise((resolve) => {
      const tx = this.db!.transaction(DRAFT_STORE, 'readwrite');
      tx.objectStore(DRAFT_STORE).put({ id: key, ...data, updatedAt: Date.now() });
      tx.oncomplete = () => resolve();
      tx.onerror = () => resolve();
    });
  }

  async loadDraft(): Promise<DraftData | null> {
    const key = this.draftKey;
    if (!key || !(await this.ready) || !this.db) return null;
    return new Promise((resolve) => {
      const tx = this.db!.transaction(DRAFT_STORE, 'readonly');
      const req = tx.objectStore(DRAFT_STORE).get(key);
      req.onsuccess = () => {
        if (!req.result) { resolve(null); return; }
        resolve({
          grid: req.result.grid,
          ruleConfig: req.result.ruleConfig,
          generation: req.result.generation,
          history: req.result.history,
        });
      };
      req.onerror = () => resolve(null);
    });
  }

  async deleteDraft(): Promise<void> {
    const key = this.draftKey;
    if (!key || !(await this.ready) || !this.db) return;
    return new Promise((resolve) => {
      const tx = this.db!.transaction(DRAFT_STORE, 'readwrite');
      tx.objectStore(DRAFT_STORE).delete(key);
      tx.oncomplete = () => resolve();
      tx.onerror = () => resolve();
    });
  }

  // ---- Records ----

  async saveRecord(name: string, data: DraftData): Promise<number> {
    const username = this.currentUser;
    if (!username || !(await this.ready) || !this.db) return -1;
    return new Promise((resolve) => {
      const tx = this.db!.transaction(RECORDS_STORE, 'readwrite');
      const req = tx.objectStore(RECORDS_STORE).add({
        name,
        username,
        grid: data.grid,
        ruleConfig: data.ruleConfig,
        generation: data.generation,
        history: data.history,
        createdAt: Date.now(),
      });
      tx.oncomplete = () => resolve(req.result as number);
      tx.onerror = () => resolve(-1);
    });
  }

  async listRecords(): Promise<RecordSummary[]> {
    const username = this.currentUser;
    if (!username || !(await this.ready) || !this.db) return [];
    return new Promise((resolve) => {
      const tx = this.db!.transaction(RECORDS_STORE, 'readonly');
      const index = tx.objectStore(RECORDS_STORE).index('byUsername');
      const req = index.getAll(username);
      req.onsuccess = () => {
        resolve(
          (req.result as SavedRecord[]).map((r) => ({
            id: r.id,
            name: r.name,
            generation: r.generation,
            createdAt: r.createdAt,
            automataType: r.ruleConfig?.automataType ?? '',
          }))
        );
      };
      req.onerror = () => resolve([]);
    });
  }

  async loadRecord(id: number): Promise<DraftData | null> {
    if (!(await this.ready) || !this.db) return null;
    return new Promise((resolve) => {
      const tx = this.db!.transaction(RECORDS_STORE, 'readonly');
      const req = tx.objectStore(RECORDS_STORE).get(id);
      req.onsuccess = () => {
        const record = req.result as SavedRecord | undefined;
        if (!record || record.username !== this.currentUser) {
          resolve(null);
          return;
        }
        resolve({
          grid: record.grid,
          ruleConfig: record.ruleConfig,
          generation: record.generation,
          history: record.history,
        });
      };
      req.onerror = () => resolve(null);
    });
  }

  async deleteRecord(id: number): Promise<void> {
    if (!(await this.ready) || !this.db) return;
    return new Promise((resolve) => {
      const tx = this.db!.transaction(RECORDS_STORE, 'readwrite');
      const store = tx.objectStore(RECORDS_STORE);
      const getReq = store.get(id);
      getReq.onsuccess = () => {
        const record = getReq.result as SavedRecord | undefined;
        if (!record || record.username !== this.currentUser) {
          resolve();
          return;
        }
        store.delete(id);
        tx.oncomplete = () => resolve();
        tx.onerror = () => resolve();
      };
      getReq.onerror = () => resolve();
    });
  }
}
