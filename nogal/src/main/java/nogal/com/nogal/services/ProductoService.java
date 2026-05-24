package nogal.com.nogal.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nogal.com.nogal.models.ProductoModel;
import nogal.com.nogal.repositories.IProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private IProductoRepository productoRepository;

    // Obtener todos los productos activos
    public ArrayList<ProductoModel> obtenerProductosActivos() {
        return productoRepository.findByActivoTrue();
    }

    // Obtener todos los productos
    public ArrayList<ProductoModel> obtenerTodosProductos() {
        return (ArrayList<ProductoModel>) productoRepository.findAll();
    }

    // Guardar nuevo producto
    public ProductoModel guardarProducto(ProductoModel producto) {
        return productoRepository.save(producto);
    }

    // Obtener producto por ID
    public Optional<ProductoModel> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Actualizar producto - CORREGIDO
    public ProductoModel actualizarProducto(ProductoModel producto, Long id) {
        ProductoModel productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setMaterial(producto.getMaterial());
        productoExistente.setDimensiones(producto.getDimensiones());
        productoExistente.setColor(producto.getColor());
        productoExistente.setCaracteristicas(producto.getCaracteristicas());
        productoExistente.setPrecioCompra(producto.getPrecioCompra());
        productoExistente.setPrecioVenta(producto.getPrecioVenta());
        productoExistente.setImagenUrl(producto.getImagenUrl()); // NUEVA LÍNEA
        
        if(producto.getProveedor() != null) {
            productoExistente.setProveedor(producto.getProveedor());
        }
        if(producto.getCategoria() != null) {
            productoExistente.setCategoria(producto.getCategoria());
        }
        productoExistente.setStock(producto.getStock());
        return productoRepository.save(productoExistente);
    }

    // Eliminar producto (eliminación lógica)
    public void eliminarProducto(Long id) {
        ProductoModel producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    // Buscar productos por nombre
    public ArrayList<ProductoModel> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContaining(nombre);
    }

    // Buscar productos por proveedor
    public ArrayList<ProductoModel> buscarPorProveedor(Long proveedorId) {
        return productoRepository.findByProveedorId(proveedorId);
    }

    // Buscar productos por categoría
    public ArrayList<ProductoModel> buscarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    // Buscar productos con stock bajo
    public ArrayList<ProductoModel> obtenerProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    // Actualizar stock
    public void actualizarStock(Long productoId, Integer cantidad) {
        ProductoModel producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }

    // Buscar productos por material
    public ArrayList<ProductoModel> buscarPorMaterial(String material) {
        return productoRepository.findByMaterialContaining(material);
    }

    // Obtener productos en oferta (precio venta > 300)
    public ArrayList<ProductoModel> obtenerProductosEnOferta() {
        ArrayList<ProductoModel> todosProductos = productoRepository.findByActivoTrue();
        ArrayList<ProductoModel> productosOferta = new ArrayList<>();

        for (ProductoModel producto : todosProductos) {
            if (producto.getPrecioVenta().compareTo(new BigDecimal("300")) > 0) {
                productosOferta.add(producto);
            }
        }
        return productosOferta;
    }

    // Obtener productos económicos (precio venta < 100)
    public ArrayList<ProductoModel> obtenerProductosEconomicos() {
        ArrayList<ProductoModel> todosProductos = productoRepository.findByActivoTrue();
        ArrayList<ProductoModel> productosEconomicos = new ArrayList<>();

        for (ProductoModel producto : todosProductos) {
            if (producto.getPrecioVenta().compareTo(new BigDecimal("100")) < 0) {
                productosEconomicos.add(producto);
            }
        }
        return productosEconomicos;
    }
}