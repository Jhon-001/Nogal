package nogal.com.nogal.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ NUEVO CAMPO
    
    
    @ManyToOne
    

    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;


    @Column(nullable = false, unique = true, length = 50)
    private String numeroPedido;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal igv;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal envio;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(nullable = false, length = 50)
    private String estado = "PENDIENTE"; // PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO, CANCELADO

    @ManyToOne
    @JoinColumn(name = "direccion_id", nullable = false)
    private DireccionModel direccion;

    @ManyToOne
    @JoinColumn(name = "tarjeta_id", nullable = false)
    private TarjetaModel tarjeta;

    @Column(nullable = false, length = 50)
    private String metodoPago; // TARJETA, YAPE, PLIN, CONTRAENTREGA

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "codigo_verificacion", length = 6)
    private String codigoVerificacion;


    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedidoModel> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaPedido = LocalDateTime.now();
    }

    // Constructores
    public PedidoModel() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public String getCodigoVerificacion() {
    return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
    this.codigoVerificacion = codigoVerificacion;
    }
    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getEnvio() {
        return envio;
    }

    public void setEnvio(BigDecimal envio) {
        this.envio = envio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public DireccionModel getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionModel direccion) {
        this.direccion = direccion;
    }

    public TarjetaModel getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(TarjetaModel tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public List<DetallePedidoModel> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoModel> detalles) {
        this.detalles = detalles;
    }

    // Método helper para agregar detalles
    public void agregarDetalle(DetallePedidoModel detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }
}