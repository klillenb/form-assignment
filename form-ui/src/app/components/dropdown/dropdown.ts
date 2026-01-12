import { Component, forwardRef, Input } from '@angular/core';
import { MatSelectModule } from '@angular/material/select';
import { MatFormField } from '@angular/material/select';
import {
  ControlValueAccessor,
  FormControl,
  NG_VALUE_ACCESSOR,
  ReactiveFormsModule,
} from '@angular/forms';
import { FlatSector, Sector } from '../../models/sector';

@Component({
  selector: 'app-dropdown',
  imports: [MatSelectModule, MatFormField, ReactiveFormsModule],
  templateUrl: './dropdown.html',
  styleUrl: './dropdown.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => Dropdown),
      multi: true,
    },
  ],
})
export class Dropdown implements ControlValueAccessor {
  @Input({ required: true }) sectors: Sector[] = [];

  selectionControl = new FormControl<number[]>([]);

  private onChange: (value: number[]) => void = () => {};
  private onTouched: () => void = () => {};

  constructor() {
    this.selectionControl.valueChanges.subscribe((v) => {
      this.onChange(v ?? []);
      this.onTouched();
    });
  }

  writeValue(value: number[] | null): void {
    this.selectionControl.setValue(value ?? [], { emitEvent: false });
  }

  registerOnChange(fn: (value: number[]) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    isDisabled ? this.selectionControl.disable() : this.selectionControl.enable();
  }

  get flattenedSectors(): FlatSector[] {
    return this.flatten(this.sectors);
  }

  private flatten(sectors: Sector[], level = 0): FlatSector[] {
    return sectors.flatMap((sector) => [
      { ...sector, level },
      ...(sector.children ? this.flatten(sector.children, level + 1) : []),
    ]);
  }
}
