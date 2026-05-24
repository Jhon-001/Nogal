package nogal.com.nogal.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nogal.com.nogal.models.DireccionModel;
import nogal.com.nogal.models.UsuarioModel;
import nogal.com.nogal.repositories.IDireccionRepository;
import nogal.com.nogal.repositories.IUsuarioRepository;

@Service
public class DireccionService {

    @Autowired
    private IDireccionRepository direccionRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^9[0-9]{8}$");

    // Obtener todas las direcciones de un usuario
    public ArrayList<DireccionModel> obtenerDireccionesPorUsuario(Long usuarioId) {
        return direccionRepository.findByUsuarioIdAndActivaTrue(usuarioId);
    }

    // Obtener dirección principal del usuario
    public Optional<DireccionModel> obtenerDireccionPrincipal(Long usuarioId) {
        return direccionRepository.findDireccionPrincipalByUsuarioId(usuarioId);
    }

    // Obtener dirección por ID
    public Optional<DireccionModel> obtenerPorId(Long id) {
        return direccionRepository.findById(id);
    }

    // Crear nueva dirección
    @Transactional
    public DireccionModel crearDireccion(DireccionModel direccion) {
        System.out.println("📍 Creando nueva dirección para usuario ID: " + direccion.getUsuario().getId());

        // Validar usuario
        UsuarioModel usuario = usuarioRepository.findById(direccion.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        direccion.setUsuario(usuario);

        // Validar campos obligatorios
        validarDireccion(direccion);

        // Si es la primera dirección del usuario, marcarla como principal
        Long cantidadDirecciones = direccionRepository.countByUsuarioIdAndActivaTrue(usuario.getId());
        if (cantidadDirecciones == 0) {
            direccion.setEsPrincipal(true);
            System.out.println("✅ Primera dirección del usuario, marcada como principal");
        } else if (direccion.getEsPrincipal()) {
            // Si se marca como principal, desmarcar las demás
            direccionRepository.desmarcarTodasComoPrincipal(usuario.getId());
            System.out.println("✅ Desmarcando otras direcciones como principal");
        }

        DireccionModel direccionGuardada = direccionRepository.save(direccion);
        System.out.println("✅ Dirección creada con ID: " + direccionGuardada.getId());
        return direccionGuardada;
    }

    // Actualizar dirección existente
    @Transactional
    public DireccionModel actualizarDireccion(Long id, DireccionModel direccion) {
        System.out.println("📝 Actualizando dirección ID: " + id);

        DireccionModel direccionExistente = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // Validar campos
        validarDireccion(direccion);

        // Actualizar campos
        direccionExistente.setNombreDireccion(direccion.getNombreDireccion());
        direccionExistente.setNombreReceptor(direccion.getNombreReceptor());
        direccionExistente.setApellidosReceptor(direccion.getApellidosReceptor());
        direccionExistente.setDireccion(direccion.getDireccion());
        direccionExistente.setNumero(direccion.getNumero());
        direccionExistente.setDepartamento(direccion.getDepartamento());
        direccionExistente.setProvincia(direccion.getProvincia());
        direccionExistente.setDistrito(direccion.getDistrito());
        direccionExistente.setDptoOficinaCasa(direccion.getDptoOficinaCasa());
        direccionExistente.setTelefono(direccion.getTelefono());

        // Si se marca como principal, desmarcar las demás
        if (direccion.getEsPrincipal() && !direccionExistente.getEsPrincipal()) {
            direccionRepository.desmarcarTodasComoPrincipal(direccionExistente.getUsuario().getId());
            direccionExistente.setEsPrincipal(true);
            System.out.println("✅ Dirección marcada como principal");
        }

        return direccionRepository.save(direccionExistente);
    }

    // Eliminar dirección (soft delete)
    @Transactional
    public void eliminarDireccion(Long id) {
        System.out.println("🗑️ Eliminando dirección ID: " + id);

        DireccionModel direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // Si es la dirección principal, marcar otra como principal
        if (direccion.getEsPrincipal()) {
            direccion.setActiva(false);
            direccion.setEsPrincipal(false);
            direccionRepository.save(direccion);

            // Buscar otra dirección para marcarla como principal
            ArrayList<DireccionModel> direcciones = direccionRepository
                    .findByUsuarioIdAndActivaTrue(direccion.getUsuario().getId());
            
            if (!direcciones.isEmpty()) {
                DireccionModel nuevaPrincipal = direcciones.get(0);
                nuevaPrincipal.setEsPrincipal(true);
                direccionRepository.save(nuevaPrincipal);
                System.out.println("✅ Nueva dirección principal: ID " + nuevaPrincipal.getId());
            }
        } else {
            direccion.setActiva(false);
            direccionRepository.save(direccion);
        }

        System.out.println("✅ Dirección eliminada correctamente");
    }

    // Marcar dirección como principal
    @Transactional
    public DireccionModel marcarComoPrincipal(Long id) {
        System.out.println("⭐ Marcando dirección ID: " + id + " como principal");

        DireccionModel direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // Desmarcar todas las direcciones del usuario
        direccionRepository.desmarcarTodasComoPrincipal(direccion.getUsuario().getId());

        // Marcar esta como principal
        direccion.setEsPrincipal(true);
        return direccionRepository.save(direccion);
    }

    // Validaciones
    private void validarDireccion(DireccionModel direccion) {
        if (direccion.getNombreDireccion() == null || direccion.getNombreDireccion().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la dirección es obligatorio");
        }

        if (direccion.getNombreReceptor() == null || direccion.getNombreReceptor().trim().isEmpty()) {
            throw new RuntimeException("El nombre del receptor es obligatorio");
        }

        if (direccion.getApellidosReceptor() == null || direccion.getApellidosReceptor().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos del receptor son obligatorios");
        }

        if (direccion.getDireccion() == null || direccion.getDireccion().trim().isEmpty()) {
            throw new RuntimeException("La dirección es obligatoria");
        }

        if (direccion.getNumero() == null || direccion.getNumero().trim().isEmpty()) {
            throw new RuntimeException("El número es obligatorio");
        }

        if (direccion.getTelefono() == null || !TELEFONO_PATTERN.matcher(direccion.getTelefono()).matches()) {
            throw new RuntimeException("El teléfono debe tener 9 dígitos y empezar con 9");
        }
    }
}