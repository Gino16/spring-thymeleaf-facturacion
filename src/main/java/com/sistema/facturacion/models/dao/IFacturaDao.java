package com.sistema.facturacion.models.dao;

import com.sistema.facturacion.models.entity.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
}
