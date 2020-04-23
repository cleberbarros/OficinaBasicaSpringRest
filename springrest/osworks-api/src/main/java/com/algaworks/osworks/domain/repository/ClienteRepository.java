package com.algaworks.osworks.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.osworks.domain.model.Cliente;

@Repository  //com essa anotação eu informo que o essa classe é um componente do spring e posso usar injeção de dependencias 
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

		List<Cliente> findByNome(String nome);
		
		List<Cliente> findByNomeContaining(String nome);
}
