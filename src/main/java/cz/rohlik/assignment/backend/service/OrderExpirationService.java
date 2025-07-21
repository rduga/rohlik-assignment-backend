package cz.rohlik.assignment.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
@ConditionalOnProperty(
    name = "app.order.expiration.scheduler.enabled",
    havingValue = "true",
    matchIfMissing = true)
public class OrderExpirationService {

    OrderService orderService;

    @Scheduled(
        fixedRateString = "${app.order.expiration.scheduler.fixed-rate-seconds:15}",
        timeUnit = TimeUnit.SECONDS
    )
    public void checkForExpiredOrders() {
        log.info("Checking for expired orders...");
        orderService.checkForExpiredOrders();
    }
}
