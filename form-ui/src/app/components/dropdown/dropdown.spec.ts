import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Dropdown } from './dropdown';
import { Sector } from '../../models/sector';
import { NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { MatFormField, MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';

describe('Dropdown', () => {
  let component: Dropdown;
  let fixture: ComponentFixture<Dropdown>;

  const mockSectors: Sector[] = [
    { id: 1, name: 'A', children: [{ id: 2, name: 'A1' }] },
    { id: 3, name: 'B' },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, MatFormFieldModule, MatSelectModule],
      providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: Dropdown, multi: true }],
    }).compileComponents();

    fixture = TestBed.createComponent(Dropdown);
    component = fixture.componentInstance;
    component.sectors = mockSectors;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ControlValueAccessor', () => {
    it('should writeValue correctly', () => {
      component.writeValue([1, 3]);
      expect(component.selectionControl.value).toEqual([1, 3]);

      component.writeValue(null);
      expect(component.selectionControl.value).toEqual([]);
    });

    it('should register onChange and trigger on value change', () => {
      const spyChange = vi.fn();
      const spyTouched = vi.fn();

      component.registerOnChange(spyChange);
      component.registerOnTouched(spyTouched);

      component.selectionControl.setValue([2]);

      expect(spyChange).toHaveBeenCalledWith([2]);
      expect(spyTouched).toHaveBeenCalled();
    });

    it('should setDisabledState correctly', () => {
      component.setDisabledState(true);
      expect(component.selectionControl.disabled).toBe(true);

      component.setDisabledState(false);
      expect(component.selectionControl.enabled).toBe(true);
    });
  });

  describe('flattenedSectors', () => {
    it('should flatten sectors correctly', () => {
      const flattened = component.flattenedSectors;

      expect(flattened).toEqual([
        { id: 1, name: 'A', children: [{ id: 2, name: 'A1' }], level: 0 },
        { id: 2, name: 'A1', level: 1 },
        { id: 3, name: 'B', level: 0 },
      ]);
    });

    it('should handle empty sectors', () => {
      component.sectors = [];
      expect(component.flattenedSectors).toEqual([]);
    });
  });
});
