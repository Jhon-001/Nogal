package nogal.com.nogal.services;

import java.util.ArrayList;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nogal.com.nogal.models.DetallePedidoModel;
import nogal.com.nogal.models.PedidoModel;
import nogal.com.nogal.models.ReporteEntregaModel;
import nogal.com.nogal.models.UsuarioModel;
import nogal.com.nogal.repositories.IPedidoRepository;
import nogal.com.nogal.repositories.IReporteEntregaRepository;
import nogal.com.nogal.repositories.IUsuarioRepository;

@Service
public class RepartidorService {

    @Autowired
    private IReporteEntregaRepository reporteEntregaRepository;

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    // Obtener pedidos disponibles para reparto CON TODAS LAS RELACIONES
    public ArrayList<PedidoModel> obtenerPedidosParaReparto() {
        System.out.println("🔍 === INICIANDO BÚSQUEDA DE PEDIDOS PARA REPARTO ===");
        
        try {
            // Primero: obtener pedidos básicos con estado ENVIADO
            ArrayList<PedidoModel> pedidosBasicos = pedidoRepository.findByEstado("ENVIADO");
            System.out.println("📦 Pedidos básicos encontrados: " + pedidosBasicos.size());
            
            if (pedidosBasicos.isEmpty()) {
                System.out.println("⚠️ No hay pedidos en estado ENVIADO");
                return new ArrayList<>();
            }
            
            // Segundo: cargar cada pedido individualmente con todas sus relaciones
            ArrayList<PedidoModel> pedidosCompletos = new ArrayList<>();
            
            for (PedidoModel pedidoBasico : pedidosBasicos) {
                try {
                    System.out.println("\n🔄 Cargando pedido ID: " + pedidoBasico.getId() + " - " + pedidoBasico.getNumeroPedido());
                    
                    // Cargar el pedido completo desde la base de datos
                    Optional<PedidoModel> pedidoOpt = pedidoRepository.findById(pedidoBasico.getId());
                    
                    if (pedidoOpt.isPresent()) {
                        PedidoModel pedido = pedidoOpt.get();
                        
                        // ✅ FORZAR CARGA DE TODAS LAS RELACIONES LAZY
                        System.out.println("   📍 Inicializando relaciones...");
                        
                        // 1. Cargar usuario
                        if (pedido.getUsuario() != null) {
                            Hibernate.initialize(pedido.getUsuario());
                            System.out.println("   👤 Usuario: " + pedido.getUsuario().getNombres() + " " + pedido.getUsuario().getApellidos());
                        } else {
                            System.out.println("   ⚠️ Usuario: NULL");
                        }
                        
                        // 2. Cargar dirección
                        if (pedido.getDireccion() != null) {
                            Hibernate.initialize(pedido.getDireccion());
                            System.out.println("   🏠 Dirección: " + pedido.getDireccion().getDireccion());
                        } else {
                            System.out.println("   ⚠️ Dirección: NULL");
                        }
                        
                        // 3. Cargar tarjeta
                        if (pedido.getTarjeta() != null) {
                            Hibernate.initialize(pedido.getTarjeta());
                            System.out.println("   💳 Tarjeta: " + pedido.getTarjeta().getNumeroEnmascarado());
                        } else {
                            System.out.println("   ⚠️ Tarjeta: NULL");
                        }
                        
                        // 4. Cargar detalles y productos
                        if (pedido.getDetalles() != null) {
                            Hibernate.initialize(pedido.getDetalles());
                            System.out.println("   📋 Detalles: " + pedido.getDetalles().size());
                            
                            // Cargar cada producto dentro de los detalles
                            for (DetallePedidoModel detalle : pedido.getDetalles()) {
                                if (detalle.getProducto() != null) {
                                    Hibernate.initialize(detalle.getProducto());
                                    System.out.println("     🎯 Producto: " + detalle.getProducto().getNombre() + 
                                                     " x " + detalle.getCantidad());
                                } else {
                                    System.out.println("     ⚠️ Producto: NULL en detalle");
                                }
                            }
                        } else {
                            System.out.println("   ⚠️ Detalles: NULL");
                        }
                        
                        pedidosCompletos.add(pedido);
                        System.out.println("   ✅ Pedido cargado exitosamente");
                        
                    } else {
                        System.out.println("   ❌ Pedido no encontrado en BD: " + pedidoBasico.getId());
                    }
                    
                } catch (Exception e) {
                    System.err.println("   💥 Error cargando pedido " + pedidoBasico.getId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("\n🎯 === RESUMEN FINAL ===");
            System.out.println("   Total pedidos procesados: " + pedidosCompletos.size());
            System.out.println("   Pedidos listos para reparto: " + pedidosCompletos.size());
            
            return pedidosCompletos;
            
        } catch (Exception e) {
            System.err.println("💥 ERROR GENERAL en obtenerPedidosParaReparto: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Obtener pedidos asignados al repartidor
    public ArrayList<PedidoModel> obtenerPedidosAsignados(Long repartidorId) {
        System.out.println("👤 Buscando pedidos asignados al repartidor ID: " + repartidorId);
        
        // Por ahora, todos los pedidos ENVIADOS están disponibles para cualquier repartidor
        // En el futuro podrías implementar lógica de asignación específica
        ArrayList<PedidoModel> pedidos = obtenerPedidosParaReparto();
        System.out.println("📦 Pedidos asignados encontrados: " + pedidos.size());
        
        return pedidos;
    }

    // Crear reporte de entrega - MODIFICADO: NO cambiar estado del pedido
@Transactional
public ReporteEntregaModel crearReporteEntrega(ReporteEntregaModel reporte) {
    System.out.println("📋 === INICIANDO CREACIÓN DE REPORTE DE ENTREGA ===");
    
    try {
        // 1. Validar que el repartidor existe y es repartidor
        System.out.println("👤 Validando repartidor ID: " + reporte.getRepartidor().getId());
        UsuarioModel repartidor = usuarioRepository.findById(reporte.getRepartidor().getId())
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));

        if (!"repartidor".equals(repartidor.getRol())) {
            throw new RuntimeException("El usuario no tiene rol de repartidor");
        }
        System.out.println("✅ Repartidor validado: " + repartidor.getNombres());

        // 2. Validar que el pedido existe (pero NO validar estado ENVIADO)
        System.out.println("📦 Validando pedido ID: " + reporte.getPedido().getId());
        PedidoModel pedido = pedidoRepository.findById(reporte.getPedido().getId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        System.out.println("✅ Pedido validado: " + pedido.getNumeroPedido() + " (Estado actual: " + pedido.getEstado() + ")");

        // 3. ✅ NUEVA VALIDACIÓN: Verificar que el código de verificación coincida
        System.out.println("🔐 Validando código de verificación...");
        System.out.println("   Código ingresado: " + reporte.getCodigoVerificacion());
        System.out.println("   Código real: " + pedido.getCodigoVerificacion());
        
        if (pedido.getCodigoVerificacion() == null || pedido.getCodigoVerificacion().isEmpty()) {
            throw new RuntimeException("El pedido no tiene código de verificación asignado");
        }
        
        if (!pedido.getCodigoVerificacion().equals(reporte.getCodigoVerificacion())) {
            throw new RuntimeException("Código de verificación incorrecto");
        }
        System.out.println("✅ Código de verificación válido");
        // 5. Validar que no existe ya un reporte para este pedido
        System.out.println("🔍 Verificando si ya existe reporte para el pedido...");
        if (reporteEntregaRepository.existsByPedidoId(pedido.getId())) {
            throw new RuntimeException("Ya existe un reporte de entrega para este pedido");
        }
        System.out.println("✅ No existe reporte previo");

        // 6. Log de archivos adjuntos (si existen)
        if (reporte.getArchivosAdjuntos() != null && !reporte.getArchivosAdjuntos().isEmpty()) {
            System.out.println("📎 Archivos adjuntos: " + reporte.getArchivosAdjuntos());
        } else {
            System.out.println("📎 Sin archivos adjuntos");
        }

        // 7. Log de observaciones
        if (reporte.getObservaciones() != null && !reporte.getObservaciones().isEmpty()) {
            System.out.println("📝 Observaciones: " + reporte.getObservaciones());
        } else {
            System.out.println("📝 Sin observaciones");
        }

        // 8. ⚠️ IMPORTANTE: NO cambiar el estado del pedido (eso lo hace el logístico)
        System.out.println("ℹ️ Manteniendo estado actual del pedido: " + pedido.getEstado() + " (El logístico cambiará el estado)");

        // 9. Guardar el reporte
        System.out.println("💾 Guardando reporte de entrega...");
        ReporteEntregaModel reporteGuardado = reporteEntregaRepository.save(reporte);
        System.out.println("✅ Reporte de entrega creado con ID: " + reporteGuardado.getId());

        System.out.println("🎉 === REPORTE CREADO EXITOSAMENTE ===");
        return reporteGuardado;
        
    } catch (Exception e) {
        System.err.println("💥 ERROR en crearReporteEntrega: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}

    // Obtener reportes del repartidor
    public ArrayList<ReporteEntregaModel> obtenerReportesPorRepartidor(Long repartidorId) {
        System.out.println("📊 Obteniendo reportes del repartidor ID: " + repartidorId);
        
        try {
            ArrayList<ReporteEntregaModel> reportes = reporteEntregaRepository.findByRepartidorId(repartidorId);
            System.out.println("✅ Reportes encontrados: " + reportes.size());
            
            // Cargar relaciones de cada reporte
            for (ReporteEntregaModel reporte : reportes) {
                if (reporte.getPedido() != null) {
                    Hibernate.initialize(reporte.getPedido());
                    // También cargar relaciones del pedido si es necesario
                    if (reporte.getPedido().getUsuario() != null) {
                        Hibernate.initialize(reporte.getPedido().getUsuario());
                    }
                }
            }
            
            return reportes;
        } catch (Exception e) {
            System.err.println("💥 ERROR obteniendo reportes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Obtener reporte por ID
    public Optional<ReporteEntregaModel> obtenerReportePorId(Long id) {
        System.out.println("🔍 Buscando reporte por ID: " + id);
        return reporteEntregaRepository.findById(id);
    }

    // Método auxiliar: verificar si un usuario es repartidor
    public boolean esRepartidor(Long usuarioId) {
        try {
            Optional<UsuarioModel> usuario = usuarioRepository.findById(usuarioId);
            return usuario.isPresent() && "repartidor".equals(usuario.get().getRol());
        } catch (Exception e) {
            System.err.println("Error verificando rol de repartidor: " + e.getMessage());
            return false;
        }
    }

    // Métodos adicionales para funcionalidades extra

    public String reportarProblema(Long pedidoId, Long repartidorId, String descripcion, String tipoProblema) {
        // Implementar lógica para guardar el problema
        // Por ejemplo, actualizar el estado del pedido a "PROBLEMA"
        System.out.println("Reportando problema - Pedido: " + pedidoId + ", Repartidor: " + repartidorId);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Tipo: " + tipoProblema);
        
        // Aquí actualizas el estado del pedido en la base de datos
        // pedidoRepository.actualizarEstado(pedidoId, "PROBLEMA");
        
        return "Problema reportado exitosamente";
    }

    public String actualizarEstadoPedido(Long pedidoId, String nuevoEstado) {
        // Implementar lógica para actualizar estado
        System.out.println("Actualizando pedido " + pedidoId + " a estado: " + nuevoEstado);
        
        // pedidoRepository.actualizarEstado(pedidoId, nuevoEstado);
        
        return "Estado actualizado exitosamente a: " + nuevoEstado;
    }

    public ArrayList<ReporteEntregaModel> obtenerHistorialEntregas(Long repartidorId) {
        // Implementar lógica para obtener historial
        System.out.println("Obteniendo historial para repartidor: " + repartidorId);
        
        // return reporteRepository.findByRepartidorIdOrderByFechaEntregaDesc(repartidorId);
        return new ArrayList<>(); // temporal
    }

    public String guardarFotoEntrega(Long reporteId, String fotoUrl) {
        // Implementar lógica para guardar URL de foto
        System.out.println("Guardando foto para reporte: " + reporteId + " - URL: " + fotoUrl);
        
        // reporteRepository.actualizarFotoUrl(reporteId, fotoUrl);
        
        return "Foto guardada exitosamente";
    }
}