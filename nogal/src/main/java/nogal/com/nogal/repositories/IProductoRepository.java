package nogal.com.nogal.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nogal.com.nogal.models.ProductoModel;

@Repository
public interface IProductoRepository extends JpaRepository<ProductoModel, Long> {

    // Buscar productos activos
    ArrayList<ProductoModel> findByActivoTrue();

    // Buscar por nombre
    ArrayList<ProductoModel> findByNombreContaining(String nombre);

    // Buscar por proveedor
    ArrayList<ProductoModel> findByProveedorId(Long proveedorId);

    // Buscar por categoría
    ArrayList<ProductoModel> findByCategoriaId(Long categoriaId);

    // Buscar productos con stock bajo (menos de 10 unidades)
    @Query("SELECT p FROM ProductoModel p WHERE p.stock < 10 AND p.activo = true")
    ArrayList<ProductoModel> findProductosConStockBajo();

    // Buscar productos por proveedor y categoría
    @Query("SELECT p FROM ProductoModel p WHERE p.proveedor.id = :proveedorId AND p.categoria.id = :categoriaId AND p.activo = true")
    ArrayList<ProductoModel> findByProveedorAndCategoria(@Param("proveedorId") Long proveedorId,
            @Param("categoriaId") Long categoriaId);

    // Buscar productos por material
    ArrayList<ProductoModel> findByMaterialContaining(String material);

    // Actualizar stock - CORREGIDO
    @Modifying
    @Transactional
    @Query("UPDATE ProductoModel p SET p.stock = p.stock + :cantidad WHERE p.id = :productoId")
    void actualizarStock(@Param("productoId") Long productoId, @Param("cantidad") Integer cantidad);
}