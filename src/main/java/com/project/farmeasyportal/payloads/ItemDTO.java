package com.project.farmeasyportal.payloads;

import com.project.farmeasyportal.enums.ItemCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    @Null(message = "ID is auto-generated and should not be provided")
    private int id;

    @NotBlank(message = "Item name is required")
    @Size(max = 100, message = "Item name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Item description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Min(value = 1, message = "Total quantity must be at least 1")
    private int totalQuantity;

    @DecimalMin(value = "0.1", inclusive = false, message = "Price per day must be greater than 0.1")
    private BigDecimal pricePerDay;

    @NotNull(message = "Category is required")
    private String category;

    @NotNull(message = "Image is required")
    private String imageName;

    private MerchantDTO merchantDTO;

}

