package ru.starashchuk.shopping.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.starashchuk.shopping.service.models.Product;
import ru.starashchuk.shopping.service.servises.ProductService;

import java.util.List;

@RestController("/products")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.findAll();

    }

    @GetMapping("{id}")
    public Product findById(@PathVariable int id) {
        return productService.findById(id);
    }

    @GetMapping
    public List<Product> findByCategoryId(@RequestParam Integer category) {
        if (category == null) {
            return productService.findAll();
        }
        return productService.findByCategoryId(category);
    }


}
