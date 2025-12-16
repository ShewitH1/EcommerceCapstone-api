package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.sql.SQLException;
import java.util.List;

@RestController // add the annotations to make this a REST controller
@RequestMapping("/categories")// add the annotation to make this controller the endpoint for the following url // http://localhost:8080/categories
@CrossOrigin// add annotation to allow cross site origin requests
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired// create an Autowired controller to inject the categoryDao and ProductDao
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }


    @GetMapping("") // add the appropriate annotation for a get action
    public List<Category> getAll() throws SQLException {
        // find and return all categories

        try{
            return categoryDao.getAllCategories();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting categories");
        }
    }

    @GetMapping("/{id}") // add the appropriate annotation for a get action
    public Category getById(@PathVariable int id) throws SQLException {
        // get the category by id
//        return categoryDao.getById(id);

        //this is the one
        try{
            var category = categoryDao.getById(id);
            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return category; // get the category by id
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"getById Error");
        }
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return null;
    }

    @PostMapping// add annotation to call this method for a POST action
    @PreAuthorize("hasRole('ROLE_ADMIN')") // add annotation to ensure that only an ADMIN can call this function
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) throws SQLException {
        // insert the category
        return categoryDao.create(category);

    }

    @PutMapping("/{id}") // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    @PreAuthorize("hasRole('ROLE_ADMIN')") // add annotation to ensure that only an ADMIN can call this function
    public void updateCategory(@PathVariable int id, @RequestBody Category category) throws SQLException {
        // update the category by id
        //good
        try {
            categoryDao.update(id, category);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "updateCategory() Error");
        }
    }


    @DeleteMapping("/{id}") // add annotation to call this method for a DELETE action - the url path must include the categoryId
    @PreAuthorize("hasRole('ROLE_ADMIN')") // add annotation to ensure that only an ADMIN can call this function
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id) throws SQLException {
        // delete the category by id
        //good
        try {
            var category = categoryDao.getById(id);

            if(category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            categoryDao.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "deleteCategory() Error");
        }

    }
}