package nogal.com.nogal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarjeta")
public class TarjetaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioModel usuario;

    @Column(nullable = false)
    private String numeroEnmascarado;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String nombreTitular;

    @Column(nullable = false)
    private Integer mesExpiracion;

    @Column(nullable = false)
    private Integer anioExpiracion;

    @Column(nullable = false)
    private Boolean predeterminada = false;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Constructores, Getters y Setters
    public TarjetaModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public UsuarioModel getUsuario() { return usuario; }
    public void setUsuario(UsuarioModel usuario) { this.usuario = usuario; }
    
    public String getNumeroEnmascarado() { return numeroEnmascarado; }
    public void setNumeroEnmascarado(String numeroEnmascarado) { this.numeroEnmascarado = numeroEnmascarado; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }
    
    public Integer getMesExpiracion() { return mesExpiracion; }
    public void setMesExpiracion(Integer mesExpiracion) { this.mesExpiracion = mesExpiracion; }
    
    public Integer getAnioExpiracion() { return anioExpiracion; }
    public void setAnioExpiracion(Integer anioExpiracion) { this.anioExpiracion = anioExpiracion; }
    
    public Boolean getPredeterminada() { return predeterminada; }
    public void setPredeterminada(Boolean predeterminada) { this.predeterminada = predeterminada; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}