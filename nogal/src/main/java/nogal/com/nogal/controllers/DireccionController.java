package nogal.com.nogal.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import nogal.com.nogal.models.DireccionModel;
import nogal.com.nogal.models.UsuarioModel;
import nogal.com.nogal.services.DireccionService;
import nogal.com.nogal.repositories.IUsuarioRepository;

@RestController
@RequestMapping("/direccion")
@CrossOrigin(origins = "http://localhost:4200")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerDireccionesPorUsuario(@PathVariable Long usuarioId) {
        try {
            System.out.println("📍 GET /direccion/usuario/" + usuarioId);
            ArrayList<DireccionModel> direcciones = direccionService.obtenerDireccionesPorUsuario(usuarioId);
            return ResponseEntity.ok(direcciones);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo direcciones: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}/principal")
    public ResponseEntity<?> obtenerDireccionPrincipal(@PathVariable Long usuarioId) {
        try {
            System.out.println("⭐ GET /direccion/usuario/" + usuarioId + "/principal");
            Optional<DireccionModel> direccion = direccionService.obtenerDireccionPrincipal(usuarioId);
            if (direccion.isPresent()) {
                return ResponseEntity.ok(direccion.get());
            } else {
                return ResponseEntity.ok(null);
            }
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo dirección principal: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDireccionPorId(@PathVariable Long id) {
        try {
            System.out.println("📍 GET /direccion/" + id);
            Optional<DireccionModel> direccion = direccionService.obtenerPorId(id);
            if (direccion.isPresent()) {
                return ResponseEntity.ok(direccion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo dirección: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearDireccion(@RequestBody DireccionRequest request) {
        try {
            System.out.println("➕ POST /direccion/crear");
            System.out.println("Usuario ID: " + request.getUsuario().getId());
            
            // Buscar el usuario completo
            UsuarioModel usuario = usuarioRepository.findById(request.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Crear dirección
            DireccionModel direccion = new DireccionModel();
            direccion.setUsuario(usuario);
            direccion.setNombreDireccion(request.getNombreDireccion());
            direccion.setNombreReceptor(request.getNombreReceptor());
            direccion.setApellidosReceptor(request.getApellidosReceptor());
            direccion.setDireccion(request.getDireccion());
            direccion.setNumero(request.getNumero());
            direccion.setDepartamento(request.getDepartamento());
            direccion.setProvincia(request.getProvincia());
            direccion.setDistrito(request.getDistrito());
            direccion.setDptoOficinaCasa(request.getDptoOficinaCasa());
            direccion.setTelefono(request.getTelefono());
            direccion.setEsPrincipal(request.getEsPrincipal());
            direccion.setActiva(true);
            
            DireccionModel direccionCreada = direccionService.crearDireccion(direccion);
            System.out.println("✅ Dirección creada con ID: " + direccionCreada.getId());
            
            return ResponseEntity.ok(direccionCreada);
        } catch (RuntimeException ex) {
            System.err.println("❌ Error creando dirección: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("❌ Error interno: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDireccion(@PathVariable Long id, @RequestBody DireccionRequest request) {
        try {
            System.out.println("📝 PUT /direccion/" + id);
            
            DireccionModel direccion = direccionService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
            
            // Actualizar campos
            direccion.setNombreDireccion(request.getNombreDireccion());
            direccion.setNombreReceptor(request.getNombreReceptor());
            direccion.setApellidosReceptor(request.getApellidosReceptor());
            direccion.setDireccion(request.getDireccion());
            direccion.setNumero(request.getNumero());
            direccion.setDepartamento(request.getDepartamento());
            direccion.setProvincia(request.getProvincia());
            direccion.setDistrito(request.getDistrito());
            direccion.setDptoOficinaCasa(request.getDptoOficinaCasa());
            direccion.setTelefono(request.getTelefono());
            direccion.setEsPrincipal(request.getEsPrincipal());
            
            DireccionModel direccionActualizada = direccionService.actualizarDireccion(id, direccion);
            return ResponseEntity.ok(direccionActualizada);
        } catch (RuntimeException ex) {
            System.err.println("❌ Error actualizando dirección: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("❌ Error interno: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDireccion(@PathVariable Long id) {
        try {
            System.out.println("🗑️ DELETE /direccion/" + id);
            direccionService.eliminarDireccion(id);
            return ResponseEntity.ok("Dirección eliminada correctamente");
        } catch (RuntimeException ex) {
            System.err.println("❌ Error eliminando dirección: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("❌ Error interno: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}/principal")
    public ResponseEntity<?> marcarComoPrincipal(@PathVariable Long id) {
        try {
            System.out.println("⭐ PUT /direccion/" + id + "/principal");
            DireccionModel direccion = direccionService.marcarComoPrincipal(id);
            return ResponseEntity.ok(direccion);
        } catch (RuntimeException ex) {
            System.err.println("❌ Error marcando como principal: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("❌ Error interno: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    // Clase interna para recibir el request
    public static class DireccionRequest {
        private UsuarioId usuario;
        private String nombreDireccion;
        private String nombreReceptor;
        private String apellidosReceptor;
        private String direccion;
        private String numero;
        private String departamento;
        private String provincia;
        private String distrito;
        private String dptoOficinaCasa;
        private String telefono;
        private Boolean esPrincipal;

        // Getters y Setters
        public UsuarioId getUsuario() {
            return usuario;
        }

        public void setUsuario(UsuarioId usuario) {
            this.usuario = usuario;
        }

        public String getNombreDireccion() {
            return nombreDireccion;
        }

        public void setNombreDireccion(String nombreDireccion) {
            this.nombreDireccion = nombreDireccion;
        }

        public String getNombreReceptor() {
            return nombreReceptor;
        }

        public void setNombreReceptor(String nombreReceptor) {
            this.nombreReceptor = nombreReceptor;
        }

        public String getApellidosReceptor() {
            return apellidosReceptor;
        }

        public void setApellidosReceptor(String apellidosReceptor) {
            this.apellidosReceptor = apellidosReceptor;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getDepartamento() {
            return departamento;
        }

        public void setDepartamento(String departamento) {
            this.departamento = departamento;
        }

        public String getProvincia() {
            return provincia;
        }

        public void setProvincia(String provincia) {
            this.provincia = provincia;
        }

        public String getDistrito() {
            return distrito;
        }

        public void setDistrito(String distrito) {
            this.distrito = distrito;
        }

        public String getDptoOficinaCasa() {
            return dptoOficinaCasa;
        }

        public void setDptoOficinaCasa(String dptoOficinaCasa) {
            this.dptoOficinaCasa = dptoOficinaCasa;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public Boolean getEsPrincipal() {
            return esPrincipal;
        }

        public void setEsPrincipal(Boolean esPrincipal) {
            this.esPrincipal = esPrincipal;
        }
    }

    public static class UsuarioId {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}