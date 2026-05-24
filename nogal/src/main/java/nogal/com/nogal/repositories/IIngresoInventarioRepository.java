package nogal.com.nogal.repositories;

import java.util.ArrayList;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nogal.com.nogal.models.IngresoInventarioModel;

@Repository
public interface IIngresoInventarioRepository extends JpaRepository<IngresoInventarioModel, Long> {

    // Buscar ingresos por proveedor
    ArrayList<IngresoInventarioModel> findByProveedorId(Long proveedorId);

    // Buscar ingresos por fecha de ingreso
    ArrayList<IngresoInventarioModel> findByFechaIngresoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar ingresos por número de factura
    ArrayList<IngresoInventarioModel> findByNumeroFacturaContaining(String numeroFactura);

    // Obtener el total de ingresos por mes
    @Query("SELECT SUM(i.total) FROM IngresoInventarioModel i WHERE YEAR(i.fechaIngreso) = :anio AND MONTH(i.fechaIngreso) = :mes")
    Double findTotalIngresosPorMes(@Param("anio") int anio, @Param("mes") int mes);

    // Buscar ingresos pendientes de pago (crédito)
    @Query("SELECT i FROM IngresoInventarioModel i WHERE i.metodoPago = 'credito' AND i.fechaPago > CURRENT_DATE")
    ArrayList<IngresoInventarioModel> findIngresosPendientesPago();
}