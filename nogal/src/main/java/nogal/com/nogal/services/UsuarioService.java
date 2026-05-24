package nogal.com.nogal.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nogal.com.nogal.models.CambioPasswordRequest;
import nogal.com.nogal.models.UsuarioModel;
import nogal.com.nogal.repositories.IUsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Patrones de validación
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^9[0-9]{8}$");
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}$");
    private static final Pattern CE_PATTERN = Pattern.compile("^[0-9]{9,12}$");

    // Obtener todos los usuarios
    public ArrayList<UsuarioModel> listaUsuarios() {
        return (ArrayList<UsuarioModel>) usuarioRepository.findAll();
    }
    
    public Map<String, Object> obtenerEstadisticasUsuarios() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Obtener todos los usuarios
        ArrayList<UsuarioModel> todosUsuarios = (ArrayList<UsuarioModel>) usuarioRepository.findAll();
        
        // Total de usuarios
        estadisticas.put("totalUsuarios", todosUsuarios.size());
        
        // Contar por rol
        long totalClientes = todosUsuarios.stream().filter(u -> "cliente".equals(u.getRol())).count();
        long totalRepartidores = todosUsuarios.stream().filter(u -> "repartidor".equals(u.getRol())).count();
        long totalLogisticos = todosUsuarios.stream().filter(u -> "logistico".equals(u.getRol())).count();
        long totalAdmins = todosUsuarios.stream().filter(u -> "admin".equals(u.getRol())).count();
        
        estadisticas.put("totalClientes", totalClientes);
        estadisticas.put("totalRepartidores", totalRepartidores);
        estadisticas.put("totalLogisticos", totalLogisticos);
        estadisticas.put("totalAdmins", totalAdmins);
        
        // Clientes nuevos este mes
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        long clientesNuevos = todosUsuarios.stream()
            .filter(u -> "cliente".equals(u.getRol()))
            .count();
        
        estadisticas.put("clientesNuevosEsteMes", clientesNuevos);
        
        return estadisticas;
    }

    // Validaciones
    private void validarUsuario(UsuarioModel usuario) {
        // Validar email
        if (!EMAIL_PATTERN.matcher(usuario.getEmail()).matches()) {
            throw new RuntimeException("El email debe tener un formato válido (ejemplo: usuario@dominio.com)");
        }

        // Validar teléfono (9 dígitos, empezando con 9)
        if (!TELEFONO_PATTERN.matcher(usuario.getTelefono()).matches()) {
            throw new RuntimeException("El teléfono debe tener 9 dígitos y empezar con 9");
        }

        // Validar documento según el tipo
        if ("DNI".equals(usuario.getTipoDocumento())) {
            if (!DNI_PATTERN.matcher(usuario.getNumeroDocumento()).matches()) {
                throw new RuntimeException("El DNI debe tener exactamente 8 dígitos numéricos");
            }
        } else if ("CE".equals(usuario.getTipoDocumento())) {
            if (!CE_PATTERN.matcher(usuario.getNumeroDocumento()).matches()) {
                throw new RuntimeException("El Carnet de Extranjería debe tener entre 9 y 12 dígitos numéricos");
            }
        } else {
            throw new RuntimeException("Tipo de documento no válido. Use 'DNI' o 'CE'");
        }
    }

    // Crear nuevo usuario (siempre como "cliente" por defecto)
    public UsuarioModel crearUsuario(UsuarioModel usuario) {
        System.out.println("=== INICIANDO REGISTRO ===");
        System.out.println("Usuario: " + usuario.getUsername());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Teléfono: " + usuario.getTelefono());
        System.out.println("Documento: " + usuario.getTipoDocumento() + " - " + usuario.getNumeroDocumento());

        try {
            // Validaciones de formato
            validarUsuario(usuario);
            System.out.println("✅ Validaciones pasadas");

            // Verificar si el username ya existe
            if (usuarioRepository.existsByUsername(usuario.getUsername())) {
                System.out.println("❌ Username ya existe");
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }

            // Verificar si el email ya existe
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                System.out.println("❌ Email ya existe");
                throw new RuntimeException("El email ya está registrado");
            }

            // Verificar si el número de documento ya existe
            if (usuarioRepository.existsByNumeroDocumento(usuario.getNumeroDocumento())) {
                System.out.println("❌ Documento ya existe");
                throw new RuntimeException("El número de documento ya está registrado");
            }

            // ✅ SIEMPRE asignar "cliente" para registro público
            usuario.setRol("cliente");
            
            // ✅ ENCRIPTAR CONTRASEÑA ANTES DE GUARDAR
            String contrasenaEncriptada = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(contrasenaEncriptada);
            System.out.println("✅ Contraseña encriptada");
            System.out.println("✅ Rol asignado: cliente");

            UsuarioModel usuarioGuardado = usuarioRepository.save(usuario);
            System.out.println("✅ Usuario guardado ID: " + usuarioGuardado.getId());

            return usuarioGuardado;

        } catch (Exception e) {
            System.out.println("❌ ERROR en crearUsuario: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Login de usuario CON COMPATIBILIDAD HACIA ATRÁS
    public UsuarioModel login(String username, String password) {
        System.out.println("=== INTENTO DE LOGIN ===");
        System.out.println("Username: " + username);

        // Buscar usuario por username
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            String passwordAlmacenado = usuario.getPassword();
            
            System.out.println("Usuario encontrado: " + usuario.getUsername() + " | Rol: " + usuario.getRol());
            
            // CASO 1: Contraseña ya encriptada con BCrypt
            if (passwordAlmacenado.startsWith("$2a$") || passwordAlmacenado.startsWith("$2b$")) {
                System.out.println("📝 Contraseña almacenada como BCrypt");
                if (passwordEncoder.matches(password, passwordAlmacenado)) {
                    System.out.println("✅ Login exitoso (BCrypt) - Rol: " + usuario.getRol());
                    return usuario;
                } else {
                    System.out.println("❌ Contraseña BCrypt incorrecta");
                    throw new RuntimeException("Credenciales incorrectas");
                }
            } 
            // CASO 2: Contraseña en texto plano (usuarios antiguos)
            else {
                System.out.println("📝 Contraseña almacenada como texto plano");
                if (passwordAlmacenado.equals(password)) {
                    System.out.println("✅ Login exitoso (texto plano) - Rol: " + usuario.getRol());
                    
                    // MIGRAR AUTOMÁTICAMENTE A BCRYPT
                    String passwordEncriptado = passwordEncoder.encode(password);
                    usuario.setPassword(passwordEncriptado);
                    usuarioRepository.save(usuario);
                    System.out.println("🔄 Contraseña migrada a BCrypt para: " + usuario.getUsername());
                    
                    return usuario;
                } else {
                    System.out.println("❌ Contraseña texto plano incorrecta");
                    throw new RuntimeException("Credenciales incorrectas");
                }
            }
        } else {
            System.out.println("❌ Usuario no encontrado");
            throw new RuntimeException("Credenciales incorrectas");
        }
    }

    // Método para migrar TODOS los usuarios existentes
    public Map<String, Object> migrarUsuariosExistentes() {
        System.out.println("=== INICIANDO MIGRACIÓN DE USUARIOS EXISTENTES ===");
        
        List<UsuarioModel> todosUsuarios = usuarioRepository.findAll();
        int migrados = 0;
        int yaEncriptados = 0;
        int errores = 0;
        List<String> detalles = new ArrayList<>();
        
        for (UsuarioModel usuario : todosUsuarios) {
            try {
                // Verificar si la contraseña ya está encriptada
                if (usuario.getPassword().startsWith("$2a$") || usuario.getPassword().startsWith("$2b$")) {
                    yaEncriptados++;
                    detalles.add("ℹ Ya encriptado: " + usuario.getUsername() + " - Rol: " + usuario.getRol());
                } else {
                    // Contraseña en texto plano → encriptarla
                    String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
                    usuario.setPassword(passwordEncriptada);
                    usuarioRepository.save(usuario);
                    migrados++;
                    detalles.add("✅ Migrado: " + usuario.getUsername() + " - Rol: " + usuario.getRol());
                    System.out.println("✅ Migrado: " + usuario.getUsername() + " - Rol: " + usuario.getRol());
                }
            } catch (Exception e) {
                errores++;
                detalles.add("❌ Error: " + usuario.getUsername() + " - " + e.getMessage());
                System.out.println("❌ Error migrando: " + usuario.getUsername() + " - " + e.getMessage());
            }
        }
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalUsuarios", todosUsuarios.size());
        resultado.put("migrados", migrados);
        resultado.put("yaEncriptados", yaEncriptados);
        resultado.put("errores", errores);
        resultado.put("detalles", detalles);
        resultado.put("mensaje", "Migración completada");
        
        System.out.println("=== MIGRACIÓN COMPLETADA ===");
        System.out.println("Total usuarios: " + todosUsuarios.size());
        System.out.println("Migrados: " + migrados);
        System.out.println("Ya encriptados: " + yaEncriptados);
        System.out.println("Errores: " + errores);
        
        return resultado;
    }

    // Buscar usuario por ID
    public Optional<UsuarioModel> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar usuario por username
    public Optional<UsuarioModel> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Buscar usuario por número de documento
    public Optional<UsuarioModel> buscarPorNumeroDocumento(String numeroDocumento) {
        return usuarioRepository.findByNumeroDocumento(numeroDocumento);
    }

    // Actualizar usuario
    public UsuarioModel actualizarUsuario(UsuarioModel usuario, Long id) {
        UsuarioModel usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!usuarioExistente.getUsername().equals(usuario.getUsername())) {
            if (usuarioRepository.existsByUsername(usuario.getUsername())) {
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
            usuarioExistente.setUsername(usuario.getUsername());
        }
        
        // Validaciones de formato si se cambian estos campos
        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            if (!EMAIL_PATTERN.matcher(usuario.getEmail()).matches()) {
                throw new RuntimeException("El email debe tener un formato válido");
            }
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }
        }

        if (!usuarioExistente.getTelefono().equals(usuario.getTelefono())) {
            if (!TELEFONO_PATTERN.matcher(usuario.getTelefono()).matches()) {
                throw new RuntimeException("El teléfono debe tener 9 dígitos y empezar con 9");
            }
        }

        // Actualizar campos permitidos
        usuarioExistente.setNombres(usuario.getNombres());
        usuarioExistente.setApellidos(usuario.getApellidos());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setTelefono(usuario.getTelefono());

        return usuarioRepository.save(usuarioExistente);
    }

    // Eliminar usuario
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Buscar usuarios por rol (para admin)
    public ArrayList<UsuarioModel> buscarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    // Método especial para crear usuarios administrativos (solo para desarrollo)
    public UsuarioModel crearUsuarioAdmin(UsuarioModel usuario) {
        System.out.println("=== CREANDO USUARIO ADMIN/LOGISTICO ===");

        // Validaciones de formato
        validarUsuario(usuario);

        // Verificar si el username ya existe
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Verificar si el número de documento ya existe
        if (usuarioRepository.existsByNumeroDocumento(usuario.getNumeroDocumento())) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        // Validar que tenga un rol administrativo
        if (!"admin".equals(usuario.getRol()) && !"logistico".equals(usuario.getRol()) && !"repartidor".equals(usuario.getRol())) {
            throw new RuntimeException("Este método solo permite crear usuarios de personal interno (admin, logistico o repartidor)");
        }

        // ✅ ENCRIPTAR CONTRASEÑA ANTES DE GUARDAR
        String contrasenaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contrasenaEncriptada);
        System.out.println("✅ Contraseña encriptada");
        System.out.println("✅ Usuario administrativo creado - Rol: " + usuario.getRol());
        
        return usuarioRepository.save(usuario);
    }
    
    public void cambiarPassword(Long usuarioId, CambioPasswordRequest request) {
        System.out.println("🔍 Buscando usuario con ID: " + usuarioId);
        
        // Buscar usuario
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> {
                System.out.println("❌ Usuario no encontrado con ID: " + usuarioId);
                return new RuntimeException("Usuario no encontrado");
            });
        
        System.out.println("✅ Usuario encontrado: " + usuario.getUsername());
        System.out.println("🔐 Verificando contraseña actual...");
        
        // Verificar contraseña actual usando passwordEncoder
        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            System.out.println("❌ Contraseña actual incorrecta");
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        System.out.println("✅ Contraseña actual verificada correctamente");
        
        // Validar que la nueva contraseña sea diferente
        if (passwordEncoder.matches(request.getNuevaPassword(), usuario.getPassword())) {
            System.out.println("❌ La nueva contraseña debe ser diferente a la actual");
            throw new RuntimeException("La nueva contraseña debe ser diferente a la actual");
        }
        
        // Validar longitud mínima
        if (request.getNuevaPassword().length() < 6) {
            System.out.println("❌ La nueva contraseña debe tener al menos 6 caracteres");
            throw new RuntimeException("La nueva contraseña debe tener al menos 6 caracteres");
        }
        
        System.out.println("✅ Validaciones pasadas, actualizando contraseña...");
        
        // ✅ ENCRIPTAR NUEVA CONTRASEÑA
        String nuevaContrasenaEncriptada = passwordEncoder.encode(request.getNuevaPassword());
        usuario.setPassword(nuevaContrasenaEncriptada);
        
        UsuarioModel usuarioActualizado = usuarioRepository.save(usuario);
        
        System.out.println("✅ Contraseña actualizada exitosamente para usuario: " + usuarioActualizado.getUsername());
    }
}
