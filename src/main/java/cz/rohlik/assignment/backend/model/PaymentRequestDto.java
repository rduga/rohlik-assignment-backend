package cz.rohlik.assignment.backend.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PaymentRequestDto {

    @NotEmpty
    String paymentMethod;

    @NotEmpty
    String paymentDetails;
}
