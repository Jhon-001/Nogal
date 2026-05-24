package nogal.com.nogal.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import nogal.com.nogal.models.PedidoModel;
import nogal.com.nogal.models.ReporteEntregaModel;
import nogal.com.nogal.repositories.IReporteEntregaRepository;
import nogal.com.nogal.services.RepartidorService;

@RestController
@RequestMapping("/repartidor")
@CrossOrigin(origins = "http://localhost:4200")
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;
    
    @Autowired // ✅ FALTA ESTA ANOTACIÓN
    private IReporteEntregaRepository reporteEntregaRepository; // ✅ Inyectar el repository


    // Obtener pedidos para reparto
    @GetMapping("/pedidos")
    public ResponseEntity<?> obtenerPedidosParaReparto() {
        try {
            System.out.println("🚚 GET /repartidor/pedidos - Obteniendo pedidos para reparto");
            ArrayList<PedidoModel> pedidos = repartidorService.obtenerPedidosParaReparto();
            return ResponseEntity.ok(pedidos);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo pedidos para reparto: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Obtener pedidos asignados al repartidor
    @GetMapping("/pedidos/{repartidorId}")
    public ResponseEntity<?> obtenerPedidosAsignados(@PathVariable Long repartidorId) {
        try {
            System.out.println("🚚 GET /repartidor/pedidos/" + repartidorId);
            ArrayList<PedidoModel> pedidos = repartidorService.obtenerPedidosAsignados(repartidorId);
            return ResponseEntity.ok(pedidos);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo pedidos asignados: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    // Crear reporte de entrega
    @PostMapping("/reporte")
    public ResponseEntity<?> crearReporteEntrega(@RequestBody ReporteEntregaModel reporte) {
        try {
            System.out.println("📋 POST /repartidor/reporte");
            System.out.println("Pedido ID: " + reporte.getPedido().getId());
            System.out.println("Repartidor ID: " + reporte.getRepartidor().getId());
            System.out.println("Código verificación: " + reporte.getCodigoVerificacion());

            ReporteEntregaModel reporteCreado = repartidorService.crearReporteEntrega(reporte);
            return ResponseEntity.ok(reporteCreado);
        } catch (RuntimeException ex) {
            System.err.println("❌ Error creando reporte: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("❌ Error interno: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    // Obtener reportes del repartidor
    @GetMapping("/reportes/{repartidorId}")
    public ResponseEntity<?> obtenerReportesRepartidor(@PathVariable Long repartidorId) {
        try {
            System.out.println("📋 GET /repartidor/reportes/" + repartidorId);
            ArrayList<ReporteEntregaModel> reportes = repartidorService.obtenerReportesPorRepartidor(repartidorId);
            return ResponseEntity.ok(reportes);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo reportes: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Obtener reporte específico
    @GetMapping("/reporte/{id}")
    public ResponseEntity<?> obtenerReporte(@PathVariable Long id) {
        try {
            System.out.println("📋 GET /repartidor/reporte/" + id);
            Optional<ReporteEntregaModel> reporte = repartidorService.obtenerReportePorId(id);
            if (reporte.isPresent()) {
                return ResponseEntity.ok(reporte.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo reporte: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    // En RepartidorController.java
    @GetMapping("/reporte/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerReportePorPedidoId(@PathVariable Long pedidoId) {
    try {
        System.out.println("🔍 Buscando reporte para pedido ID: " + pedidoId);
        
        // Buscar el reporte por ID de pedido
        Optional<ReporteEntregaModel> reporte = reporteEntregaRepository.findByPedidoId(pedidoId);
        
        if (reporte.isPresent()) {
            System.out.println("✅ Reporte encontrado: " + reporte.get().getId());
            return ResponseEntity.ok(reporte.get());
        } else {
            System.out.println("❌ No se encontró reporte para el pedido: " + pedidoId);
            return ResponseEntity.notFound().build();
        }
    } catch (Exception ex) {
        System.err.println("💥 Error buscando reporte: " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
    // NUEVO: Reportar problema en la entrega
    @PostMapping("/problema")
    public ResponseEntity<?> reportarProblema(@RequestBody Map<String, Object> problemaRequest) {
        try {
            System.out.println("⚠️ POST /repartidor/problema");
            System.out.println("Problema request: " + problemaRequest);
            
            Long pedidoId = Long.valueOf(problemaRequest.get("pedidoId").toString());
            Long repartidorId = Long.valueOf(problemaRequest.get("repartidorId").toString());
            String descripcion = problemaRequest.get("descripcion").toString();
            String tipoProblema = problemaRequest.get("tipoProblema") != null ? 
                problemaRequest.get("tipoProblema").toString() : "ENTREGA";
            
            // Llamar al servicio para reportar el problema
            String resultado = repartidorService.reportarProblema(pedidoId, repartidorId, descripcion, tipoProblema);
            return ResponseEntity.ok(Map.of("mensaje", resultado));
            
        } catch (Exception ex) {
            System.err.println("❌ Error reportando problema: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.badRequest().body("Error al reportar el problema: " + ex.getMessage());
        }
    }

    // NUEVO: Actualizar estado del pedido
    @PutMapping("/pedidos/{pedidoId}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Long pedidoId, @RequestBody Map<String, String> estadoRequest) {
        try {
            System.out.println("🔄 PUT /repartidor/pedidos/" + pedidoId + "/estado");
            String nuevoEstado = estadoRequest.get("estado");
            System.out.println("Nuevo estado: " + nuevoEstado);
            
            String resultado = repartidorService.actualizarEstadoPedido(pedidoId, nuevoEstado);
            return ResponseEntity.ok(Map.of("mensaje", resultado));
            
        } catch (Exception ex) {
            System.err.println("❌ Error actualizando estado del pedido: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.badRequest().body("Error al actualizar estado: " + ex.getMessage());
        }
    }

    // NUEVO: Obtener historial de entregas del repartidor
    @GetMapping("/historial/{repartidorId}")
    public ResponseEntity<?> obtenerHistorialEntregas(@PathVariable Long repartidorId) {
        try {
            System.out.println("📊 GET /repartidor/historial/" + repartidorId);
            ArrayList<ReporteEntregaModel> historial = repartidorService.obtenerHistorialEntregas(repartidorId);
            return ResponseEntity.ok(historial);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo historial: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // NUEVO: Subir foto de entrega (opcional - si necesitas esta funcionalidad)
    @PostMapping("/subir-foto")
    public ResponseEntity<?> subirFotoEntrega(@RequestParam("foto") MultipartFile foto, 
                                            @RequestParam("reporteId") Long reporteId) {
        try {
            System.out.println("📸 POST /repartidor/subir-foto");
            System.out.println("Reporte ID: " + reporteId);
            System.out.println("Nombre archivo: " + foto.getOriginalFilename());
            System.out.println("Tamaño: " + foto.getSize());
            
            // Aquí implementarías la lógica para guardar la foto
            // Por ahora solo simulamos la respuesta
            String fotoUrl = "/uploads/entregas/" + reporteId + "_" + foto.getOriginalFilename();
            String resultado = repartidorService.guardarFotoEntrega(reporteId, fotoUrl);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", resultado,
                "fotoUrl", fotoUrl
            ));
            
        } catch (Exception ex) {
            System.err.println("❌ Error subiendo foto: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.badRequest().body("Error al subir foto: " + ex.getMessage());
        }
    }
    // NUEVO: Subir archivo de entrega
@PostMapping("/subir-archivo")
public ResponseEntity<?> subirArchivo(@RequestParam("archivo") MultipartFile archivo) {
    try {
        System.out.println("📎 POST /repartidor/subir-archivo");
        System.out.println("Nombre archivo: " + archivo.getOriginalFilename());
        System.out.println("Tipo: " + archivo.getContentType());
        System.out.println("Tamaño: " + archivo.getSize());
        
        // Validar que el archivo no esté vacío
        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }
        
        // Validar tamaño máximo (5MB)
        if (archivo.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("El archivo es demasiado grande. Tamaño máximo: 5MB");
        }
        
        // Validar tipos de archivo permitidos
        String contentType = archivo.getContentType();
        String[] tiposPermitidos = {
            "image/jpeg", "image/jpg", "image/png", 
            "application/pdf", 
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain"
        };
        
        boolean tipoValido = false;
        for (String tipo : tiposPermitidos) {
            if (tipo.equals(contentType)) {
                tipoValido = true;
                break;
            }
        }
        
        if (!tipoValido) {
            return ResponseEntity.badRequest().body("Tipo de archivo no permitido");
        }
        
        // Crear directorio de uploads si no existe
        String uploadDir = "uploads/entregas/";
        Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("📁 Directorio creado: " + uploadPath.toAbsolutePath());
        }
        
        // Generar nombre único para el archivo
        String extension = FilenameUtils.getExtension(archivo.getOriginalFilename());
        String nombreArchivo = System.currentTimeMillis() + "_" + 
                              archivo.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
        
        Path filePath = uploadPath.resolve(nombreArchivo);
        
        // Guardar archivo en el sistema
        Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        String urlArchivo = "/" + uploadDir + nombreArchivo;
        
        System.out.println("✅ Archivo guardado: " + filePath.toAbsolutePath());
        System.out.println("🔗 URL del archivo: " + urlArchivo);
        
        return ResponseEntity.ok(Map.of(
            "url", urlArchivo,
            "mensaje", "Archivo subido exitosamente"
        ));
        
    } catch (IOException ex) {
        System.err.println("❌ Error de IO subiendo archivo: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(500).body("Error al guardar el archivo: " + ex.getMessage());
    } catch (Exception ex) {
        System.err.println("❌ Error subiendo archivo: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(500).body("Error interno al subir archivo: " + ex.getMessage());
    }
}

// NUEVO: Subir múltiples archivos
@PostMapping("/subir-archivos")
public ResponseEntity<?> subirArchivos(@RequestParam("archivos") MultipartFile[] archivos) {
    try {
        System.out.println("📎 POST /repartidor/subir-archivos");
        System.out.println("Número de archivos: " + archivos.length);
        
        List<String> urls = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        
        // Crear directorio de uploads si no existe
        String uploadDir = "uploads/entregas/";
        Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        for (MultipartFile archivo : archivos) {
            if (!archivo.isEmpty()) {
                try {
                    // Validar tamaño máximo (5MB)
                    if (archivo.getSize() > 5 * 1024 * 1024) {
                        errores.add("Archivo " + archivo.getOriginalFilename() + " es demasiado grande");
                        continue;
                    }
                    
                    // Generar nombre único
                    String nombreArchivo = System.currentTimeMillis() + "_" + 
                                          archivo.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
                    Path filePath = uploadPath.resolve(nombreArchivo);
                    
                    // Guardar archivo
                    Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    
                    String urlArchivo = "/" + uploadDir + nombreArchivo;
                    urls.add(urlArchivo);
                    
                    System.out.println("   ✅ Procesado: " + archivo.getOriginalFilename() + " -> " + urlArchivo);
                    
                } catch (Exception e) {
                    String errorMsg = "Error procesando " + archivo.getOriginalFilename() + ": " + e.getMessage();
                    errores.add(errorMsg);
                    System.err.println("   ❌ " + errorMsg);
                }
            }
        }
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("urls", urls);
        respuesta.put("mensaje", urls.size() + " archivos subidos exitosamente");
        
        if (!errores.isEmpty()) {
            respuesta.put("errores", errores);
            respuesta.put("advertencia", "Algunos archivos no pudieron ser procesados");
        }
        
        return ResponseEntity.ok(respuesta);
        
    } catch (Exception ex) {
        System.err.println("❌ Error subiendo archivos: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(500).body("Error interno al subir archivos: " + ex.getMessage());
    }
}
}