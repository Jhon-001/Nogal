import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IngresoInventarioComponent } from './ingreso-inventario.component';

describe('IngresoInventarioComponent', () => {
  let component: IngresoInventarioComponent;
  let fixture: ComponentFixture<IngresoInventarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngresoInventarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IngresoInventarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
