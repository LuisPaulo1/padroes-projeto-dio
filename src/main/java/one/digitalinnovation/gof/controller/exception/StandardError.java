package one.digitalinnovation.gof.controller.exception;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime localDateTime;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
}