# FormUi

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 21.0.3.

## Development server

### Prerequisites
- Node.js >= 20.19.0
- npm >= 9
- Angular CLI 21: `npm install -g @angular/cli`

## Setup
```bash
git clone https://github.com/klillenb/form-assignment.git
cd ./form-assignment/form-ui
npm install
```

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```
No data will be visible nor saved when running UI separately without API application.

## What could've been done better
- Currently tailwind styling is inlined, would most likely be better to extract them into css files for better maintainability.
- Better API error handling.
- Better UI/UX, there is definitely room for improvement regarding spacing.