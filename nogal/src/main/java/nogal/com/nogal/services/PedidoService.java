package nogal.com.nogal.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nogal.com.nogal.models.DetallePedidoModel;
import nogal.com.nogal.models.PedidoModel;
import nogal.com.nogal.models.ProductoModel;
import nogal.com.nogal.models.UsuarioModel;
import nogal.com.nogal.repositories.IDireccionRepository;
import nogal.com.nogal.repositories.IPedidoRepository;
import nogal.com.nogal.repositories.IProductoRepository;
import nogal.com.nogal.repositories.ITarjetaRepository;
import nogal.com.nogal.repositories.IUsuarioRepository;

@Service
public class PedidoService {

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IDireccionRepository direccionRepository;

    @Autowired
    private ITarjetaRepository tarjetaRepository;

    @Autowired
    private IProductoRepository productoRepository;

    // Obtener todos los pedidos de un usuario
    public ArrayList<PedidoModel> obtenerPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaPedidoDesc(usuarioId);
    }
    public Map<String, Object> obtenerTendenciaVentas30Dias() {
    Map<String, Object> resultado = new HashMap<>();
    
    // Obtener todos los pedidos
    ArrayList<PedidoModel> todosPedidos = pedidoRepository.findAllByOrderByFechaPedidoDesc();
    
    // Filtrar pedidos de los últimos 30 días
    LocalDate fechaInicio = LocalDate.now().minusDays(30);
    
    List<PedidoModel> pedidos30Dias = todosPedidos.stream()
        .filter(pedido -> {
            try {
                LocalDate fechaPedido = pedido.getFechaPedido().toLocalDate();
                return !fechaPedido.isBefore(fechaInicio);
            } catch (Exception e) {
                return false;
            }
        })
        .collect(Collectors.toList());
    
    // Agrupar por día
    Map<LocalDate, Double> ventasPorDia = pedidos30Dias.stream()
        .collect(Collectors.groupingBy(
            pedido -> pedido.getFechaPedido().toLocalDate(),
            Collectors.summingDouble(pedido -> pedido.getTotal().doubleValue())
        ));
    
    // Crear datos para los últimos 30 días
    List<Map<String, Object>> tendencia = new ArrayList<>();
    LocalDate fechaActual = LocalDate.now();
    
    for (int i = 29; i >= 0; i--) {
        LocalDate fecha = fechaActual.minusDays(i);
        Double ventas = ventasPorDia.getOrDefault(fecha, 0.0);
        
        Map<String, Object> dia = new HashMap<>();
        dia.put("fecha", fecha.toString());
        dia.put("dia", fecha.getDayOfMonth());
        dia.put("nombre", obtenerNombreDia(fecha));
        dia.put("ventas", ventas);
        
        tendencia.add(dia);
    }
    
    // Calcular totales
    double totalVentas30Dias = pedidos30Dias.stream()
        .mapToDouble(pedido -> pedido.getTotal().doubleValue())
        .sum();
    
    double promedioDiario = totalVentas30Dias / 30;
    
    resultado.put("tendencia", tendencia);
    resultado.put("totalVentas30Dias", totalVentas30Dias);
    resultado.put("promedioDiario", promedioDiario);
    resultado.put("totalPedidos30Dias", pedidos30Dias.size());
    
    return resultado;
}
    // Obtener productos más vendidos
public List<Map<String, Object>> obtenerProductosMasVendidos() {
    // Obtener todos los pedidos
    ArrayList<PedidoModel> todosPedidos = pedidoRepository.findAllByOrderByFechaPedidoDesc();
    
    // Agrupar productos por cantidad vendida
    Map<Long, Map<String, Object>> productosVendidos = new HashMap<>();
    
    for (PedidoModel pedido : todosPedidos) {
        for (DetallePedidoModel detalle : pedido.getDetalles()) {
            Long productoId = detalle.getProducto().getId();
            String productoNombre = detalle.getProducto().getNombre();
            int cantidad = detalle.getCantidad();
            double total = detalle.getSubtotal().doubleValue();
            
            productosVendidos.computeIfAbsent(productoId, k -> {
                Map<String, Object> producto = new HashMap<>();
                producto.put("id", productoId);
                producto.put("nombre", productoNombre);
                producto.put("cantidadTotal", 0);
                producto.put("ventasTotal", 0.0);
                producto.put("precioPromedio", 0.0);
                return producto;
            });
            
            Map<String, Object> producto = productosVendidos.get(productoId);
            producto.put("cantidadTotal", (Integer) producto.get("cantidadTotal") + cantidad);
            producto.put("ventasTotal", (Double) producto.get("ventasTotal") + total);
        }
    }
    
    // Calcular precio promedio y ordenar por cantidad
    for (Map<String, Object> producto : productosVendidos.values()) {
        int cantidadTotal = (Integer) producto.get("cantidadTotal");
        double ventasTotal = (Double) producto.get("ventasTotal");
        producto.put("precioPromedio", ventasTotal / cantidadTotal);
    }
    
    // Ordenar por cantidad descendente y tomar top 10
    return productosVendidos.values().stream()
        .sorted((p1, p2) -> Integer.compare(
            (Integer) p2.get("cantidadTotal"), 
            (Integer) p1.get("cantidadTotal")
        ))
        .limit(10)
        .collect(Collectors.toList());
}

    // Método auxiliar para obtener nombre del día
    private String obtenerNombreDia(LocalDate fecha) {
        String[] nombresDias = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        return nombresDias[fecha.getDayOfWeek().getValue() % 7];
    }

    // Obtener pedido por ID
    public Optional<PedidoModel> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    // Obtener pedido por número
    public Optional<PedidoModel> obtenerPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    // Crear nuevo pedido
    @Transactional
    public PedidoModel crearPedido(PedidoModel pedido) {
        System.out.println("🛒 Creando nuevo pedido...");

        // Validar usuario
        if (!usuarioRepository.existsById(pedido.getUsuario().getId())) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Validar dirección
        if (!direccionRepository.existsById(pedido.getDireccion().getId())) {
            throw new RuntimeException("Dirección no encontrada");
        }

        // Validar tarjeta
        if (!tarjetaRepository.existsById(pedido.getTarjeta().getId())) {
            throw new RuntimeException("Tarjeta no encontrada");
        }

        // Generar número de pedido único
        pedido.setNumeroPedido(generarNumeroPedido());

        pedido.setCodigoVerificacion(generarCodigoVerificacion());
        System.out.println("🔢 Código de verificación generado: " + pedido.getCodigoVerificacion());

        // Validar stock de productos
        for (DetallePedidoModel detalle : pedido.getDetalles()) {
            ProductoModel producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(
                            () -> new RuntimeException("Producto no encontrado: " + detalle.getProducto().getNombre()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre() +
                        ". Disponible: " + producto.getStock() +
                        ", Solicitado: " + detalle.getCantidad());
            }
        }

        // Establecer la relación bidireccional para cada detalle
        for (DetallePedidoModel detalle : pedido.getDetalles()) {
            detalle.setPedido(pedido);
            detalle.calcularSubtotal();
        }

        // Guardar el pedido
        PedidoModel pedidoGuardado = pedidoRepository.save(pedido);

        // Actualizar stock de productos
        for (DetallePedidoModel detalle : pedido.getDetalles()) {
            ProductoModel producto = productoRepository.findById(detalle.getProducto().getId()).get();
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }

        System.out.println("✅ Pedido creado exitosamente: " + pedidoGuardado.getNumeroPedido());
        return pedidoGuardado;
    }

    // Actualizar estado del pedido
    @Transactional
    public PedidoModel actualizarEstado(Long pedidoId, String nuevoEstado) {
        PedidoModel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Validar estado
        if (!esEstadoValido(nuevoEstado)) {
            throw new RuntimeException("Estado no válido: " + nuevoEstado);
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    // Cancelar pedido
    @Transactional
    public PedidoModel cancelarPedido(Long pedidoId) {
        PedidoModel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Solo se puede cancelar si está PENDIENTE o PROCESANDO
        if (!pedido.getEstado().equals("PENDIENTE") && !pedido.getEstado().equals("PROCESANDO")) {
            throw new RuntimeException("No se puede cancelar un pedido en estado: " + pedido.getEstado());
        }

        // Devolver stock
        for (DetallePedidoModel detalle : pedido.getDetalles()) {
            ProductoModel producto = productoRepository.findById(detalle.getProducto().getId()).get();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setEstado("CANCELADO");
        return pedidoRepository.save(pedido);
    }

    // Obtener pedidos por estado
    public ArrayList<PedidoModel> obtenerPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }
    private String generarCodigoVerificacion() {
    Random random = new Random();
    // Generar entre 4 y 6 dígitos
    int longitud = 4 + random.nextInt(3); // 4, 5 o 6
    StringBuilder codigo = new StringBuilder();
    
    for (int i = 0; i < longitud; i++) {
        codigo.append(random.nextInt(10)); // Dígitos del 0-9
    }
    
    return codigo.toString();
    }
    // Generar número de pedido único
    private String generarNumeroPedido() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        String numeroPedido = "PED-" + timestamp + "-" + random;

        // Verificar que no exista
        while (pedidoRepository.existsByNumeroPedido(numeroPedido)) {
            random = String.format("%04d", new Random().nextInt(10000));
            numeroPedido = "PED-" + timestamp + "-" + random;
        }

        return numeroPedido;
    }

    // Validar estado
    private boolean esEstadoValido(String estado) {
        return estado.equals("PENDIENTE") ||
                estado.equals("PROCESANDO") ||
                estado.equals("ENVIADO") ||
                estado.equals("ENTREGADO") ||
                estado.equals("CANCELADO");
    }

    // Contar pedidos por usuario
    public Long contarPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.countByUsuarioId(usuarioId);
    }

    // mis cambios para logistico
    // Obtener todos los pedidos
    public ArrayList<PedidoModel> obtenerTodosLosPedidos() {
        return pedidoRepository.findAllByOrderByFechaPedidoDesc();
    }




    //cambios para los reportes
    //Cambios para reporte de clientes top

    // Obtener clientes top (los que más han comprado)
    public List<Map<String, Object>> obtenerClientesTop() {
        // Obtener todos los pedidos
        ArrayList<PedidoModel> todosPedidos = pedidoRepository.findAllByOrderByFechaPedidoDesc();
        
        // Agrupar por cliente
        Map<Long, Map<String, Object>> clientesAgrupados = new HashMap<>();
        
        for (PedidoModel pedido : todosPedidos) {
            Long clienteId = pedido.getUsuario().getId();
            UsuarioModel cliente = pedido.getUsuario();
            
            clientesAgrupados.computeIfAbsent(clienteId, k -> {
                Map<String, Object> clienteData = new HashMap<>();
                clienteData.put("id", clienteId);
                clienteData.put("nombres", cliente.getNombres());
                clienteData.put("apellidos", cliente.getApellidos());
                clienteData.put("email", cliente.getEmail());
                clienteData.put("totalPedidos", 0);
                clienteData.put("totalGastado", 0.0);
                clienteData.put("ultimaCompra", pedido.getFechaPedido());
                return clienteData;
            });
            
            Map<String, Object> clienteData = clientesAgrupados.get(clienteId);
            clienteData.put("totalPedidos", (Integer) clienteData.get("totalPedidos") + 1);
            clienteData.put("totalGastado", (Double) clienteData.get("totalGastado") + pedido.getTotal().doubleValue());
            
            // Actualizar última compra si es más reciente
            if (pedido.getFechaPedido().isAfter(((LocalDateTime) clienteData.get("ultimaCompra")))) {
                clienteData.put("ultimaCompra", pedido.getFechaPedido());
            }
        }
        
        // Ordenar por total gastado y tomar top 10
        return clientesAgrupados.values().stream()
            .sorted((c1, c2) -> Double.compare(
                (Double) c2.get("totalGastado"), 
                (Double) c1.get("totalGastado")
            ))
            .limit(10)
            .collect(Collectors.toList());
    }

    // Obtener evolución de ventas por día
    public List<Map<String, Object>> obtenerEvolucionVentas30Dias() {
        // Obtener todos los pedidos
        ArrayList<PedidoModel> todosPedidos = pedidoRepository.findAllByOrderByFechaPedidoDesc();
        
        // Crear mapa para agrupar por día
        Map<LocalDate, Double> ventasPorDia = new HashMap<>();
        
        for (PedidoModel pedido : todosPedidos) {
            if (pedido.getFechaPedido() != null) {
                LocalDate fechaPedido = pedido.getFechaPedido().toLocalDate();
                LocalDate fechaLimite = LocalDate.now().minusDays(30);
                
                // Solo considerar últimos 30 días
                if (!fechaPedido.isBefore(fechaLimite)) {
                    double totalDia = ventasPorDia.getOrDefault(fechaPedido, 0.0);
                    ventasPorDia.put(fechaPedido, totalDia + pedido.getTotal().doubleValue());
                }
            }
        }
        
        // Ordenar por fecha y convertir a lista
        return ventasPorDia.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> {
                Map<String, Object> dia = new HashMap<>();
                dia.put("fecha", entry.getKey().toString());
                dia.put("nombre", obtenerNombreDia(entry.getKey()));
                dia.put("ventas", entry.getValue());
                return dia;
            })
            .collect(Collectors.toList());
    }


}