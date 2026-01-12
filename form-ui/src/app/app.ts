import { Component, signal } from '@angular/core';
import { InputForm } from "./components/form/input-form";

@Component({
  selector: 'app-root',
  imports: [InputForm],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('form-ui');
}
