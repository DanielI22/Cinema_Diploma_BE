package sit.tu_varna.bg.rest.exceptionhandler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorMessage {
    private String message;
}
