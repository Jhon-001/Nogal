package nogal.com.nogal.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nogal.com.nogal.models.DetalleIngresoModel;
import nogal.com.nogal.models.IngresoInventarioModel;
import nogal.com.nogal.models.ProductoModel;
import nogal.com.nogal.repositories.IIngresoInventarioRepository;
import nogal.com.nogal.repositories.IProductoRepository;
import nogal.com.nogal.repositories.IProveedorRepository;

@Service
public class IngresoInventarioService {

    @Autowired
    private IIngresoInventarioRepository ingresoRepository;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private IProveedorRepository proveedorRepository;

    // Obtener todos los ingresos
    public ArrayList<IngresoInventarioModel> obtenerTodosIngresos() {
        return (ArrayList<IngresoInventarioModel>) ingresoRepository.findAll();
    }

    // Guardar nuevo ingreso con detalles - CORREGIDO
    @Transactional
    public IngresoInventarioModel guardarIngreso(IngresoInventarioModel ingreso) {
        // Validar que el proveedor existe
        if (!proveedorRepository.existsById(ingreso.getProveedor().getId())) {
            throw new RuntimeException("El proveedor no existe");
        }
        System.out.println("Proveedor validado: " + ingreso.getProveedor().getId());

        // Calcular totales
        calcularTotales(ingreso);

        // Si es contado, fecha de pago es la misma que fecha de emisión
        if ("contado".equals(ingreso.getMetodoPago())) {
            ingreso.setFechaPago(ingreso.getFechaEmision());
        }

        // Establecer la relación bidireccional para cada detalle
        for (DetalleIngresoModel detalle : ingreso.getDetalles()) {
            detalle.setIngreso(ingreso);

            // Validar que el producto existe (si tiene ID) o crear nuevo producto
            if (detalle.getProducto().getId() != null && detalle.getProducto().getId() > 0) {
                // Producto existente - validar que existe
                if (!productoRepository.existsById(detalle.getProducto().getId())) {
                    throw new RuntimeException("El producto con ID " + detalle.getProducto().getId() + " no existe");
                }
            } else {
                // Nuevo producto - guardarlo primero
                ProductoModel nuevoProducto = detalle.getProducto();
                nuevoProducto.setProveedor(ingreso.getProveedor());
                ProductoModel productoGuardado = productoRepository.save(nuevoProducto);
                detalle.setProducto(productoGuardado);
            }
        }

        // Guardar el ingreso (esto guarda también los detalles por cascade)
        IngresoInventarioModel ingresoGuardado = ingresoRepository.save(ingreso);

        // Actualizar stock de productos
        for (DetalleIngresoModel detalle : ingreso.getDetalles()) {
            productoRepository.actualizarStock(detalle.getProducto().getId(), detalle.getCantidad());
        }

        return ingresoGuardado;
    }

    // Obtener ingreso por ID
    public Optional<IngresoInventarioModel> obtenerPorId(Long id) {
        return ingresoRepository.findById(id);
    }

    // Calcular totales del ingreso
    private void calcularTotales(IngresoInventarioModel ingreso) {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetalleIngresoModel detalle : ingreso.getDetalles()) {
            // Asegurarnos de que el subtotal esté calculado
            if (detalle.getSubtotal() == null) {
                detalle.calcularSubtotal();
            }
            subtotal = subtotal.add(detalle.getSubtotal());
        }

        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = subtotal.add(igv);

        ingreso.setSubtotal(subtotal);
        ingreso.setIgv(igv);
        ingreso.setTotal(total);
    }

    // Buscar ingresos por proveedor
    public ArrayList<IngresoInventarioModel> buscarPorProveedor(Long proveedorId) {
        return ingresoRepository.findByProveedorId(proveedorId);
    }

    // Buscar ingresos por rango de fechas
    public ArrayList<IngresoInventarioModel> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ingresoRepository.findByFechaIngresoBetween(fechaInicio, fechaFin);
    }

    // Buscar ingresos por número de factura
    public ArrayList<IngresoInventarioModel> buscarPorNumeroFactura(String numeroFactura) {
        return ingresoRepository.findByNumeroFacturaContaining(numeroFactura);
    }

    // Obtener ingresos pendientes de pago
    public ArrayList<IngresoInventarioModel> obtenerIngresosPendientesPago() {
        return ingresoRepository.findIngresosPendientesPago();
    }

    // Obtener total de ingresos por mes
    public Double obtenerTotalIngresosPorMes(int anio, int mes) {
        return ingresoRepository.findTotalIngresosPorMes(anio, mes);
    }
}