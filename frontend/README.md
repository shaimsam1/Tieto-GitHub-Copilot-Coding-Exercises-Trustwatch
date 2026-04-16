# TrustWatch AML Dashboard - Frontend

Angular 17+ frontend for the Anti-Money Laundering Pattern Detection Dashboard.

## Prerequisites

- Node.js 18+
- npm 9+

## Installation

```bash
cd frontend
npm install
```

## Development

Start the development server:

```bash
npm start
```

The application will be available at `http://localhost:4200`.

To use the proxy for local backend:

```bash
ng serve --proxy-config proxy.conf.json
```

## Build

Production build:

```bash
npm run build:prod
```

## Testing

Run unit tests:

```bash
npm test
```

Run tests with coverage (CI mode):

```bash
npm run test:ci
```

## Project Structure

```
src/
├── app/
│   ├── core/                    # Singleton services
│   │   └── services/
│   │       ├── api.service.ts
│   │       ├── account.service.ts
│   │       ├── transaction.service.ts
│   │       └── aml-flag.service.ts
│   ├── shared/                  # Reusable components
│   │   └── components/
│   │       ├── risk-indicator/
│   │       ├── severity-badge/
│   │       ├── status-badge/
│   │       ├── account-type-badge/
│   │       └── loading-spinner/
│   ├── features/                # Feature modules
│   │   ├── aml-dashboard/
│   │   ├── pattern-viewer/
│   │   └── account-network/
│   ├── models/                  # TypeScript interfaces
│   ├── app.component.ts
│   ├── app.config.ts
│   └── app.routes.ts
├── environments/
├── styles.scss
├── main.ts
└── index.html
```

## Features

### AML Dashboard
- Overview statistics (accounts, flags, transactions)
- Recent AML flags with severity indicators
- High-risk transaction list
- Quick navigation to other views

### Pattern Viewer
- Filter by severity and typology
- Search patterns
- Confidence score visualization
- Recommended action display

### Account Network
- Account list with risk ratings
- Transaction edge visualization (table-based)
- Flagged connection highlighting
- Account detail panel with AML flags

## Architectural Decisions

- **Standalone Components**: All components are standalone (Angular 17+ default)
- **Signal-based Inputs**: Using `input()` and `output()` for component communication
- **OnPush Change Detection**: Presentational components use OnPush for performance
- **No Graph Libraries**: Network visualization uses tables per project constraints
- **Typed Models**: All API responses have TypeScript interfaces
- **Environment-based Config**: API URL configured via environment files

## API Integration

The frontend expects the backend API at:
- Development: `http://localhost:8080`
- Production: Same origin

API response envelope:
```typescript
interface ApiResponse<T> {
  data: T;
  meta: { timestamp: string; requestId: string };
  errors: ApiError[];
}
```
