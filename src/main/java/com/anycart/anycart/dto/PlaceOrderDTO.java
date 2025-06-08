package com.anycart.anycart.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDTO {
    @NotEmpty
    private List<@NotNull Long> cartItemIds;

    @NotBlank
    private String shippingAddress;
}
