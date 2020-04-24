package com.algaworks.osworks.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*Essa classe é necessaria pois o ModelMapper trata-se de uma biblioteca de terceiros e não consigo injetar
 * em OrdemServicoController, fazendo isso retorno uma instancia de ModelMapper como um componente Bean
 * gerenciado pelo Spring e com isso é possivel a injeção da dependencia.*/

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
