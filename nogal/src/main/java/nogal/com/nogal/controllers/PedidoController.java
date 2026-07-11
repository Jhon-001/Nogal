package nogal.com.nogal.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nogal.com.nogal.models.PedidoModel;
import nogal.com.nogal.services.PedidoService;

@RestController
@RequestMapping("/pedido")
@CrossOrigin(origins = {"http://localhost:4200", "https://brave-bush-0f4d5fa10.7.azurestaticapps.net"})
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Obtener pedidos por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerPedidosPorUsuario(@PathVariable Long usuarioId) {
        try {
            System.out.println("📦 GET /pedido/usuario/" + usuarioId);
            ArrayList<PedidoModel> pedidos = pedidoService.obtenerPedidosPorUsuario(usuarioId);
            return ResponseEntity.ok(pedidos);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo pedidos: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Obtener pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPedidoPorId(@PathVariable Long id) {
        try {
            System.out.println("📦 GET /pedido/" + id);
            Optional<PedidoModel> pedido = pedidoService.obtenerPorId(id);
            if (pedido.isPresent()) {
                return ResponseEntity.ok(pedido.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo pedido: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Obtener pedido por número
    @GetMapping("/numero/{numeroPedido}")
    public ResponseEntity<?> obtenerPedidoPorNumero(@PathVariable String numeroPedido) {
        try {
            System.out.println("📦 GET /pedido/numero/" + numeroPedido);
            Optional<PedidoModel> pedido = pedidoService.obtenerPorNumeroPedido(numeroPedido);
            if (pedido.isPresent()) {
                return ResponseEntity.ok(pedido.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo pedido: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Crear nuevo pedido
    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestBody PedidoModel pedido) {
        try {
            System.out.println("➕ POST /pedido/crear");
            System.out.println("Usuario ID: " + pedido.getUsuario().getId());
            System.out.println("Dirección ID: " + pedido.getDireccion().getId());
            System.out.println("Tarjeta ID: " + pedido.getTarjeta().getId());
            System.out.println("Cantidad de productos: " + pedido.getDetalles().size());

            PedidoModel pedidoCreado = pedidoService.crearPedido(pedido);
            return ResponseEntity.ok(pedidoCreado);
        } catch (RuntimeException ex) {
            System.err.println("❌ Error creando pedido: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("❌ Error interno: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    // Actualizar estado del pedido
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody EstadoRequest request) {
        try {
            System.out.println("📝 PUT /pedido/" + id + "/estado");
            System.out.println("Nuevo estado: " + request.getEstado());

            PedidoModel pedidoActualizado = pedidoService.actualizarEstado(id, request.getEstado());
            return ResponseEntity.ok(pedidoActualizado);
        } catch (RuntimeException ex) {
            System.err.println("❌ Error actualizando estado: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("❌ Error interno: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    // Cancelar pedido
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            System.out.println("🚫 PUT /pedido/" + id + "/cancelar");
            PedidoModel pedidoCancelado = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedidoCancelado);
        } catch (RuntimeException ex) {
            System.err.println("❌ Error cancelando pedido: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("❌ Error interno: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    // Obtener pedidos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPedidosPorEstado(@PathVariable String estado) {
        try {
            System.out.println("📦 GET /pedido/estado/" + estado);
            ArrayList<PedidoModel> pedidos = pedidoService.obtenerPedidosPorEstado(estado);
            return ResponseEntity.ok(pedidos);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo pedidos por estado: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Clase interna para request de actualización de estado
    public static class EstadoRequest {
        private String estado;

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }

    // cambios para logistico
    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodosLosPedidos() {
        try {
            System.out.println("📦 GET /pedido/todos - Obteniendo todos los pedidos");
            ArrayList<PedidoModel> pedidos = pedidoService.obtenerTodosLosPedidos();
            return ResponseEntity.ok(pedidos);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo todos los pedidos: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    // Nuevo endpoint: Productos más vendidos
    @GetMapping("/estadisticas/productos-mas-vendidos")
    public ResponseEntity<?> obtenerProductosMasVendidos() {
    try {
        System.out.println("🏆 GET /pedido/estadisticas/productos-mas-vendidos");
        
        List<Map<String, Object>> productosMasVendidos = pedidoService.obtenerProductosMasVendidos();
        return ResponseEntity.ok(productosMasVendidos);
        
    } catch (Exception ex) {
        System.err.println("❌ Error obteniendo productos más vendidos: " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

    @GetMapping("/estadisticas/tendencia-ventas")
    public ResponseEntity<?> obtenerTendenciaVentas() {
    try {
        System.out.println("📈 GET /pedido/estadisticas/tendencia-ventas - Obteniendo tendencia de ventas");
        
        Map<String, Object> tendencia = pedidoService.obtenerTendenciaVentas30Dias();
        return ResponseEntity.ok(tendencia);
        
    } catch (Exception ex) {
        System.err.println("❌ Error obteniendo tendencia de ventas: " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    
}



//cambios para los reportes 

// Obtener top clientes
@GetMapping("/estadisticas/clientes-top")
public ResponseEntity<?> obtenerClientesTop() {
    try {
        System.out.println("🏆 GET /pedido/estadisticas/clientes-top");
        
        List<Map<String, Object>> clientesTop = pedidoService.obtenerClientesTop();
        return ResponseEntity.ok(clientesTop);
        
    } catch (Exception ex) {
        System.err.println("❌ Error obteniendo clientes top: " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}


// Obtener evolución de ventas
@GetMapping("/estadisticas/evolucion-ventas/{periodo}")
public ResponseEntity<?> obtenerEvolucionVentas(@PathVariable String periodo) {
    try {
        System.out.println("📈 GET /pedido/estadisticas/evolucion-ventas/" + periodo);
        
        List<Map<String, Object>> evolucionVentas = pedidoService.obtenerEvolucionVentas30Dias();
        return ResponseEntity.ok(evolucionVentas);
        
    } catch (Exception ex) {
        System.err.println("❌ Error obteniendo evolución de ventas: " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
}