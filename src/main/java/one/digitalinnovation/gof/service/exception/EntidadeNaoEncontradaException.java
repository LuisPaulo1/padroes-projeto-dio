package one.digitalinnovation.gof.service.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {
	public EntidadeNaoEncontradaException(String msg){
		super(msg);
	}
}