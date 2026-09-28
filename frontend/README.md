# Teamplanner frontend

Web app for Teamplanner, built with [Angular](https://angular.dev/) and [Angular Material](https://material.angular.dev/).

## Running locally

Start the backend first (see `../backend/README.md`), then:

```shell
npm install
npm start
```

The app runs at http://localhost:4200/. Requests to `/api` are proxied to the backend at http://localhost:8080 (see `proxy.conf.json`).

## Other commands

| Command | Description |
| --- | --- |
| `npm test` | Run the unit tests with Vitest |
| `npm run lint` | Run ESLint |
| `npm run build` | Production build into `dist/frontend` |

In production the app expects the API at `/api/v1.0` on the same host, for example behind a reverse proxy that forwards `/api` to the backend.
