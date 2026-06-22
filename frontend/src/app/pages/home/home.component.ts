import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  imports: [RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, OnDestroy {

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  get username(): string | null {
    return this.authService.getCurrentUser();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
  /* ---- glider animation ---- */
  private readonly rows = 20;
  private readonly cols = 30;
  grid: number[][] = [];
  generation = 0;
  private timer: ReturnType<typeof setInterval> | null = null;

  ngOnInit(): void {
    this.initGrid();
    this.runAnimation();
  }

  ngOnDestroy(): void {
    if (this.timer) clearInterval(this.timer);
  }

  private initGrid(): void {
    this.grid = Array.from({ length: this.rows }, () => Array.from({ length: this.cols }, () => 0));
    // classic Conway glider
    const midR = Math.floor(this.rows / 2) - 8;
    const midC = Math.floor(this.cols / 2) - 3;
    this.grid[midR][midC + 1] = 1;
    this.grid[midR + 1][midC + 2] = 1;
    this.grid[midR + 2][midC] = 1;
    this.grid[midR + 2][midC + 1] = 1;
    this.grid[midR + 2][midC + 2] = 1;
  }

  private runAnimation(): void {
    this.timer = setInterval(() => {
      this.grid = this.computeNext(this.grid);
      this.generation++;
    }, 180);
  }

  private computeNext(grid: number[][]): number[][] {
    const rows = grid.length;
    const cols = grid[0].length;
    const next = grid.map(row => [...row]);
    for (let r = 0; r < rows; r++) {
      for (let c = 0; c < cols; c++) {
        let neighbors = 0;
        for (let dr = -1; dr <= 1; dr++) {
          for (let dc = -1; dc <= 1; dc++) {
            if (dr === 0 && dc === 0) continue;
            const nr = r + dr, nc = c + dc;
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
              neighbors += grid[nr][nc];
            }
          }
        }
        if (grid[r][c] === 1) {
          next[r][c] = (neighbors === 2 || neighbors === 3) ? 1 : 0;
        } else {
          next[r][c] = (neighbors === 3) ? 1 : 0;
        }
      }
    }
    return next;
  }
}
