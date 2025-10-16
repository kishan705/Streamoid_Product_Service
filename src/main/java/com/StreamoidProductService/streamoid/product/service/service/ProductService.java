package com.StreamoidProductService.streamoid.product.service.service;

import com.StreamoidProductService.streamoid.product.service.dto.FailedRow;
import com.StreamoidProductService.streamoid.product.service.dto.UploadResponse;
import com.StreamoidProductService.streamoid.product.service.model.Product;
import com.StreamoidProductService.streamoid.product.service.repository.ProductRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public UploadResponse uploadAndSaveCsv(MultipartFile file) throws IOException, CsvValidationException {
        List<Product> validProducts = new ArrayList<>();
        List<FailedRow> failedRows = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            String[] headers = csvReader.readNext(); // Skip header row
            if (headers == null) {
                throw new IllegalArgumentException("CSV file is empty or has no header.");
            }

            String[] line;
            long rowNumber = 2; // Start from row 2 (after header)
            while ((line = csvReader.readNext()) != null) {
                if (line.length == 1 && line[0].isEmpty()) continue; // Skip empty lines

                try {
                    Product product = parseRowToProduct(line);
                    validateProduct(product);
                    validProducts.add(product);
                } catch (IllegalArgumentException e) {
                    failedRows.add(new FailedRow(rowNumber, e.getMessage(), Arrays.toString(line)));
                }
                rowNumber++;
            }
        }

        if (!validProducts.isEmpty()) {
            productRepository.saveAll(validProducts);
        }

        return new UploadResponse(validProducts.size(), failedRows);
    }

    private Product parseRowToProduct(String[] row) {
        // Basic check for required fields based on count
        if (row.length < 8) { // CSV has 8 data columns
            throw new IllegalArgumentException("Incorrect number of columns. Expected 8, found " + row.length);
        }

        try {
            return Product.builder()
                    .sku(getTrimmedValue(row[0]))
                    .name(getTrimmedValue(row[1]))
                    .brand(getTrimmedValue(row[2]))
                    .color(getTrimmedValue(row[3]))
                    .size(getTrimmedValue(row[4]))
                    .mrp(parseDouble(getTrimmedValue(row[5]), "mrp"))
                    .price(parseDouble(getTrimmedValue(row[6]), "price"))
                    .quantity(parseInteger(getTrimmedValue(row[7]), "quantity"))
                    .build();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in row.");
        }
    }

    private void validateProduct(Product product) {
        // Rule: All required fields (sku, name, brand, mrp, price) must be present.
        if (!StringUtils.hasText(product.getSku())) throw new IllegalArgumentException("SKU is missing.");
        if (!StringUtils.hasText(product.getName())) throw new IllegalArgumentException("Name is missing.");
        if (!StringUtils.hasText(product.getBrand())) throw new IllegalArgumentException("Brand is missing.");
        if (product.getMrp() == null) throw new IllegalArgumentException("MRP is missing.");
        if (product.getPrice() == null) throw new IllegalArgumentException("Price is missing.");

        // Rule: price must be <= mrp
        if (product.getPrice() > product.getMrp()) {
            throw new IllegalArgumentException("Price (" + product.getPrice() + ") cannot be greater than MRP (" + product.getMrp() + ").");
        }
        // Rule: quantity >= 0 is already handled by @Min annotation, but we can double check
        if (product.getQuantity() != null && product.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative.");
        }
    }

    private String getTrimmedValue(String value) {
        return value != null ? value.trim() : null;
    }

    private Double parseDouble(String value, String fieldName) {
        if (!StringUtils.hasText(value)) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid format for " + fieldName + ": " + value);
        }
    }

    private Integer parseInteger(String value, String fieldName) {
        if (!StringUtils.hasText(value)) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid format for " + fieldName + ": " + value);
        }
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<Product> searchProducts(String brand, String color, Double minPrice, Double maxPrice) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(brand)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.toLowerCase()));
            }
            if (StringUtils.hasText(color)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("color")), color.toLowerCase()));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(spec);
    }
}