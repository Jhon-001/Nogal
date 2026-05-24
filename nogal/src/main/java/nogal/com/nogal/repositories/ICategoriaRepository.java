package nogal.com.nogal.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nogal.com.nogal.models.CategoriaModel;

@Repository
public interface ICategoriaRepository extends JpaRepository<CategoriaModel, Long> {

    // Buscar por nombre
    Optional<CategoriaModel> findByNombre(String nombre);

    // Buscar categorías que contengan el nombre
    ArrayList<CategoriaModel> findByNombreContaining(String nombre);
}