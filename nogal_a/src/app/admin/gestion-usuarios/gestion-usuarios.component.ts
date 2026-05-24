import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../services/admin.service';


@Component({
  selector: 'app-gestion-usuarios',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './gestion-usuarios.component.html',
  styleUrls: ['./gestion-usuarios.component.css']
})
export class GestionUsuariosComponent implements OnInit {
  private adminService = inject(AdminService);
  private router = inject(Router);

  usuarios: any[] = [];
  usuariosFiltrados: any[] = [];
  cargando: boolean = true;
  filtroRol: string = '';
  filtroBusqueda: string = '';

  ngOnInit() {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.cargando = true;
    this.adminService.obtenerTodosUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios = usuarios;
        this.usuariosFiltrados = usuarios;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando usuarios:', err);
        this.cargando = false;
      }
    });
  }

  aplicarFiltros() {
    this.usuariosFiltrados = this.usuarios.filter(usuario => {
      const coincideRol = !this.filtroRol || usuario.rol === this.filtroRol;
      const coincideBusqueda = !this.filtroBusqueda || 
        usuario.nombres.toLowerCase().includes(this.filtroBusqueda.toLowerCase()) ||
        usuario.apellidos.toLowerCase().includes(this.filtroBusqueda.toLowerCase()) ||
        usuario.username.toLowerCase().includes(this.filtroBusqueda.toLowerCase());
      
      return coincideRol && coincideBusqueda;
    });
  }

  limpiarFiltros() {
    this.filtroRol = '';
    this.filtroBusqueda = '';
    this.usuariosFiltrados = this.usuarios;
  }

  getClaseRol(rol: string): string {
    const clases: {[key: string]: string} = {
      'admin': 'bg-danger',
      'logistico': 'bg-warning',
      'repartidor': 'bg-info',
      'cliente': 'bg-success'
    };
    return clases[rol] || 'bg-secondary';
  }

  volverAlPanel() {
    this.router.navigate(['/panel-admin']);
  }

  formatearFecha(fechaString: string): string {
    if (!fechaString) return 'N/A';
    try {
      const fecha = new Date(fechaString);
      return fecha.toLocaleDateString('es-PE');
    } catch {
      return 'Fecha inválida';
    }
  }
}