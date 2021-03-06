package com.techstore.ecommerce.object.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductRequest {

    @NotBlank(message = "blank")
    private String name;

    @NotNull(message = "null")
    private int categoryId;

    @NotNull(message = "null")
    private int brandId;

    @NotEmpty(message = "empty")
    private List<ProductDetailRequest> details;
}
