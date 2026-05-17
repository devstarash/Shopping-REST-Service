package ru.starashchuk.shopping.service.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DAO.ProductDAO;
import ru.starashchuk.shopping.service.models.Product;

import java.util.List;
@Component
public class ProductService {
    private ProductDAO productDAO;
    @Autowired
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    public List<Product> findAll(){
        return productDAO.findAll();
    }

    public Product findById(int id) {
        return productDAO.findById(id);
    }

    public List<Product> findByCategoryId(int categoryId) {
        return productDAO.findByCategoryId(categoryId);
    }

}
