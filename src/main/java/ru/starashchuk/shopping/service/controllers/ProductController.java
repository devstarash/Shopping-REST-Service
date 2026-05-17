package ru.starashchuk.shopping.service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.starashchuk.shopping.service.models.Product;

import java.util.List;

@RestController("/products")
public class ProductController {
    @GetMapping
    public List<Product> getProducts(){
        return null;

    }
    @GetMapping("{id}")
    public Product findById(@PathVariable int id){
        return null;


    }

}
