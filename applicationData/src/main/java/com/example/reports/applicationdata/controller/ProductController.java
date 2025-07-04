package com.example.reports.applicationdata.controller;

import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.service.ProductServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public List<Product> getCustomers() {
        return productService.findAll();
    }

    @GetMapping("/product/{stockCode}")
    public Product getCustomerById(@PathVariable String stockCode) {
        return productService.findById(stockCode);
    }

    @PostMapping("/product/save")
    public void saveCustomer(@RequestBody Product product) {
        productService.save(product);
    }

    @PutMapping("/product/update/{stockCode}")
    public void updateCustomer(@PathVariable String stockCode, @RequestBody Product product) {
        product.setStockCode(stockCode);
        productService.update(product);
    }

    @DeleteMapping("/product/delete/{id}")
    public void deleteCustomer(@PathVariable String id) {
        Product product = productService.findById(id);
        if (product != null) {
            productService.delete(product);
        }
    }
}
