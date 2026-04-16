import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/aml-dashboard/aml-dashboard.component')
      .then(m => m.AmlDashboardComponent),
    title: 'AML Dashboard | TrustWatch'
  },
  {
    path: 'patterns',
    loadComponent: () => import('./features/pattern-viewer/pattern-viewer.component')
      .then(m => m.PatternViewerComponent),
    title: 'Pattern Viewer | TrustWatch'
  },
  {
    path: 'network',
    loadComponent: () => import('./features/account-network/account-network.component')
      .then(m => m.AccountNetworkComponent),
    title: 'Account Network | TrustWatch'
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
