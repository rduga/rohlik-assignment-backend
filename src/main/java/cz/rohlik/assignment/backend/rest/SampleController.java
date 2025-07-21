package cz.rohlik.assignment.backend.rest;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Operation(summary = "Sample operation")
    @GetMapping("/sample")
    public String getSample() {
        return "Hello sample-operation";
    }
}
