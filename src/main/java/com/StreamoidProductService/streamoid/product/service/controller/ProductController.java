package com.StreamoidProductService.streamoid.product.service.controller;

import com.StreamoidProductService.streamoid.product.service.dto.UploadResponse;
import com.StreamoidProductService.streamoid.product.service.model.Product;
import com.StreamoidProductService.streamoid.product.service.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // Corrected import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            UploadResponse response = productService.uploadAndSaveCsv(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // A more specific exception handling can be added here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        // This line now works because the type matches
        Pageable pageable = PageRequest.of(page, limit);
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Product> products = productService.searchProducts(brand, color, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
}