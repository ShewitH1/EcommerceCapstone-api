package org.yearup.data;

import org.yearup.models.Category;
import org.yearup.models.ShoppingCart;

import java.sql.SQLException;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here


    //  create a POST/Create method to add a product to the cart
    ShoppingCart addProduct (int userId, int productId) throws SQLException;

    //  OPTIONAL - add a PUT (update) method to update an existing product in the cart
    void update(int userId, int productId, int quantity) throws SQLException;


    //  add a DELETE method to clear all products from the current users cart
    void delete(int userId, int productId) throws SQLException;

    //  function to clear cart for the user
    void clearCart(int userId) throws SQLException;

}
