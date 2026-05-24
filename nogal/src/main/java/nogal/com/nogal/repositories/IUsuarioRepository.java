package nogal.com.nogal.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nogal.com.nogal.models.UsuarioModel;

@Repository
public interface IUsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    // Buscar por username
    Optional<UsuarioModel> findByUsername(String username);

    // Buscar por email
    Optional<UsuarioModel> findByEmail(String email);

    // Buscar por número de documento
    Optional<UsuarioModel> findByNumeroDocumento(String numeroDocumento);

    // Verificar si existe un usuario por username, email o documento
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByNumeroDocumento(String numeroDocumento);

    // Buscar usuarios por rol
    ArrayList<UsuarioModel> findByRol(String rol);
    
    // Login: buscar por username y password
    @Query("SELECT u FROM UsuarioModel u WHERE u.username = :username AND u.password = :password")
    Optional<UsuarioModel> findByUsernameAndPassword(@Param("username") String username,
            @Param("password") String password);
}