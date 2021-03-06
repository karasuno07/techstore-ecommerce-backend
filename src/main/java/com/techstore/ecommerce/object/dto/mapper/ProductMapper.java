package com.techstore.ecommerce.object.dto.mapper;

import com.techstore.ecommerce.object.dto.request.ProductRequest;
import com.techstore.ecommerce.object.dto.response.ProductResponse;
import com.techstore.ecommerce.object.entity.jpa.Product;
import com.techstore.ecommerce.util.CustomStringUtil;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductResponse toResponseModel(Product product);

//    @Mapping(target = "details", ignore = true)
    Product createEntityFromRequest(ProductRequest request);

//    @Mapping(target = "details", ignore = true)
    void update(@MappingTarget Product product, ProductRequest request);

    @AfterMapping
    default void generateSlug(@MappingTarget Product product, ProductRequest request) {
        String slug = CustomStringUtil.generateSlug(request.getName());
        product.setSlug(slug);
    }
}
