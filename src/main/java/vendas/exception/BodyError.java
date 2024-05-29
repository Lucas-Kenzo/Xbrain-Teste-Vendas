package vendas.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BodyError {

    private LocalDateTime dataHora;
    private String mensagem;
}
