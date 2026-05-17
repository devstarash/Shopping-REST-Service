package ru.starashchuk.shopping.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.starashchuk.shopping.service.models.Category;
import ru.starashchuk.shopping.service.servises.CategoryService;

import java.util.List;

@RestController("category")
public class CategoryController {
    private CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> findALl(){
        return categoryService.findAll();
    }

}
