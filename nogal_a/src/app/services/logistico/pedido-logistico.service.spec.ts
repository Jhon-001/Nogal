import { TestBed } from '@angular/core/testing';

import { PedidoLogisticoService } from './pedido-logistico.service';

describe('PedidoLogisticoService', () => {
  let service: PedidoLogisticoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PedidoLogisticoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
