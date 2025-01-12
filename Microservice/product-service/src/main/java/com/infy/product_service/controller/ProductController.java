package com.infy.product_service.controller;

import com.infy.product_service.entity.Product;
import com.infy.product_service.errorHandling.CustomFileException;
import com.infy.product_service.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "APIs to manage products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("add-product")
    @Operation(
            summary = "Add a new product",
            description = "Add new products to database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully added",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request, product not saved")
            }
    )
    public ResponseEntity<?> saveProduct(@RequestBody Product newProduct) {
        try {
            productService.saveProduct(newProduct);
            return new ResponseEntity<>(newProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Product not saved.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("upload-product")
    @Operation(
            summary = "Upload products from Excel file",
            description = "Upload an excel file to add new products to database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully added",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request, product not saved"),
                    @ApiResponse(responseCode = "415", description = "Unsupported file format")
            }
    )
    public ResponseEntity<?> addProductFromExcel(@RequestParam("file")MultipartFile file) {
        try {
            productService.processExcelFile(file);
            return new ResponseEntity<>("File processed successfully", HttpStatus.OK);
        } catch (CustomFileException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Product not saved. Error: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-product/{productId}")
    @Operation(
            summary = "Get a product by ID",
            description = "Fetch a product from database using product ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid product ID provided"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        try {
            Product product_by_id = productService.getProductById(productId);
            if (product_by_id != null) {
                return new ResponseEntity<>(product_by_id, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found.",
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Please provide a correct product ID.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-all-products")
    @Operation(
            summary = "Get all products",
            description = "Fetch all existing products from database",
            tags = {"Product API"}, // Added the 'tags' property to group under Product API
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched all products",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "No products found"),
                    @ApiResponse(responseCode = "400", description = "Failed to fetch products")
            }
    )
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> allProducts = productService.getAllProducts();
            if (!allProducts.isEmpty()) {
                return new ResponseEntity<>(allProducts, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No products found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch products.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update-product/{productId}")
    @Operation(
            summary = "Update a product",
            description = "Update an existing product from database using product id",
            tags = {"Product API"}, // Added the 'tags' property to group under Product API
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found for update"),
                    @ApiResponse(responseCode = "400", description = "Failed to update product")
            }
    )
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @RequestBody Product updatedProduct) {
        try {
            Product product = productService.updateProductById(productId, updatedProduct);
            if (product != null) {
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found for update.",
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete-product/{productId}")
    @Operation(
            summary = "Delete a product",
            description = "Delete an existing product from database using product id",
            tags = {"Product API"}, // Added the 'tags' property to group under Product API
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product deleted successfully",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Product not found for deletion"),
                    @ApiResponse(responseCode = "400", description = "Failed to delete product")
            }
    )
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            boolean isDeleted = productService.deleteProductById(productId);
            if (isDeleted) {
                return new ResponseEntity<>("Product deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found for deletion.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete product.", HttpStatus.BAD_REQUEST);
        }
    }


}
