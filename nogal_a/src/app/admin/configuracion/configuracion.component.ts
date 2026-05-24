import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; // ✅ Agregar esta importación


@Component({
  selector: 'app-configuracion',
  standalone: true,
  imports: [CommonModule, RouterModule,FormsModule],
  templateUrl: './configuracion.component.html',
  styleUrls: ['./configuracion.component.css']
})
export class ConfiguracionComponent {
  private router = inject(Router);

  configuraciones = {
    notificaciones: true,
    reportesAutomaticos: false,
    modoOscuro: true,
    idioma: 'es'
  };

  volverAlPanel() {
    this.router.navigate(['/panel-admin']);
  }

  guardarConfiguracion() {
    alert('✅ Configuración guardada exitosamente');
    // Aquí puedes implementar la lógica para guardar en el backend
  }
}