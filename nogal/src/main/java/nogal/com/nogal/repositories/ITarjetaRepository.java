package nogal.com.nogal.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nogal.com.nogal.models.TarjetaModel;

@Repository
public interface ITarjetaRepository extends JpaRepository<TarjetaModel, Long> {

    // Buscar tarjetas por usuario
    ArrayList<TarjetaModel> findByUsuarioId(Long usuarioId);

    // Buscar tarjeta predeterminada del usuario
    Optional<TarjetaModel> findByUsuarioIdAndPredeterminadaTrue(Long usuarioId);

    // Contar tarjetas del usuario
    Long countByUsuarioId(Long usuarioId);

    // Quitar predeterminada de todas las tarjetas del usuario
    @Modifying
    @Transactional
    @Query("UPDATE TarjetaModel t SET t.predeterminada = false WHERE t.usuario.id = :usuarioId")
    void quitarPredeterminadaDeTodas(@Param("usuarioId") Long usuarioId);
}