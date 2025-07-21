package cz.rohlik.assignment.backend.model;

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
    String paymentMethod;

    @NotEmpty
    String paymentDetails;

    @PositiveOrZero
    BigDecimal amount;
}
