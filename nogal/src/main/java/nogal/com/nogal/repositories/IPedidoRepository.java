package nogal.com.nogal.repositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nogal.com.nogal.models.PedidoModel;

@Repository
public interface IPedidoRepository extends JpaRepository<PedidoModel, Long> {

    // Buscar pedidos por usuario
    @Query("SELECT p FROM PedidoModel p WHERE p.usuario.id = :usuarioId ORDER BY p.fechaPedido DESC")
    
    ArrayList<PedidoModel> findByUsuarioIdOrderByFechaPedidoDesc(@Param("usuarioId") Long usuarioId);
    @Query("SELECT DISTINCT p FROM PedidoModel p " +
       "LEFT JOIN FETCH p.usuario " +
       "LEFT JOIN FETCH p.direccion " +
       "LEFT JOIN FETCH p.tarjeta " +
       "LEFT JOIN FETCH p.detalles d " +
       "LEFT JOIN FETCH d.producto " +
       "WHERE p.estado = 'ENVIADO' " +
       "ORDER BY p.fechaPedido DESC")
ArrayList<PedidoModel> findPedidosEnviadosConRelaciones();

    // Buscar por número de pedido
    Optional<PedidoModel> findByNumeroPedido(String numeroPedido);

    // Buscar pedidos por estado
    @Query("SELECT p FROM PedidoModel p WHERE p.estado = 'ENVIADO'")
    ArrayList<PedidoModel> findByEstado(String estado);
    @Query("SELECT p FROM PedidoModel p WHERE p.fechaPedido BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fechaPedido DESC")
    ArrayList<PedidoModel> findByFechaPedidoBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                               @Param("fechaFin") LocalDateTime fechaFin);
    // Buscar pedidos por usuario y estado
    @Query("SELECT p FROM PedidoModel p WHERE p.usuario.id = :usuarioId AND p.estado = :estado ORDER BY p.fechaPedido DESC")
    ArrayList<PedidoModel> findByUsuarioIdAndEstado(@Param("usuarioId") Long usuarioId, @Param("estado") String estado);

    // Contar pedidos por usuario
    Long countByUsuarioId(Long usuarioId);

    // Verificar si existe número de pedido
    Boolean existsByNumeroPedido(String numeroPedido);

    // mis cambios para logistico
    // Obtener todos los pedidos ordenados por fecha
    ArrayList<PedidoModel> findAllByOrderByFechaPedidoDesc();
}