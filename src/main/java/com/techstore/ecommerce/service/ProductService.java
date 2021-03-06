package com.techstore.ecommerce.service;

import com.techstore.ecommerce.object.exception.ResourceNotFoundException;
import com.techstore.ecommerce.object.dto.filter.ProductFilter;
import com.techstore.ecommerce.object.dto.request.ProductRequest;
import com.techstore.ecommerce.object.entity.jpa.Brand;
import com.techstore.ecommerce.object.entity.jpa.Category;
import com.techstore.ecommerce.object.entity.jpa.Product;
import com.techstore.ecommerce.object.entity.jpa.ProductDetail;
import com.techstore.ecommerce.object.dto.mapper.ProductDetailMapper;
import com.techstore.ecommerce.object.dto.mapper.ProductMapper;
import com.techstore.ecommerce.repository.ProductRepository;
import com.techstore.ecommerce.repository.spec.ProductSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;
    private final ProductDetailMapper productDetailMapper;

    private final BrandService brandService;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;

    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public Product findProductById(long id) {
        return productRepo.findDefaultProductById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any product with id " + id));
    }

    public Product createProduct(ProductRequest request) {
        Product product = productMapper.createEntityFromRequest(request);
        uploadImg(request);
        return saveProduct(product, request);
    }

    public Product updateProduct(long id, ProductRequest request) {
        Product product = findProductById(id);
        productMapper.update(product, request);
        uploadImg(request);
        return saveProduct(product, request);
    }

    private Product saveProduct(Product product, ProductRequest request) {
        Brand brand = brandService.findBrandById(request.getBrandId());
        product.setBrand(brand);
        Category category = categoryService.findCategoryById(request.getCategoryId());
        product.setCategory(category);
        List<ProductDetail> details = request.getDetails().stream()
                                             .map(productDetailMapper::createEntityFromRequest)
                                             .collect(Collectors.toList());
        product.setDetails(details);

        return productRepo.save(product);
    }

    public void uploadImg(ProductRequest request){
        request.getDetails().forEach(productDetail -> {
            if(productDetail.getImageFiles() != null){
                productDetail.getImageFiles().forEach(img -> cloudinaryService.uploadImage(null, img));
            }
        });
    }

    public void activateProduct(long id) {
        Product product = findProductById(id);
        product.setAvailable(true);
        productRepo.save(product);
    }

    public void deactivateProduct(long id) {
        Product product = findProductById(id);
        product.setAvailable(false);
        productRepo.save(product);
    }

}
