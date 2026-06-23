import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { SimulatorComponent } from './pages/simulator/simulator.component';
import { CollaborationComponent } from './pages/collaboration/collaboration.component';
import { authGuard } from './services/auth.guard';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'simulator',
    component: SimulatorComponent,
    canActivate: [authGuard],
  },
  {
    path: 'collaboration',
    component: CollaborationComponent,
    canActivate: [authGuard],
  },
  {
    path: 'multi-state',
    loadComponent: () =>
      import('./pages/multi-state-simulator/multi-state-simulator.component').then(
        (m) => m.MultiStateSimulatorComponent,
      ),
    canActivate: [authGuard],
  },
];
