import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RepartidorService, Pedido, ReporteEntrega, ArchivoSeleccionado, ImagenPrevia } from '../../services/repartidor.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-panel-repartidor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './panel-repartidor.component.html',
  styleUrls: ['./panel-repartidor.component.css']
})
export class PanelRepartidorComponent implements OnInit {
  pedidos: Pedido[] = [];
  reportes: ReporteEntrega[] = []; // ✅ ESTA ES LA LISTA DE ENTREGAS REALIZADAS
  usuarioActual: any;
  loading: boolean = true;
  tabActiva: string = 'pendientes';

  constructor(
    private repartidorService: RepartidorService,
    private authService: AuthService,
    private router: Router
  ) {}

ngOnInit() {
  console.log('🔄 Inicializando Panel Repartidor...');
  
  // Obtener usuario actual
  this.usuarioActual = this.authService.getUsuarioActual();
  console.log('👤 Usuario actual desde AuthService:', this.usuarioActual);
  
  // Si no hay usuario, redirigir al login
  if (!this.usuarioActual) {
    console.error('❌ No hay usuario autenticado');
    this.router.navigate(['/login']);
    return;
  }
  
  // Verificar que el usuario sea repartidor
  if (this.usuarioActual.rol !== 'repartidor') {
    console.error('❌ Usuario no es repartidor:', this.usuarioActual.rol);
    alert('❌ No tienes permisos de repartidor');
    this.router.navigate(['/']);
    return;
  }
  
  console.log('✅ Usuario validado:', this.usuarioActual.nombres, this.usuarioActual.apellidos);
  this.cargarDatos();
}
cargarDatos() {
  this.loading = true;
  console.log('🔄 Cargando datos para repartidor ID:', this.usuarioActual.id);
  
  this.repartidorService.getPedidosParaReparto().subscribe({
    next: (pedidos) => {
      console.log('📦 Todos los pedidos recibidos:', pedidos.length);
      
      // ✅ FILTRADO COMPLETO: Solo pedidos ENVIADOS que NO tengan reporte
      const pedidosEnviados = pedidos.filter(pedido => {
        const esEnviado = pedido.estado === 'ENVIADO';
        
        // Verificar si ya tiene reporte (consultando el backend)
        // O puedes mantener una lista local de pedidos con reporte
        const tieneReporte = this.reportes.some(reporte => 
          reporte.pedido?.id === pedido.id
        );
        
        if (!esEnviado || tieneReporte) {
          console.log(`   ❌ Filtrado pedido #${pedido.numeroPedido} - Estado: ${pedido.estado}, Tiene reporte: ${tieneReporte}`);
          return false;
        }
        
        return true;
      });
      
      console.log('✅ Pedidos ENVIADOS sin reporte:', pedidosEnviados.length);
      
      this.pedidos = pedidosEnviados.map(pedido => ({
        ...pedido,
        mostrarMenu: false,
        mostrarDetalles: false,
        mostrarFormularioEntrega: false,
        codigoVerificacion: '',
        observaciones: '',
        archivosSeleccionados: [],
        imagenesPrevia: []
      }));
      
      this.loading = false;
    },
    error: (error) => {
      console.error('❌ Error cargando pedidos:', error);
      this.loading = false;
    }
  });

  this.cargarEntregasRealizadas();
}

  cambiarTab(tab: string) {
  this.tabActiva = tab;
  console.log(`📁 Cambiando a pestaña: ${tab}`);
  }

  esTabActiva(tab: string): boolean {
  return this.tabActiva === tab;
  }

  // ✅ MÉTODO PARA CARGAR ENTREGAS REALIZADAS
  cargarEntregasRealizadas() {
    console.log('📋 Cargando entregas realizadas...');
    
    this.repartidorService.getReportesRepartidor(this.usuarioActual.id).subscribe({
      next: (reportes) => {
        console.log('✅ Entregas realizadas cargadas:', reportes.length);
        this.reportes = reportes;
        
        // Debug: mostrar info de cada reporte
        reportes.forEach((reporte, index) => {
          console.log(`   ${index + 1}. Reporte #${reporte.id} - Pedido: ${reporte.pedido?.numeroPedido}`);
        });
      },
      error: (error) => {
        console.error('❌ Error cargando entregas realizadas:', error);
        console.error('Detalles del error:', error.error);
      }
    });
  }

  // Métodos para manejar archivos
  onFileSelected(event: any, pedido: Pedido) {
    const files: FileList = event.target.files;
    
    if (files.length > 0) {
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        
        // Validar tamaño máximo (5MB)
        if (file.size > 5 * 1024 * 1024) {
          alert(`El archivo "${file.name}" es demasiado grande. Tamaño máximo: 5MB`);
          continue;
        }
        
        // Validar tipo de archivo
        const tiposPermitidos = [
          'image/jpeg', 
          'image/jpg', 
          'image/png', 
          'application/pdf', 
          'application/msword',
          'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
          'text/plain'
        ];
        
        if (!tiposPermitidos.includes(file.type)) {
          alert(`El archivo "${file.name}" no es un tipo válido. Formatos permitidos: JPG, PNG, PDF, DOC, DOCX, TXT`);
          continue;
        }
        
        // Agregar archivo a la lista
        if (!pedido.archivosSeleccionados) {
          pedido.archivosSeleccionados = [];
        }
        
        const archivoSeleccionado: ArchivoSeleccionado = { file: file };
        pedido.archivosSeleccionados.push(archivoSeleccionado);
        
        // Generar vista previa para imágenes
        if (file.type.startsWith('image/')) {
          this.generarVistaPrevia(file, pedido, archivoSeleccionado);
        }
      }
      
      // Limpiar el input
      event.target.value = '';
    }
  }

  generarVistaPrevia(file: File, pedido: Pedido, archivoSeleccionado: ArchivoSeleccionado) {
    const reader = new FileReader();
    
    reader.onload = (e: any) => {
      archivoSeleccionado.urlPrevia = e.target.result;
      
      // Actualizar lista de imágenes para vista previa
      if (!pedido.imagenesPrevia) {
        pedido.imagenesPrevia = [];
      }
      
      const imagenPrevia: ImagenPrevia = {
        url: e.target.result,
        nombre: file.name,
        tipo: file.type
      };
      
      pedido.imagenesPrevia.push(imagenPrevia);
    };
    
    reader.readAsDataURL(file);
  }

  eliminarArchivo(pedido: Pedido, index: number) {
    if (pedido.archivosSeleccionados && pedido.archivosSeleccionados[index]) {
      const archivoEliminado = pedido.archivosSeleccionados[index];
      
      // Eliminar también de la vista previa si es una imagen
      if (archivoEliminado.file.type.startsWith('image/') && pedido.imagenesPrevia) {
        pedido.imagenesPrevia = pedido.imagenesPrevia.filter((img: ImagenPrevia) => 
          img.nombre !== archivoEliminado.file.name
        );
      }
      
      pedido.archivosSeleccionados.splice(index, 1);
    }
  }

  // Método para eliminar archivo por nombre (para usar en la vista previa)
  eliminarArchivoPorNombre(pedido: Pedido, nombreArchivo: string) {
    if (pedido.archivosSeleccionados) {
      const index = pedido.archivosSeleccionados.findIndex(archivo => 
        archivo.file.name === nombreArchivo
      );
      
      if (index !== -1) {
        this.eliminarArchivo(pedido, index);
      }
    }
  }

  getFileIcon(fileType: string): string {
    if (fileType.startsWith('image/')) return '🖼️';
    if (fileType === 'application/pdf') return '📄';
    if (fileType.includes('word') || fileType.includes('document')) return '📝';
    if (fileType === 'text/plain') return '📃';
    return '📎';
  }

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  // Métodos para el menú desplegable
  togglePedido(pedidoId: number) {
    const pedido = this.pedidos.find(p => p.id === pedidoId);
    if (pedido) {
      pedido.mostrarDetalles = !pedido.mostrarDetalles;
      pedido.mostrarMenu = false;
    }
  }

  toggleMenu(pedidoId: number, event?: Event) {
    if (event) {
      event.stopPropagation();
    }
    
    this.pedidos.forEach(p => {
      if (p.id !== pedidoId) {
        p.mostrarMenu = false;
      }
    });
    
    const pedido = this.pedidos.find(p => p.id === pedidoId);
    if (pedido) {
      pedido.mostrarMenu = !pedido.mostrarMenu;
    }
  }

  // Acciones del menú
  iniciarEntrega(pedido: Pedido) {
    pedido.mostrarFormularioEntrega = true;
    pedido.mostrarDetalles = true;
    pedido.mostrarMenu = false;
  }

  llamarCliente(pedido: Pedido) {
    const telefono = pedido.direccion.telefono || pedido.usuario.telefono;
    if (telefono) {
      if (confirm('¿Deseas llamar al cliente?')) {
        window.open(`tel:${telefono}`, '_self');
      }
    } else {
      alert('No hay número de teléfono disponible para este cliente');
    }
    pedido.mostrarMenu = false;
  }

  verUbicacion(pedido: Pedido) {
    const direccionCompleta = `${pedido.direccion.direccion} ${pedido.direccion.numero}, ${pedido.direccion.distrito || ''}, ${pedido.direccion.provincia || ''}`;
    
    if (confirm('¿Deseas abrir la ubicación en Google Maps?')) {
      const url = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(direccionCompleta)}`;
      window.open(url, '_blank');
    }
    pedido.mostrarMenu = false;
  }

  reportarProblema(pedido: Pedido) {
    const problemas = [
      'Cliente no se encuentra',
      'Dirección incorrecta',
      'Producto dañado',
      'Cliente se arrepintió',
      'Problema de pago',
      'Otro'
    ];
    
    const problema = prompt(
      `Reportar problema para el pedido #${pedido.numeroPedido}:\n\nSelecciona o describe el problema:`, 
      problemas.join('\n')
    );
    
    if (problema && problema.trim()) {
      alert(`Problema reportado para el pedido #${pedido.numeroPedido}: ${problema}`);
    }
    pedido.mostrarMenu = false;
  }

  cancelarEntrega(pedido: Pedido) {
    pedido.mostrarFormularioEntrega = false;
    pedido.codigoVerificacion = '';
    pedido.observaciones = '';
    pedido.archivosSeleccionados = [];
    pedido.imagenesPrevia = [];
  }

  // MÉTODOS PARA SUBIR ARCHIVOS AL BACKEND

  // Método real para subir archivos
  private subirArchivoReal(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      this.repartidorService.subirArchivo(file).subscribe({
        next: (response) => {
          console.log('✅ Archivo subido exitosamente:', response);
          resolve(response.url);
        },
        error: (error) => {
          console.error('❌ Error subiendo archivo:', error);
          reject(error);
        }
      });
    });
  }

  // Método para subir archivos al backend
  private async subirArchivosBackend(archivosSeleccionados: ArchivoSeleccionado[]): Promise<string[]> {
    const archivosSubidos: string[] = [];
    
    for (const archivoInfo of archivosSeleccionados) {
      try {
        console.log('📤 Subiendo archivo:', archivoInfo.file.name);
        const resultado = await this.subirArchivoReal(archivoInfo.file);
        archivosSubidos.push(resultado);
        console.log('✅ Archivo subido:', archivoInfo.file.name, 'URL:', resultado);
      } catch (error) {
        console.error('❌ Error subiendo archivo:', archivoInfo.file.name, error);
        throw error;
      }
    }
    
    return archivosSubidos;
  }

 /// Método para confirmar entrega (MODIFICADO: remover manualmente de la lista)
async confirmarEntrega(pedido: Pedido) {
  console.log('🔍 INICIANDO CONFIRMACIÓN DE ENTREGA');
  
  if (!pedido.codigoVerificacion) {
    alert('❌ Por favor ingresa el código de verificación');
    return;
  }

  if (!confirm('¿Confirmar que la entrega se realizó exitosamente?')) {
    return;
  }

  try {
    let archivosSubidos: string[] = [];
    if (pedido.archivosSeleccionados?.length) {
      archivosSubidos = await this.simularSubidaExitosa(pedido.archivosSeleccionados);
    }

    const reporte: ReporteEntrega = {
      pedido: { id: pedido.id },
      repartidor: { id: this.usuarioActual.id },
      codigoVerificacion: pedido.codigoVerificacion,
      observaciones: pedido.observaciones || 'Entrega exitosa sin observaciones',
      estado: 'ENTREGADO',
      fechaEntrega: new Date().toISOString(),
      archivosAdjuntos: archivosSubidos.length ? JSON.stringify(archivosSubidos) : null
    };

    this.repartidorService.crearReporteEntrega(reporte).subscribe({
      next: (response) => {
        alert(`✅ Entrega registrada exitosamente\n📦 Pedido: ${pedido.numeroPedido}\n🔢 Código: ${pedido.codigoVerificacion}`);
        
        this.pedidos = this.pedidos.filter(p => p.id !== pedido.id);
        this.reportes.push(response);
        this.cancelarEntrega(pedido);
        pedido.mostrarDetalles = false;
        this.actualizarEstadisticas();
      },
      error: (error: any) => {
        console.error('❌ Error:', error);
        this.manejarErrorEntrega(error);
      }
    });
  } catch (error: any) {
    console.error('💥 Error inesperado:', error);
    alert('❌ Error inesperado al procesar la entrega');
  }
}

// Método auxiliar para manejar errores
private manejarErrorEntrega(error: any) {
  const errorMessage = error.error?.message || error.error || error.message || '';
  
  const errores = {
    'Código de verificación incorrecto': '❌ Código de verificación incorrecto\n\nSolicita al cliente el código correcto',
    'no tiene código de verificación': '❌ Pedido sin código de verificación\n\nContacta al administrador',
    'Ya existe un reporte': '⚠️ Ya existe un reporte para este pedido',
    'Repartidor no encontrado': '❌ Error de autenticación',
    'Pedido no encontrado': '❌ El pedido no existe'
  };

  const mensaje = Object.entries(errores).find(([key]) => 
    errorMessage.includes(key)
  )?.[1] || `❌ Error en el codigo de verificación`;

  alert(mensaje);
}

// Método para actualizar solo las estadísticas
actualizarEstadisticas() {
  console.log('📊 Actualizando estadísticas...');
  
  // Actualizar reportes del repartidor
  if (this.usuarioActual) {
    this.repartidorService.getReportesRepartidor(this.usuarioActual.id).subscribe({
      next: (reportes) => {
        this.reportes = reportes;
        console.log('📋 Reportes actualizados:', reportes.length);
      },
      error: (error) => {
        console.error('❌ Error actualizando reportes:', error);
      }
    });
  }
}
  // Método alternativo si prefieres simular la subida (sin backend)
  private async simularSubidaExitosa(archivosSeleccionados: ArchivoSeleccionado[]): Promise<string[]> {
    return new Promise((resolve) => {
      setTimeout(() => {
        const urlsSimuladas = archivosSeleccionados.map(archivoInfo => 
          `https://ejemplo.com/uploads/${Date.now()}_${archivoInfo.file.name}`
        );
        resolve(urlsSimuladas);
      }, 500);
    });
  }

  // Resto de métodos existentes...
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.dropdown')) {
      this.pedidos.forEach(pedido => {
        pedido.mostrarMenu = false;
      });
    }
  }

  logout() {
    if (confirm('¿Estás seguro de que deseas cerrar sesión?')) {
      this.authService.logout();
    }
  }

// Estadísticas
get pedidosPendientes(): number {
  const pendientes = this.pedidos.filter(p => p.estado === 'ENVIADO').length;
  console.log('📊 Calculando pedidos pendientes:', pendientes);
  return pendientes;
}

get totalEntregas(): number {
  console.log('📊 Total de entregas en reportes:', this.reportes.length);
  return this.reportes.length;
}
}