package by.clevertec.proxy.util;

import static by.clevertec.proxy.util.TestConstant.PRODUCT_DESCRIPTION;
import static by.clevertec.proxy.util.TestConstant.PRODUCT_NAME;
import static by.clevertec.proxy.util.TestConstant.PRODUCT_PRICE;
import static by.clevertec.proxy.util.TestConstant.PRODUCT_UUID;

import by.clevertec.proxy.data.InfoProductDto;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class InfoProductTestBuilder {

    @Builder.Default
    private UUID uuid = PRODUCT_UUID;

    @Builder.Default
    private String name = PRODUCT_NAME;

    @Builder.Default
    private String description = PRODUCT_DESCRIPTION;

    @Builder.Default
    private BigDecimal price = PRODUCT_PRICE;

    public InfoProductDto buildInfoProductDto() {
        return new InfoProductDto(uuid, name, description, price);
    }
}
