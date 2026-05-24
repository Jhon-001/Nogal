package nogal.com.nogal.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nogal.com.nogal.models.PedidoModel;
import nogal.com.nogal.models.ReporteEntregaModel;

@Repository
public interface IReporteEntregaRepository extends JpaRepository<ReporteEntregaModel, Long> {

    ArrayList<ReporteEntregaModel> findByRepartidorId(Long repartidorId);
    Optional<ReporteEntregaModel> findByPedidoId(Long pedidoId);
    
    @Query("SELECT p FROM PedidoModel p WHERE p.estado = 'ENVIADO' ORDER BY p.fechaPedido DESC")
    ArrayList<PedidoModel> findPedidosParaRepartidor();

    boolean existsByPedidoId(Long pedidoId);

}
