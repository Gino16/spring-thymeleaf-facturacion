package com.sistema.facturacion.models.service;

import com.sistema.facturacion.models.entity.Cliente;
import com.sistema.facturacion.models.entity.Factura;
import com.sistema.facturacion.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {
    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable);

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public void delete(Long id);

    public List<Producto> findByNombre(String term);

    public void saveFactura(Factura factura);

}
