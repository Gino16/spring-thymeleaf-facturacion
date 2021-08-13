package com.sistema.facturacion.controllers;

import com.sistema.facturacion.models.entity.Cliente;
import com.sistema.facturacion.models.entity.Factura;
import com.sistema.facturacion.models.entity.ItemFactura;
import com.sistema.facturacion.models.entity.Producto;
import com.sistema.facturacion.models.service.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable Long clienteId, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la BD");
            return "redirect:/listar";
        }

        Factura factura = new Factura();
        factura.setCliente(cliente);
        model.put("factura", factura);
        model.put("titulo", "Crear factura");

        return "factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody
    List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByNombre(term);
    }

    @PostMapping("/form")
    public String guardar(@Valid Factura factura,
                          BindingResult result,
                          Model model,
                          @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                          RedirectAttributes flash,
                          SessionStatus status) {

        if (result.hasErrors()){
            model.addAttribute("titulo", "Crear Factura");
            return "factura/form";
        }

        if (itemId == null || itemId.length == 0){
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "Error: La factura NO PUEDE ESTAR VACIA SIN ITEMS");
            return "factura/form";
        }

        for (int i = 0; i < itemId.length; i++) {
            Producto producto = clienteService.findProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);
            logger.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
        }
        clienteService.saveFactura(factura);
        status.setComplete();
        flash.addFlashAttribute("success", "La factura fue guardada correctamente");

        return "redirect:/ver/" + factura.getCliente().getId();
    }
}
