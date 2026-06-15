import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SimulatorComponent } from './pages/simulator/simulator.component';
import { authGuard } from './services/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/simulator',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'simulator',
    component: SimulatorComponent,
    canActivate: [authGuard]
  }
];
