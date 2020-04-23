package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.repository.ClienteRepository;
import com.algaworks.osworks.domain.service.CadastroClienteService;



@RestController
@RequestMapping("/clientes") //Esse anotação define que esse controlador irá responder sempre a /clientes
public class ClienteController {

	//NÃO PRECISO MAIS AO USAR O REPOSITORY
//	@PersistenceContext
//	private EntityManager manager;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CadastroClienteService cadastroCliente;
	
	@GetMapping
	public List<Cliente> listar() {
		return clienteRepository.findAll();
		//return clienteRepository.findByNome("Ana Carla");
		//return clienteRepository.findByNomeContaining("Cl");
	}
	
	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {  //@PathVariable para associar o que vem em /{clienteId} com o parametro do metodo
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);
		
		//Teste se existe uma informação para retornar
		if(cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		
		//Para retornar o o Status: 404 Not Found
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)  //Anotação para retornar a resposta como 201 Created
	public Cliente adicionar(@Valid @RequestBody Cliente cliente ) {  //A anotação @RequestBody pega o Json que vier e transformar em um objeto Cliente
	
		return cadastroCliente.salvar(cliente);
	}
	
	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> atualizar (@Valid @PathVariable Long clienteId, 
				@RequestBody Cliente cliente){  //@RequestBody para transformar o Json em objeto cliente
		
		if( !clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		cliente.setId(clienteId);
		cliente = cadastroCliente.salvar(cliente);
		
		return ResponseEntity.ok(cliente);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id){
		
		if( !clienteRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		

		cadastroCliente.excluir(id);
		
		return ResponseEntity.noContent().build();  //passando o Status 204 No Content
		
	}
	
}
