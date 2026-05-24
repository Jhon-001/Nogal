package nogal.com.nogal.models;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedor")
public class ProveedorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true)
    private String ruc;

    private String telefono;
    private String email;
    private String direccion;
    private String contacto;
    private String materialEspecialidad;
    private boolean activo = true;

    // Constructores
    public ProveedorModel() {
    }

    public ProveedorModel(String nombre, String materialEspecialidad) {
        this.nombre = nombre;
        this.materialEspecialidad = materialEspecialidad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getMaterialEspecialidad() {
        return materialEspecialidad;
    }

    public void setMaterialEspecialidad(String materialEspecialidad) {
        this.materialEspecialidad = materialEspecialidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}