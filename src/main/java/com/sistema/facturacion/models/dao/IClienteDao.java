package com.sistema.facturacion.models.dao;



import com.sistema.facturacion.models.entity.Cliente;
import org.springframework.data.repository.CrudRepository;


public interface IClienteDao extends CrudRepository<Cliente, Long> {

}
