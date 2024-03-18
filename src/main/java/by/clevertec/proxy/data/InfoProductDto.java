package by.clevertec.proxy.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InfoProductDto(
        UUID uuid,
        String name,
        String description,
        BigDecimal price,
        LocalDateTime created) {
}
