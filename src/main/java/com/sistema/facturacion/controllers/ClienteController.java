package com.sistema.facturacion.controllers;

import com.sistema.facturacion.models.entity.Cliente;
import com.sistema.facturacion.models.entity.Producto;
import com.sistema.facturacion.models.service.IClienteService;
import com.sistema.facturacion.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @Value("${uploads.paths}")
    private String rootPath;

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable Long id, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(id);
        //Cliente cliente = clienteService.fetchByIdWithFacturas(id);

        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la BD");
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente: " + cliente.getNombre() + ", " + cliente.getApellido());
        return "ver";
    }


    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 4);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", "Listado de Clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @RequestMapping(value = "/listar-todo", method = RequestMethod.GET)
    public String listarTodo(Model model){
        List<Cliente> clientes = clienteService.findAll();
        model.addAttribute("clientes", clientes);
        return "listar-todo";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam(name = "file", required = false) MultipartFile foto, SessionStatus status, RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        if (foto != null) {
        	if (!foto.isEmpty()) {

                if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() !=null && cliente.getFoto().length() > 0){
                    Path root = Paths.get(rootPath).resolve(cliente.getFoto()).toAbsolutePath();
                    File archivo = root.toFile();

                    if (archivo.exists() && archivo.canRead()) {
                        if (archivo.delete()) {
                            flash.addFlashAttribute("info", "Foto cambiada del usuario " + cliente.getNombre());
                        }
                    }
                }

                try {
                    byte[] bytes = foto.getBytes();
                    String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
                    Path rutaCompleta = Paths.get(rootPath + "//" + uniqueFilename);
                    Files.write(rutaCompleta, bytes);
                    flash.addFlashAttribute("info", "Se ha subido correctamnte la foto " + foto.getOriginalFilename());
                    cliente.setFoto(uniqueFilename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}else {
			cliente.setFoto("");
		}

        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con exito" : "Cliente creado con exito!";

        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "No se encuentra el cliente el id= " + id);
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID no puede ser menor que 0");
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito!");
            Path root = Paths.get(rootPath).resolve(cliente.getFoto()).toAbsolutePath();
            File archivo = root.toFile();

            if (archivo.exists() && archivo.canRead()) {
                if (archivo.delete()) {
                    flash.addFlashAttribute("info", "Foto eliminada del usuario " + cliente.getNombre());
                }
            }
        }
        return "redirect:/listar";
    }


}
