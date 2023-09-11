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
		StandardError err = new StandardError();
				err.setLocalDateTime(LocalDateTime.now());
				err.setStatus(status.value());
				err.setError("Entidade não encontrada");
				err.setMessage(e.getMessage());
				err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ValidationError err = new ValidationError();
				err.setLocalDateTime(LocalDateTime.now());
				err.setStatus(status.value());
				err.setError("Erro de validação de dados");
				err.setMessage(e.getMessage());
				err.setPath(request.getRequestURI());
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<StandardError> handleErroDeSistema(IllegalArgumentException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new	StandardError();
		err.setLocalDateTime(LocalDateTime.now());
		err.setStatus(status.value());
		err.setError("Erro de dados");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardError> handleErroDeSistema(Exception e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		StandardError err = new	StandardError();
				err.setLocalDateTime(LocalDateTime.now());
				err.setStatus(status.value());
				err.setError("Erro de sistema");
				err.setMessage(e.getMessage());
				err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

}