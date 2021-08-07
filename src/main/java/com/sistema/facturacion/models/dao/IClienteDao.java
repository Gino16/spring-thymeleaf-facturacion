package com.sistema.facturacion.models.dao;



import com.sistema.facturacion.models.entity.Cliente;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

}
