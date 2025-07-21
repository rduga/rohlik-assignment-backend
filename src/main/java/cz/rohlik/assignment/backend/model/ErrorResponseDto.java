package cz.rohlik.assignment.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Value
@Builder
@Jacksonized
public class ErrorResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Instant timestamp;

    String message;

    String error;
}
