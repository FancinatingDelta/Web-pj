import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  mode: 'login' | 'register' = 'login';
  username = '';
  password = '';
  confirmPassword = '';
  errorMessage = '';
  loading = false;

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
  ) {}

  switchMode(mode: 'login' | 'register'): void {
    this.mode = mode;
    this.errorMessage = '';
  }

  submit(): void {
    this.errorMessage = '';
    if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = '请填写用户名和密码';
      return;
    }
    if (this.mode === 'register' && this.password !== this.confirmPassword) {
      this.errorMessage = '两次密码不一致';
      return;
    }

    this.loading = true;
    const request = { username: this.username.trim(), password: this.password };

    const action =
      this.mode === 'login' ? this.authService.login(request) : this.authService.register(request);

    action.subscribe({
      next: (res) => {
        this.loading = false;
        if (res.success) {
          this.authService.setCurrentUser(res.username!);
          this.router.navigate(['/']);
        } else {
          this.errorMessage = res.message;
        }
      },
      error: () => {
        this.loading = false;
        this.errorMessage = '网络错误，请确认后端已启动';
      },
    });
  }
}
