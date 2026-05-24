package nogal.com.nogal.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nogal.com.nogal.models.ProveedorModel;
import nogal.com.nogal.repositories.IProveedorRepository;

@Service
public class ProveedorService {

    @Autowired
    private IProveedorRepository proveedorRepository;

    // Obtener todos los proveedores activos
    public ArrayList<ProveedorModel> obtenerProveedoresActivos() {
        return proveedorRepository.findByActivoTrue();
    }

    // Obtener todos los proveedores
    public ArrayList<ProveedorModel> obtenerTodosProveedores() {
        return (ArrayList<ProveedorModel>) proveedorRepository.findAll();
    }

    // Guardar nuevo proveedor
    public ProveedorModel guardarProveedor(ProveedorModel proveedor) {
        // Validar RUC único si está presente
        if (proveedor.getRuc() != null && !proveedor.getRuc().isEmpty()) {
            if (proveedorRepository.existsByRuc(proveedor.getRuc())) {
                throw new RuntimeException("Ya existe un proveedor con el RUC: " + proveedor.getRuc());
            }
        }
        return proveedorRepository.save(proveedor);
    }

    // Obtener proveedor por ID
    public Optional<ProveedorModel> obtenerPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    // Actualizar proveedor
    public ProveedorModel actualizarProveedor(ProveedorModel proveedor, Long id) {
        ProveedorModel proveedorExistente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // Validar RUC único si se está cambiando
        if (proveedor.getRuc() != null && !proveedor.getRuc().isEmpty() &&
                !proveedorExistente.getRuc().equals(proveedor.getRuc())) {
            if (proveedorRepository.existsByRuc(proveedor.getRuc())) {
                throw new RuntimeException("Ya existe un proveedor con el RUC: " + proveedor.getRuc());
            }
        }

        // Actualizar campos
        proveedorExistente.setNombre(proveedor.getNombre());
        proveedorExistente.setRuc(proveedor.getRuc());
        proveedorExistente.setTelefono(proveedor.getTelefono());
        proveedorExistente.setEmail(proveedor.getEmail());
        proveedorExistente.setDireccion(proveedor.getDireccion());
        proveedorExistente.setContacto(proveedor.getContacto());
        proveedorExistente.setMaterialEspecialidad(proveedor.getMaterialEspecialidad());

        return proveedorRepository.save(proveedorExistente);
    }

    // Eliminar proveedor (eliminación lógica)
    public void eliminarProveedor(Long id) {
        ProveedorModel proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    // Buscar proveedores por nombre
    public ArrayList<ProveedorModel> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreContaining(nombre);
    }

    // Buscar proveedores por material
    public ArrayList<ProveedorModel> buscarPorMaterial(String material) {
        return proveedorRepository.findByMaterialEspecialidadContaining(material);
    }
}