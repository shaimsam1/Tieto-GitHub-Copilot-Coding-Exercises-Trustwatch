import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="app-layout">
      <nav class="app-nav">
        <div class="nav-brand">
          <span class="brand-icon">🛡️</span>
          <span class="brand-text">TrustWatch</span>
        </div>
        
        <ul class="nav-links">
          <li>
            <a routerLink="/dashboard" routerLinkActive="active">
              <span class="nav-icon">📊</span>
              <span class="nav-label">Dashboard</span>
            </a>
          </li>
          <li>
            <a routerLink="/patterns" routerLinkActive="active">
              <span class="nav-icon">🔍</span>
              <span class="nav-label">Patterns</span>
            </a>
          </li>
          <li>
            <a routerLink="/network" routerLinkActive="active">
              <span class="nav-icon">🔗</span>
              <span class="nav-label">Network</span>
            </a>
          </li>
        </ul>

        <div class="nav-footer">
          <span class="version">v1.0.0</span>
        </div>
      </nav>

      <main class="app-main">
        <router-outlet />
      </main>
    </div>
  `,
  styles: [`
    .app-layout {
      display: flex;
      min-height: 100vh;
    }

    .app-nav {
      width: 220px;
      background: #1e293b;
      color: white;
      display: flex;
      flex-direction: column;
      position: fixed;
      top: 0;
      left: 0;
      bottom: 0;
      z-index: 100;
    }

    .nav-brand {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 1.25rem 1rem;
      border-bottom: 1px solid #334155;

      .brand-icon {
        font-size: 1.5rem;
      }

      .brand-text {
        font-size: 1.125rem;
        font-weight: 700;
        letter-spacing: -0.025em;
      }
    }

    .nav-links {
      list-style: none;
      padding: 1rem 0.5rem;
      margin: 0;
      flex: 1;

      li {
        margin-bottom: 0.25rem;
      }

      a {
        display: flex;
        align-items: center;
        gap: 0.75rem;
        padding: 0.75rem 1rem;
        color: #94a3b8;
        text-decoration: none;
        border-radius: 8px;
        transition: all 0.15s;

        &:hover {
          background: #334155;
          color: white;
        }

        &.active {
          background: #3b82f6;
          color: white;
        }

        .nav-icon {
          font-size: 1.125rem;
        }

        .nav-label {
          font-size: 0.875rem;
          font-weight: 500;
        }
      }
    }

    .nav-footer {
      padding: 1rem;
      border-top: 1px solid #334155;
      text-align: center;

      .version {
        font-size: 0.75rem;
        color: #64748b;
      }
    }

    .app-main {
      flex: 1;
      margin-left: 220px;
      background: #f8fafc;
      min-height: 100vh;
    }

    @media (max-width: 768px) {
      .app-nav {
        width: 100%;
        height: auto;
        position: relative;
        flex-direction: row;
        align-items: center;
        padding: 0.5rem;
      }

      .nav-brand {
        border-bottom: none;
        padding: 0.5rem;
      }

      .nav-links {
        display: flex;
        padding: 0;
        flex: 1;
        justify-content: center;
        gap: 0.5rem;

        li {
          margin: 0;
        }

        a {
          padding: 0.5rem 0.75rem;

          .nav-label {
            display: none;
          }
        }
      }

      .nav-footer {
        display: none;
      }

      .app-main {
        margin-left: 0;
      }

      .app-layout {
        flex-direction: column;
      }
    }
  `]
})
export class AppComponent {
  title = 'TrustWatch AML Dashboard';
}
