package nogal.com.nogal.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import nogal.com.nogal.models.TarjetaModel;
import nogal.com.nogal.models.UsuarioModel;
import nogal.com.nogal.services.TarjetaService;
import nogal.com.nogal.repositories.IUsuarioRepository;

@RestController
@RequestMapping("/tarjeta")
@CrossOrigin(origins = "http://localhost:4200")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @GetMapping("/usuario/{usuarioId}")
    public ArrayList<TarjetaModel> getTarjetasPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        return this.tarjetaService.obtenerTarjetasPorUsuario(usuarioId);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> nuevaTarjeta(@RequestBody TarjetaRequest request) {
        try {
            System.out.println("📝 Creando tarjeta para usuario: " + request.getUsuario().getId());
            
            // Buscar el usuario completo
            UsuarioModel usuario = usuarioRepository.findById(request.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Crear tarjeta
            TarjetaModel tarjeta = new TarjetaModel();
            tarjeta.setUsuario(usuario);
            tarjeta.setNumeroEnmascarado(request.getNumeroEnmascarado());
            tarjeta.setTipo(request.getTipo());
            tarjeta.setNombreTitular(request.getNombreTitular());
            tarjeta.setMesExpiracion(request.getMesExpiracion());
            tarjeta.setAnioExpiracion(request.getAnioExpiracion());
            tarjeta.setPredeterminada(request.getPredeterminada());
            
            TarjetaModel tarjetaCreada = this.tarjetaService.guardarTarjeta(tarjeta);
            System.out.println("✅ Tarjeta creada con ID: " + tarjetaCreada.getId());
            
            return ResponseEntity.ok(tarjetaCreada);
        } catch (RuntimeException ex) {
            System.err.println("❌ Error: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTarjeta(@RequestBody TarjetaModel tarjeta, @PathVariable Long id) {
        try {
            TarjetaModel tarjetaActualizada = this.tarjetaService.actualizarTarjeta(tarjeta, id);
            return ResponseEntity.ok(tarjetaActualizada);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTarjeta(@PathVariable("id") Long id) {
        try {
            this.tarjetaService.eliminarTarjeta(id);
            return ResponseEntity.ok("Tarjeta eliminada correctamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/predeterminada")
    public ResponseEntity<?> establecerPredeterminada(@PathVariable("id") Long id) {
        try {
            TarjetaModel tarjeta = this.tarjetaService.establecerPredeterminada(id);
            return ResponseEntity.ok(tarjeta);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTarjeta(@PathVariable("id") Long id) {
        try {
            Optional<TarjetaModel> tarjeta = this.tarjetaService.obtenerPorId(id);
            if (tarjeta.isPresent()) {
                return ResponseEntity.ok(tarjeta.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Clase interna para recibir el request
    public static class TarjetaRequest {
        private UsuarioId usuario;
        private String numeroEnmascarado;
        private String tipo;
        private String nombreTitular;
        private Integer mesExpiracion;
        private Integer anioExpiracion;
        private Boolean predeterminada;

        public UsuarioId getUsuario() {
            return usuario;
        }

        public void setUsuario(UsuarioId usuario) {
            this.usuario = usuario;
        }

        public String getNumeroEnmascarado() {
            return numeroEnmascarado;
        }

        public void setNumeroEnmascarado(String numeroEnmascarado) {
            this.numeroEnmascarado = numeroEnmascarado;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getNombreTitular() {
            return nombreTitular;
        }

        public void setNombreTitular(String nombreTitular) {
            this.nombreTitular = nombreTitular;
        }

        public Integer getMesExpiracion() {
            return mesExpiracion;
        }

        public void setMesExpiracion(Integer mesExpiracion) {
            this.mesExpiracion = mesExpiracion;
        }

        public Integer getAnioExpiracion() {
            return anioExpiracion;
        }

        public void setAnioExpiracion(Integer anioExpiracion) {
            this.anioExpiracion = anioExpiracion;
        }

        public Boolean getPredeterminada() {
            return predeterminada;
        }

        public void setPredeterminada(Boolean predeterminada) {
            this.predeterminada = predeterminada;
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