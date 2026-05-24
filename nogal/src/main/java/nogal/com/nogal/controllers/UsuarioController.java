package nogal.com.nogal.controllers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nogal.com.nogal.models.CambioPasswordRequest;
import nogal.com.nogal.models.UsuarioModel;
import nogal.com.nogal.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    // Clave secreta para migración (cámbiala por una más segura)
    private static final String CLAVE_MIGRACION = "MigracionSegura123";

    // Manejo de excepciones global
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        System.out.println("❌ Error controlado: " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        System.out.println("❌ Error interno: " + ex.getMessage());
        return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
    }

    @GetMapping
    public ArrayList<UsuarioModel> getUsuarios() {
        return this.usuarioService.listaUsuarios();
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<?> nuevoUsuario(@RequestBody UsuarioModel usuario) {
        try {
            System.out.println("📝 Recibiendo solicitud de registro para: " + usuario.getUsername());
            UsuarioModel usuarioCreado = this.usuarioService.crearUsuario(usuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (RuntimeException ex) {
            System.out.println("❌ Error en registro: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("❌ Error interno en registro: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @PostMapping(path = "/crear-admin")
    public ResponseEntity<?> nuevoUsuarioAdmin(@RequestBody UsuarioModel usuario) {
        try {
            System.out.println("📝 Recibiendo solicitud de usuario admin: " + usuario.getUsername());
            UsuarioModel usuarioCreado = this.usuarioService.crearUsuarioAdmin(usuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (RuntimeException ex) {
            System.out.println("❌ Error en registro admin: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("❌ Error interno en registro admin: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("🔐 Intentando login para: " + loginRequest.getUsername());
            UsuarioModel usuario = this.usuarioService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException ex) {
            System.out.println("❌ Error en login: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("❌ Error interno en login: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @GetMapping("/{id}")
    public Optional<UsuarioModel> obtenerUsuarioId(@PathVariable("id") Long id) {
        return this.usuarioService.buscarPorId(id);
    }

    @GetMapping("/username/{username}")
    public Optional<UsuarioModel> obtenerUsuarioUsername(@PathVariable("username") String username) {
        return this.usuarioService.buscarPorUsername(username);
    }

    @GetMapping("/documento/{documento}")
    public Optional<UsuarioModel> obtenerUsuarioPorDocumento(@PathVariable("documento") String documento) {
        return this.usuarioService.buscarPorNumeroDocumento(documento);
    }
    
    @GetMapping("/todos")
    public ResponseEntity<?> listaUsuarios() {
        try {
            System.out.println("👥 GET /usuario/todos - Obteniendo todos los usuarios");
            ArrayList<UsuarioModel> usuarios = usuarioService.listaUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo usuarios: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticasUsuarios() {
        try {
            System.out.println("📊 GET /usuario/estadisticas - Obteniendo estadísticas de usuarios");
            
            Map<String, Object> estadisticas = usuarioService.obtenerEstadisticasUsuarios();
            return ResponseEntity.ok(estadisticas);
            
        } catch (Exception ex) {
            System.err.println("❌ Error obteniendo estadísticas: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/rol/{rol}")
    public ArrayList<UsuarioModel> obtenerUsuariosPorRol(@PathVariable("rol") String rol) {
        return this.usuarioService.buscarPorRol(rol);
    }

    @PutMapping(path = "/{id}")
    public UsuarioModel actualizarUsuario(@RequestBody UsuarioModel usuario, @PathVariable Long id) {
        return this.usuarioService.actualizarUsuario(usuario, id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        this.usuarioService.eliminarUsuario(id);
        return "Usuario eliminado correctamente";
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> cambiarPassword(@PathVariable("id") Long id, 
                                            @RequestBody CambioPasswordRequest request) {
        try {
            System.out.println("🔐 Solicitando cambio de contraseña para usuario ID: " + id);
            
            this.usuarioService.cambiarPassword(id, request);
            
            System.out.println("✅ Contraseña cambiada exitosamente para usuario ID: " + id);
            return ResponseEntity.ok("Contraseña cambiada exitosamente");
            
        } catch (RuntimeException ex) {
            System.out.println("❌ Error cambiando contraseña: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("❌ Error interno cambiando contraseña: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }
    
    // 📌 ENDPOINT DE MIGRACIÓN (TEMPORAL - ELIMINAR DESPUÉS DE USAR)
    @PostMapping("/migrar-contrasenas")
    public ResponseEntity<?> migrarContrasenas(@RequestHeader("X-Migration-Key") String migrationKey) {
        try {
            System.out.println("🔄 Solicitando migración de contraseñas");
            System.out.println("🔑 Clave recibida: " + migrationKey);
            
            // Verificar clave de seguridad
            if (!CLAVE_MIGRACION.equals(migrationKey)) {
                System.out.println("❌ Clave de migración incorrecta");
                return ResponseEntity.status(401).body("Acceso no autorizado");
            }
            
            Map<String, Object> resultado = usuarioService.migrarUsuariosExistentes();
            
            System.out.println("✅ Migración completada exitosamente");
            return ResponseEntity.ok(resultado);
            
        } catch (Exception ex) {
            System.out.println("❌ Error durante la migración: " + ex.getMessage());
            return ResponseEntity.status(500).body("Error en migración: " + ex.getMessage());
        }
    }

    // Clase interna para el login
    public static class LoginRequest {
        private String username;
        private String password;

        // Getters y Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}

