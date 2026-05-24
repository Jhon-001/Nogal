package nogal.com.nogal.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nogal.com.nogal.models.ProveedorModel;

@Repository
public interface IProveedorRepository extends JpaRepository<ProveedorModel, Long> {

    // Buscar proveedores activos
    ArrayList<ProveedorModel> findByActivoTrue();

    // Buscar por nombre
    ArrayList<ProveedorModel> findByNombreContaining(String nombre);

    // Buscar por material de especialidad
    ArrayList<ProveedorModel> findByMaterialEspecialidadContaining(String material);

    // Verificar si existe un proveedor con el mismo RUC
    boolean existsByRuc(String ruc);

    // Buscar por RUC
    Optional<ProveedorModel> findByRuc(String ruc);

    // Buscar proveedores activos por material
    @Query("SELECT p FROM ProveedorModel p WHERE p.activo = true AND p.materialEspecialidad LIKE %:material%")
    ArrayList<ProveedorModel> findActivosByMaterial(@Param("material") String material);
}