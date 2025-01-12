package com.infy.product_service.services;

import com.infy.product_service.entity.Product;
import com.infy.product_service.errorHandling.CustomFileException;
import com.infy.product_service.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    // save product
    public void saveProduct(Product product) {
        log.info("Saving product: {}", product);
        try {
            productRepository.save(product);
            log.info("Product saved successfully: {}", product);
        } catch (Exception e) {
            log.error("Failed to save product: {}", product, e);
        }
    }

    // get all products
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = null;
        try {
            products = productRepository.findAll();
            log.info("Fetched {} products", products.size());
        } catch (Exception e) {
            log.error("Failed to fetch products", e);
        }
        return products;
    }

    // get product by id
    public Product getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            log.info("Product found: {}", product.get());
            return product.get();
        } else {
            log.warn("Product with id {} not found", id);
            return null;
        }
    }

    // update a product
    public Product updateProductById(Long id, Product product) {
        log.info("Updating product with id: {}", id);
        Optional<Product> product_to_update = productRepository.findById(id);
        if (product_to_update.isPresent()) {
            Product existingProduct = product_to_update.get();
            existingProduct.setProductName(product.getProductName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCountry(product.getCountry());
            Product updatedProduct = productRepository.save(existingProduct);
            log.info("Product updated successfully: {}", updatedProduct);
            return updatedProduct;
        } else {
            log.warn("Product with id {} not found for update", id);
            return null;
        }
    }

    // delete a product
    public boolean deleteProductById(Long id) {
        log.info("Deleting product with id: {}", id);
        Optional<Product> product_to_delete = productRepository.findById(id);
        if (product_to_delete.isPresent()) {
            productRepository.deleteById(id);
            log.info("Product with id {} deleted successfully", id);
            return true;
        } else {
            log.warn("Product with id {} not found for deletion", id);
            return false;
        }
    }

    // process Excel file to upload new products
    public void processExcelFile(MultipartFile file) {
        // Check if the uploaded file is empty
        if (file.isEmpty()) {
            log.error("The uploaded file is empty.");
            throw new CustomFileException("The uploaded file is empty.");
        }

        String contentType = file.getContentType();
        // Check if the file is of the correct format (Excel file)
        if (contentType == null ||
                !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            log.error("Invalid file format. Only Excel files are supported.");
            throw new CustomFileException("Invalid file format. Only Excel files are supported.");
        }

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Check if the Excel sheet is empty (no rows of data, excluding header row)
            if (sheet.getPhysicalNumberOfRows() <= 1) { // <= 1 because the first row is the header
                log.error("The Excel file is empty or contains no data rows.");
                throw new CustomFileException("The Excel file is empty or contains no data rows.");
            }

            // Check for required headers (productName and description)
            Row headerRow = sheet.getRow(0);
            if (headerRow == null || !isValidHeader(headerRow)) {
                log.error("The Excel file does not contain the required headers (productName, description).");
                throw new CustomFileException("The Excel file does not contain the required headers (productName, description).");
            }

            // List to hold the parsed product objects
            List<Product> products = new ArrayList<>();
            Set<String> existingProductNames = new HashSet<>();

            for (Row row : sheet) {
                // Skip header row (row.getRowNum() == 0)
                if (row.getRowNum() == 0) {
                    continue;
                }

                Product product = parseRowToProduct(row);
                if (product != null && !existingProductNames.contains(product.getProductName())) {
                    // If the product is not already in the set, add it and proceed to save
                    existingProductNames.add(product.getProductName());
                    products.add(product);
                } else if (product != null) {
                    log.info("Duplicate product detected: {}", product.getProductName());
                }
            }

            // If no valid products are found, throw an exception
            if (products.isEmpty()) {
                log.error("No valid product data found in the Excel file.");
                throw new CustomFileException("No valid product data found in the file.");
            }

            // Save all the valid products to the database
            productRepository.saveAll(products);
            log.info("{} products successfully added to the database.", products.size());

        } catch (Exception e) {
            log.error("Error processing Excel file: {}", e.getMessage());
            throw new CustomFileException("Error processing Excel file: " + e.getMessage());
        }
    }

    private Product parseRowToProduct(Row row) {
        try {
            String productName = getCellValue(row.getCell(0));
            String description = getCellValue(row.getCell(1));
            String country= getCellValue(row.getCell(2));
            if (productName == null || productName.isEmpty()) {
                log.warn("Product name is empty in row {}.", row.getRowNum() + 1);
                throw new CustomFileException("Product name cannot be empty in row " + (row.getRowNum() + 1));
            }

            Product product = new Product();
            product.setProductName(productName);
            product.setDescription(description);
            product.setCountry(country);

            return product;

        } catch (Exception e) {
            log.error("Error parsing row {}: {}", row.getRowNum() + 1, e.getMessage());
            throw new CustomFileException("Error parsing row " + (row.getRowNum() + 1) + ": " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }

        return null;
    }

    // Helper method to validate the headers
    private boolean isValidHeader(Row headerRow) {
        boolean hasProductName = false;
        boolean hasDescription = false;

        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue().trim().toLowerCase();
            if (cellValue.equals("productname")) {
                hasProductName = true;
            } else if (cellValue.equals("description")) {
                hasDescription = true;
            }
        }

        return hasProductName && hasDescription;
    }
}
