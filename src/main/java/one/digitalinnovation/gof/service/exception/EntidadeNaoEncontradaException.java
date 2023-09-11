package one.digitalinnovation.gof.service.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {
	public EntidadeNaoEncontradaException(String mensagem){
		super(mensagem);
	}
}