package one.digitalinnovation.gof.controller.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime localDateTime;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
}