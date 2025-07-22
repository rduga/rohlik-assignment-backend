package cz.rohlik.assignment.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

@Value
@Builder
@Jacksonized
public class ErrorResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @Schema(description = "Timestamp of the error occurrence (epoch milliseconds)", example = "1681234567890")
    Instant timestamp;

    @Schema(description = "Error message", example = "Entity not found")
    String message;

    @Schema(description = "Error type or HTTP status reason", example = "Not Found")
    String error;
}
