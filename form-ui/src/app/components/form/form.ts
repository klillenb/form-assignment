import { Component, inject } from '@angular/core';
import { Api } from '../../services/api';
import { Observable } from 'rxjs';
import { Sector } from '../../models/sector';

@Component({
  selector: 'app-form',
  imports: [],
  templateUrl: './form.html',
  styleUrl: './form.css',
})
export class Form {
  private readonly api: Api = inject(Api);

  sectors$!: Observable<Sector[]>;

  constructor() {
    this.sectors$ = this.api.getSectors();
    this.sectors$.subscribe(v => console.log(v))
  }
}
