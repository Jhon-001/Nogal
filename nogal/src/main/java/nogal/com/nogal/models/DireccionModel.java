package nogal.com.nogal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "direccion")
public class DireccionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioModel usuario;

    @Column(nullable = false, length = 100)
    private String nombreDireccion;

    @Column(nullable = false, length = 100)
    private String nombreReceptor;

    @Column(nullable = false, length = 100)
    private String apellidosReceptor;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String numero;

    private String departamento;
    private String provincia;
    private String distrito;

    @Column(length = 50)
    private String dptoOficinaCasa;

    @Column(nullable = false, length = 9)
    private String telefono;

    @Column(nullable = false)
    private Boolean esPrincipal = false;

    @Column(nullable = false)
    private Boolean activa = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
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

    // Constructores
    public DireccionModel() {}

    public DireccionModel(UsuarioModel usuario, String nombreDireccion, String nombreReceptor,
                         String apellidosReceptor, String direccion, String numero, String telefono) {
        this.usuario = usuario;
        this.nombreDireccion = nombreDireccion;
        this.nombreReceptor = nombreReceptor;
        this.apellidosReceptor = apellidosReceptor;
        this.direccion = direccion;
        this.numero = numero;
        this.telefono = telefono;
    }

    // Getters y Setters (igual que antes)
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

    public String getNombreDireccion() {
        return nombreDireccion;
    }

    public void setNombreDireccion(String nombreDireccion) {
        this.nombreDireccion = nombreDireccion;
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public void setNombreReceptor(String nombreReceptor) {
        this.nombreReceptor = nombreReceptor;
    }

    public String getApellidosReceptor() {
        return apellidosReceptor;
    }

    public void setApellidosReceptor(String apellidosReceptor) {
        this.apellidosReceptor = apellidosReceptor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDptoOficinaCasa() {
        return dptoOficinaCasa;
    }

    public void setDptoOficinaCasa(String dptoOficinaCasa) {
        this.dptoOficinaCasa = dptoOficinaCasa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getDireccionCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(direccion).append(" ").append(numero);
        if (dptoOficinaCasa != null && !dptoOficinaCasa.isEmpty()) {
            sb.append(", ").append(dptoOficinaCasa);
        }
        if (distrito != null && !distrito.isEmpty()) {
            sb.append(", ").append(distrito);
        }
        if (provincia != null && !provincia.isEmpty()) {
            sb.append(", ").append(provincia);
        }
        if (departamento != null && !departamento.isEmpty()) {
            sb.append(", ").append(departamento);
        }
        return sb.toString();
    }
}