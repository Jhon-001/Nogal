package nogal.com.nogal.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nogal.com.nogal.models.ProductoModel;
import nogal.com.nogal.services.ProductoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ArrayList<ProductoModel> getProductos() {
        return this.productoService.obtenerProductosActivos();
    }

    @GetMapping("/todos")
    public ArrayList<ProductoModel> getTodosProductos() {
        return this.productoService.obtenerTodosProductos();
    }

    @PostMapping(path = "/crear")
    public ProductoModel nuevoProducto(@RequestBody ProductoModel producto) {
        return this.productoService.guardarProducto(producto);
    }

    @GetMapping("/{id}")
    public Optional<ProductoModel> obtenerProductoId(@PathVariable("id") Long id) {
        return this.productoService.obtenerPorId(id);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ArrayList<ProductoModel> obtenerProductosPorProveedor(@PathVariable("proveedorId") Long proveedorId) {
        return this.productoService.buscarPorProveedor(proveedorId);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ArrayList<ProductoModel> obtenerProductosPorCategoria(@PathVariable("categoriaId") Long categoriaId) {
        return this.productoService.buscarPorCategoria(categoriaId);
    }

    @GetMapping("/nombre/{nombre}")
    public ArrayList<ProductoModel> obtenerProductosPorNombre(@PathVariable("nombre") String nombre) {
        return this.productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/material/{material}")
    public ArrayList<ProductoModel> obtenerProductosPorMaterial(@PathVariable("material") String material) {
        return this.productoService.buscarPorMaterial(material);
    }

    @GetMapping("/stock-bajo")
    public ArrayList<ProductoModel> obtenerProductosStockBajo() {
        return this.productoService.obtenerProductosConStockBajo();
    }

    // NUEVO ENDPOINT: Productos en oferta (precio venta > 300)
    @GetMapping("/ofertas")
    public ArrayList<ProductoModel> obtenerProductosEnOferta() {
        return this.productoService.obtenerProductosEnOferta();
    }

    // NUEVO ENDPOINT: Productos económicos (precio venta < 100)
    @GetMapping("/economicos")
    public ArrayList<ProductoModel> obtenerProductosEconomicos() {
        return this.productoService.obtenerProductosEconomicos();
    }

    @PutMapping(path = "/{id}")
    public ProductoModel actualizarProducto(@RequestBody ProductoModel producto, @PathVariable Long id) {
        return this.productoService.actualizarProducto(producto, id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarProducto(@PathVariable("id") Long id) {
        this.productoService.eliminarProducto(id);
        return "Producto eliminado correctamente";
    }

    @PutMapping(path = "/{id}/stock/{cantidad}")
    public String actualizarStock(@PathVariable("id") Long id, @PathVariable("cantidad") Integer cantidad) {
        this.productoService.actualizarStock(id, cantidad);
        return "Stock actualizado correctamente";
    }
}