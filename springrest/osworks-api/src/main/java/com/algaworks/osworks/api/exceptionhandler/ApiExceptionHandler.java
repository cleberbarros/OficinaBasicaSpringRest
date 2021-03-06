package com.algaworks.osworks.api.exceptionhandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.osworks.domain.exception.NegocioException;

import jdk.jfr.Experimental;

@ControllerAdvice   //diz que é um componente do spring que usamos para tratar Expections de todos os controllers que criarmos
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;  // injetando o que foi configurado em /src/main/resources/messages.properties
	
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleNegocio(EntidadeNaoEncontradaException ex, WebRequest resquest) {
		var status = HttpStatus.NOT_FOUND;
		
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, resquest);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest resquest) {
		var status = HttpStatus.BAD_REQUEST;
		
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, resquest);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		 var campos = new ArrayList<Problema.Campo>();
		 
		 for(ObjectError error: ex.getBindingResult().getAllErrors()) {
			 	String nome = ( (FieldError) error).getField();
			 	String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			 	
			 	campos.add(new Problema.Campo(nome, mensagem));
		 }
		 
		 var problema = new Problema();
		 problema.setStatus(status.value());
		 problema.setTitulo("Um ou mais campos estão inválido."
				 	+"Faça o preenchimento correto e tente nvamente");
		 problema.setDataHora(OffsetDateTime.now());
		 problema.setCampos(campos);
		 
		return super.handleExceptionInternal(ex, problema, headers, status, request);
	 }
	
}
