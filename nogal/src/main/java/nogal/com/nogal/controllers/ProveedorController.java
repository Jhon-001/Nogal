package nogal.com.nogal.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nogal.com.nogal.models.ProveedorModel;
import nogal.com.nogal.services.ProveedorService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ArrayList<ProveedorModel> getProveedores() {
        return this.proveedorService.obtenerProveedoresActivos();
    }

    @GetMapping("/todos")
    public ArrayList<ProveedorModel> getTodosProveedores() {
        return this.proveedorService.obtenerTodosProveedores();
    }

    @PostMapping(path = "/crear")
    public ProveedorModel nuevoProveedor(@RequestBody ProveedorModel proveedor) {
        return this.proveedorService.guardarProveedor(proveedor);
    }

    @GetMapping("/{id}")
    public Optional<ProveedorModel> obtenerProveedorId(@PathVariable("id") Long id) {
        return this.proveedorService.obtenerPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public ArrayList<ProveedorModel> obtenerProveedoresPorNombre(@PathVariable("nombre") String nombre) {
        return this.proveedorService.buscarPorNombre(nombre);
    }

    @GetMapping("/material/{material}")
    public ArrayList<ProveedorModel> obtenerProveedoresPorMaterial(@PathVariable("material") String material) {
        return this.proveedorService.buscarPorMaterial(material);
    }

    @PutMapping(path = "/{id}")
    public ProveedorModel actualizarProveedor(@RequestBody ProveedorModel proveedor, @PathVariable Long id) {
        return this.proveedorService.actualizarProveedor(proveedor, id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarProveedor(@PathVariable("id") Long id) {
        this.proveedorService.eliminarProveedor(id);
        return "Proveedor eliminado correctamente";
    }
}