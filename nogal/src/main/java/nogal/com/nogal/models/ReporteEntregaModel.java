package nogal.com.nogal.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "reporte_entrega")
public class ReporteEntregaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoModel pedido;

    @ManyToOne
    @JoinColumn(name = "repartidor_id", nullable = false)
    private UsuarioModel repartidor;

    @Column(nullable = false)
    private String codigoVerificacion;

    @Column(columnDefinition = "TEXT")
    private String fotoUrl;

    @Column(columnDefinition = "TEXT")
    private String archivosAdjuntos; // NUEVO CAMPO AÑADIDO

    private String observaciones;

    @Column(nullable = false)
    private LocalDateTime fechaEntrega;

    @Column(nullable = false)
    private String estado;

    @PrePersist
    protected void onCreate() {
        fechaEntrega = LocalDateTime.now();
    }

    // Constructores
    public ReporteEntregaModel() {}

    public ReporteEntregaModel(PedidoModel pedido, UsuarioModel repartidor, String codigoVerificacion) {
        this.pedido = pedido;
        this.repartidor = repartidor;
        this.codigoVerificacion = codigoVerificacion;
        this.estado = "ENTREGADO";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PedidoModel getPedido() { return pedido; }
    public void setPedido(PedidoModel pedido) { this.pedido = pedido; }

    public UsuarioModel getRepartidor() { return repartidor; }
    public void setRepartidor(UsuarioModel repartidor) { this.repartidor = repartidor; }

    public String getCodigoVerificacion() { return codigoVerificacion; }
    public void setCodigoVerificacion(String codigoVerificacion) { this.codigoVerificacion = codigoVerificacion; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getArchivosAdjuntos() { return archivosAdjuntos; } // NUEVO GETTER
    public void setArchivosAdjuntos(String archivosAdjuntos) { this.archivosAdjuntos = archivosAdjuntos; } // NUEVO SETTER

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}