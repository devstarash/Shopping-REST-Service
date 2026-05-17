package ru.starashchuk.shopping.service.servises;

import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DAO.CategoryDAO;
import ru.starashchuk.shopping.service.models.Category;

import java.util.List;

@Component
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> findAll() {
        return categoryDAO.findAll();
    }
}