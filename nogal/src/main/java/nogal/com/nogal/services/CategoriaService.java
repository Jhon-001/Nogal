package nogal.com.nogal.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nogal.com.nogal.models.CategoriaModel;
import nogal.com.nogal.repositories.ICategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    // Obtener todas las categorías
    public ArrayList<CategoriaModel> obtenerTodasCategorias() {
        return (ArrayList<CategoriaModel>) categoriaRepository.findAll();
    }

    // Guardar nueva categoría
    public CategoriaModel guardarCategoria(CategoriaModel categoria) {
        // Validar nombre único
        Optional<CategoriaModel> categoriaExistente = categoriaRepository.findByNombre(categoria.getNombre());
        if (categoriaExistente.isPresent()) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }

    // Obtener categoría por ID
    public Optional<CategoriaModel> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    // Obtener categoría por nombre
    public Optional<CategoriaModel> obtenerPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    // Actualizar categoría
    public CategoriaModel actualizarCategoria(CategoriaModel categoria, Long id) {
        CategoriaModel categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Validar nombre único si se está cambiando
        if (!categoriaExistente.getNombre().equals(categoria.getNombre())) {
            Optional<CategoriaModel> categoriaConMismoNombre = categoriaRepository.findByNombre(categoria.getNombre());
            if (categoriaConMismoNombre.isPresent()) {
                throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombre());
            }
        }

        categoriaExistente.setNombre(categoria.getNombre());
        categoriaExistente.setDescripcion(categoria.getDescripcion());

        return categoriaRepository.save(categoriaExistente);
    }

    // Eliminar categoría
    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

    // Buscar categorías por nombre
    public ArrayList<CategoriaModel> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombreContaining(nombre);
    }
}