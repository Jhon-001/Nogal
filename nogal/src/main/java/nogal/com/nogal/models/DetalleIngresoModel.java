package nogal.com.nogal.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "detalle_ingreso")
public class DetalleIngresoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingreso_id", nullable = false)
    @JsonIgnore // ← AÑADIR ESTO
    private IngresoInventarioModel ingreso;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoModel producto;

    private Integer cantidad;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Constructores
    public DetalleIngresoModel() {
    }

    public DetalleIngresoModel(ProductoModel producto, Integer cantidad, BigDecimal precioCompra) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.subtotal = precioCompra.multiply(BigDecimal.valueOf(cantidad));
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IngresoInventarioModel getIngreso() {
        return ingreso;
    }

    public void setIngreso(IngresoInventarioModel ingreso) {
        this.ingreso = ingreso;
    }

    public ProductoModel getProducto() {
        return producto;
    }

    public void setProducto(ProductoModel producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    // Método público para calcular subtotal
    public void calcularSubtotal() {
        if (this.precioCompra != null && this.cantidad != null) {
            this.subtotal = this.precioCompra.multiply(BigDecimal.valueOf(this.cantidad));
        }
    }
}