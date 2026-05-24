package nogal.com.nogal.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nogal.com.nogal.models.IngresoInventarioModel;
import nogal.com.nogal.services.IngresoInventarioService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/ingreso-inventario")
public class IngresoInventarioController {

    @Autowired
    private IngresoInventarioService ingresoInventarioService;

    @GetMapping
    public ArrayList<IngresoInventarioModel> getIngresos() {
        return this.ingresoInventarioService.obtenerTodosIngresos();
    }

    @PostMapping(path = "/crear")
    public IngresoInventarioModel nuevoIngreso(@RequestBody IngresoInventarioModel ingreso) {
        System.out.println("Ejemplo");
        System.out.println(ingreso.getNumeroFactura());
        return this.ingresoInventarioService.guardarIngreso(ingreso);
    }

    @GetMapping("/{id}")
    public Optional<IngresoInventarioModel> obtenerIngresoId(@PathVariable("id") Long id) {
        return this.ingresoInventarioService.obtenerPorId(id);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ArrayList<IngresoInventarioModel> obtenerIngresosPorProveedor(
            @PathVariable("proveedorId") Long proveedorId) {
        return this.ingresoInventarioService.buscarPorProveedor(proveedorId);
    }

    @GetMapping("/factura/{numeroFactura}")
    public ArrayList<IngresoInventarioModel> obtenerIngresosPorFactura(
            @PathVariable("numeroFactura") String numeroFactura) {
        return this.ingresoInventarioService.buscarPorNumeroFactura(numeroFactura);
    }

    @GetMapping("/pendientes-pago")
    public ArrayList<IngresoInventarioModel> obtenerIngresosPendientesPago() {
        return this.ingresoInventarioService.obtenerIngresosPendientesPago();
    }

    @GetMapping("/rango-fechas")
    public ArrayList<IngresoInventarioModel> obtenerIngresosPorRangoFechas(
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin) {
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        return this.ingresoInventarioService.buscarPorRangoFechas(inicio, fin);
    }

    @GetMapping("/estadisticas/mensual")
    public Double obtenerTotalIngresosMensual(
            @RequestParam("anio") int anio,
            @RequestParam("mes") int mes) {
        return this.ingresoInventarioService.obtenerTotalIngresosPorMes(anio, mes);
    }
}