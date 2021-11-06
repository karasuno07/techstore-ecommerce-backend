package com.techstore.ecommerce.repository.jpa;

import com.techstore.ecommerce.object.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductReviewRepository extends
        JpaRepository<ProductReview, Long>, JpaSpecificationExecutor<ProductReview> {
}
