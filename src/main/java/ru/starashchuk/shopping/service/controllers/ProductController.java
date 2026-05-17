package ru.starashchuk.shopping.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.starashchuk.shopping.service.models.Product;
import ru.starashchuk.shopping.service.servises.ProductService;

import java.util.List;

@RestController()
@RequestMapping("products")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts(@RequestParam(value = "categoryId", required = false) Integer categoryId) {
        if (categoryId == null) {
            return productService.findAll();
        }
        return productService.findByCategoryId(categoryId);

    }

    @GetMapping("{id}")
    public Product findById(@PathVariable("id") int id) {
        return productService.findById(id);
    }




}
