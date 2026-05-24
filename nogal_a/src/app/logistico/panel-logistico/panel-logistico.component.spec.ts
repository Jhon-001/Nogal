import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PanelLogisticoComponent } from './panel-logistico.component';

describe('PanelLogisticoComponent', () => {
  let component: PanelLogisticoComponent;
  let fixture: ComponentFixture<PanelLogisticoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PanelLogisticoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PanelLogisticoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
