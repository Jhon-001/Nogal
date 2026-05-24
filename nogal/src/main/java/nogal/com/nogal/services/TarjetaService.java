package nogal.com.nogal.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nogal.com.nogal.models.TarjetaModel;
import nogal.com.nogal.models.UsuarioModel;
import nogal.com.nogal.repositories.ITarjetaRepository;
import nogal.com.nogal.repositories.IUsuarioRepository;

@Service
public class TarjetaService {

    @Autowired
    private ITarjetaRepository tarjetaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    // Obtener tarjetas del usuario
    public ArrayList<TarjetaModel> obtenerTarjetasPorUsuario(Long usuarioId) {
        return tarjetaRepository.findByUsuarioId(usuarioId);
    }

    // Obtener tarjeta por ID
    public Optional<TarjetaModel> obtenerPorId(Long id) {
        return tarjetaRepository.findById(id);
    }

    // Guardar nueva tarjeta
    public TarjetaModel guardarTarjeta(TarjetaModel tarjeta) {
        // Validar que el usuario existe
        if (!usuarioRepository.existsById(tarjeta.getUsuario().getId())) {
            throw new RuntimeException("El usuario no existe");
        }

        // Si es la primera tarjeta, establecer como predeterminada
        Long cantidadTarjetas = tarjetaRepository.countByUsuarioId(tarjeta.getUsuario().getId());
        if (cantidadTarjetas == 0) {
            tarjeta.setPredeterminada(true);
        }

        return tarjetaRepository.save(tarjeta);
    }

    // Actualizar tarjeta
    public TarjetaModel actualizarTarjeta(TarjetaModel tarjeta, Long id) {
        TarjetaModel tarjetaExistente = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        // Solo permitir actualizar campos editables
        tarjetaExistente.setNombreTitular(tarjeta.getNombreTitular());
        tarjetaExistente.setMesExpiracion(tarjeta.getMesExpiracion());
        tarjetaExistente.setAnioExpiracion(tarjeta.getAnioExpiracion());

        return tarjetaRepository.save(tarjetaExistente);
    }

    // Eliminar tarjeta
    public void eliminarTarjeta(Long id) {
        TarjetaModel tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        // Si es la tarjeta predeterminada, buscar otra para establecer como predeterminada
        if (tarjeta.getPredeterminada()) {
            ArrayList<TarjetaModel> otrasTarjetas = tarjetaRepository.findByUsuarioId(tarjeta.getUsuario().getId());
            otrasTarjetas.removeIf(t -> t.getId().equals(id));
            
            if (!otrasTarjetas.isEmpty()) {
                TarjetaModel nuevaPredeterminada = otrasTarjetas.get(0);
                nuevaPredeterminada.setPredeterminada(true);
                tarjetaRepository.save(nuevaPredeterminada);
            }
        }

        tarjetaRepository.deleteById(id);
    }

    // Establecer tarjeta predeterminada
    public TarjetaModel establecerPredeterminada(Long id) {
        TarjetaModel tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        // Quitar predeterminada de todas las tarjetas del usuario
        tarjetaRepository.quitarPredeterminadaDeTodas(tarjeta.getUsuario().getId());

        // Establecer esta tarjeta como predeterminada
        tarjeta.setPredeterminada(true);
        return tarjetaRepository.save(tarjeta);
    }
}