import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interface para archivos seleccionados - AHORA EXPORTADA
export interface ArchivoSeleccionado {
  file: File;
  urlPrevia?: string;
}

// Interface para vista previa de imágenes - AHORA EXPORTADA
export interface ImagenPrevia {
  url: string;
  nombre: string;
  tipo: string;
}

export interface Pedido {
  id: number;
  numeroPedido: string;
  fechaPedido: string;
  estado: string;
  direccion: any;
  usuario: any;
  detalles: any[];
  total: number;
  subtotal: number;
  igv: number;
  envio: number;
  // Propiedades adicionales para el componente
  // Propiedades para UI
  mostrarMenu?: boolean;
  mostrarDetalles?: boolean;
  mostrarFormularioEntrega?: boolean;
  codigoVerificacion?: string;
  observaciones?: string;
  archivosSeleccionados?: ArchivoSeleccionado[];
  imagenesPrevia?: ImagenPrevia[];
}

export interface ReporteEntrega {
  id?: number;
  pedido: {
    id: number;
    numeroPedido?: string;
    usuario?: any;
    direccion?: any;
    total?: number;
  };
  repartidor: {
    id: number;
    nombres?: string;
    apellidos?: string;
  };
  codigoVerificacion: string;
  fotoUrl?: string;
  observaciones?: string;
  fechaEntrega?: string;
  estado: string;
  archivosAdjuntos?: string | null;
}

export interface ProblemaEntrega {
  pedidoId: number;
  repartidorId: number;
  descripcion: string;
  fechaReporte: string;
  tipoProblema?: string;
}

@Injectable({
  providedIn: 'root'
})
export class RepartidorService {
  private apiUrl = 'https://nogal-backend-czaqc4gfffg0asfs.chilecentral-01.azurewebsites.net/repartidor';

  constructor(private http: HttpClient) { }

  // Obtener pedidos para reparto
  getPedidosParaReparto(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/pedidos`);
  }

  // Obtener pedidos asignados al repartidor
  getPedidosAsignados(repartidorId: number): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/pedidos/${repartidorId}`);
  }

  // Crear reporte de entrega
  crearReporteEntrega(reporte: ReporteEntrega): Observable<ReporteEntrega> {
    return this.http.post<ReporteEntrega>(`${this.apiUrl}/reporte`, reporte);
  }

  // Obtener reportes del repartidor
  getReportesRepartidor(repartidorId: number): Observable<ReporteEntrega[]> {
    console.log('📋 Obteniendo reportes para repartidor:', repartidorId);
    return this.http.get<ReporteEntrega[]>(`${this.apiUrl}/reportes/${repartidorId}`);
  }

  // Obtener reporte específico
  getReporte(id: number): Observable<ReporteEntrega> {
    return this.http.get<ReporteEntrega>(`${this.apiUrl}/reporte/${id}`);
  }

  // NUEVO: Subir archivo individual
  subirArchivo(archivo: File): Observable<{url: string}> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    
    return this.http.post<{url: string}>(`${this.apiUrl}/subir-archivo`, formData);
  }

  // NUEVO: Subir múltiples archivos
  subirArchivos(archivos: File[]): Observable<{urls: string[]}> {
    const formData = new FormData();
    archivos.forEach(archivo => {
      formData.append('archivos', archivo);
    });
    
    return this.http.post<{urls: string[]}>(`${this.apiUrl}/subir-archivos`, formData);
  }

  // Reportar problema en la entrega
  reportarProblema(pedidoId: number, descripcion: string, repartidorId: number): Observable<any> {
    const problema: ProblemaEntrega = {
      pedidoId: pedidoId,
      repartidorId: repartidorId,
      descripcion: descripcion,
      fechaReporte: new Date().toISOString(),
      tipoProblema: 'ENTREGA'
    };
    
    return this.http.post(`${this.apiUrl}/problema`, problema);
  }

  // Método adicional: Actualizar estado del pedido
  actualizarEstadoPedido(pedidoId: number, estado: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/pedidos/${pedidoId}/estado`, { estado });
  }

  // Método adicional: Obtener historial de entregas del repartidor
  getHistorialEntregas(repartidorId: number): Observable<ReporteEntrega[]> {
    return this.http.get<ReporteEntrega[]>(`${this.apiUrl}/historial/${repartidorId}`);
  }

  // Método adicional: Subir foto de entrega
  subirFotoEntrega(reporteId: number, foto: File): Observable<any> {
    const formData = new FormData();
    formData.append('foto', foto);
    formData.append('reporteId', reporteId.toString());
    
    return this.http.post(`${this.apiUrl}/subir-foto`, formData);
  }
}