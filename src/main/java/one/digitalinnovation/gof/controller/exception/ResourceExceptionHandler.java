package one.digitalinnovation.gof.controller.exception;

import one.digitalinnovation.gof.service.exception.EntidadeNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<StandardError> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = StandardError.builder()
				.localDateTime(LocalDateTime.now())
				.status(status.value())
				.error("Entidade não encontrada")
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ValidationError err = (ValidationError) ValidationError.builder()
				.localDateTime(LocalDateTime.now())
				.status(status.value())
				.error("Erro de validação de dados")
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardError> handleErroDeSistema(Exception e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		StandardError err = StandardError.builder()
				.localDateTime(LocalDateTime.now())
				.status(status.value())
				.error("Erro de sistema")
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		return ResponseEntity.status(status).body(err);
	}

}