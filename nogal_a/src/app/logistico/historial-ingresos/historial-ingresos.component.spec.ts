import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistorialIngresosComponent } from './historial-ingresos.component';

describe('HistorialIngresosComponent', () => {
  let component: HistorialIngresosComponent;
  let fixture: ComponentFixture<HistorialIngresosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistorialIngresosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistorialIngresosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
