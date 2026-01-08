import { Component, signal } from '@angular/core';
import { Form } from "./components/form/form";

@Component({
  selector: 'app-root',
  imports: [Form],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('form-ui');
}
