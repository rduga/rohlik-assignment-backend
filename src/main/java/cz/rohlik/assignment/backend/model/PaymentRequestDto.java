package cz.rohlik.assignment.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class PaymentRequestDto {

    @NotEmpty
    @Schema(description = "Payment method used", example = "CARD")
    String paymentMethod;

    @NotEmpty
    @Schema(description = "Payment details (e.g., card number, account info)", example = "4111-xxxx-xxxx-1234")
    String paymentDetails;

    @PositiveOrZero
    @Schema(description = "Amount paid", example = "199.99")
    BigDecimal amount;
}
