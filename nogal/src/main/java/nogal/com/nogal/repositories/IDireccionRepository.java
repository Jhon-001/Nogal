package nogal.com.nogal.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nogal.com.nogal.models.DireccionModel;

@Repository
public interface IDireccionRepository extends JpaRepository<DireccionModel, Long> {

    // Buscar direcciones por usuario
    @Query("SELECT d FROM DireccionModel d WHERE d.usuario.id = :usuarioId AND d.activa = true ORDER BY d.esPrincipal DESC, d.fechaCreacion DESC")
    ArrayList<DireccionModel> findByUsuarioIdAndActivaTrue(@Param("usuarioId") Long usuarioId);

    // Buscar dirección principal del usuario
    @Query("SELECT d FROM DireccionModel d WHERE d.usuario.id = :usuarioId AND d.esPrincipal = true AND d.activa = true")
    Optional<DireccionModel> findDireccionPrincipalByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Contar direcciones activas de un usuario
    @Query("SELECT COUNT(d) FROM DireccionModel d WHERE d.usuario.id = :usuarioId AND d.activa = true")
    Long countByUsuarioIdAndActivaTrue(@Param("usuarioId") Long usuarioId);

    // Desmarcar todas las direcciones como principal para un usuario
    @Modifying
    @Transactional
    @Query("UPDATE DireccionModel d SET d.esPrincipal = false WHERE d.usuario.id = :usuarioId")
    void desmarcarTodasComoPrincipal(@Param("usuarioId") Long usuarioId);

    // Verificar si existe dirección principal para usuario
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DireccionModel d WHERE d.usuario.id = :usuarioId AND d.esPrincipal = true AND d.activa = true")
    Boolean existeDireccionPrincipal(@Param("usuarioId") Long usuarioId);
}