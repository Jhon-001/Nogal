package nogal.com.nogal.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nogal.com.nogal.models.CategoriaModel;
import nogal.com.nogal.services.CategoriaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ArrayList<CategoriaModel> getCategorias() {
        return this.categoriaService.obtenerTodasCategorias();
    }

    @PostMapping(path = "/crear")
    public CategoriaModel nuevaCategoria(@RequestBody CategoriaModel categoria) {
        return this.categoriaService.guardarCategoria(categoria);
    }

    @GetMapping("/{id}")
    public Optional<CategoriaModel> obtenerCategoriaId(@PathVariable("id") Long id) {
        return this.categoriaService.obtenerPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public Optional<CategoriaModel> obtenerCategoriaPorNombre(@PathVariable("nombre") String nombre) {
        return this.categoriaService.obtenerPorNombre(nombre);
    }

    @GetMapping("/buscar/{nombre}")
    public ArrayList<CategoriaModel> buscarCategoriasPorNombre(@PathVariable("nombre") String nombre) {
        return this.categoriaService.buscarPorNombre(nombre);
    }

    @PutMapping(path = "/{id}")
    public CategoriaModel actualizarCategoria(@RequestBody CategoriaModel categoria, @PathVariable Long id) {
        return this.categoriaService.actualizarCategoria(categoria, id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarCategoria(@PathVariable("id") Long id) {
        this.categoriaService.eliminarCategoria(id);
        return "Categoría eliminada correctamente";
    }
}