package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {


    private ProductDao productDao;


    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    //  how could i use principal here
    @Override
    public ShoppingCart getByUserId(int userId) {


        ShoppingCart shoppingCart = new ShoppingCart();

        //  initiaze query
        String query = """
            select product_id, quantity
            from shopping_cart
            where user_id = ?
    """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            //  use result-set here

            //  logic here
            while (resultSet.next()) {
                //  user_id, product_id, quantity
//                   int user_Id = resultSet.getInt("user_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                Product product = productDao.getById(product_id);

                ShoppingCartItem shoppingCartItem = new ShoppingCartItem(product_id, quantity);
                shoppingCart.add(shoppingCartItem);

                shoppingCartItem.setProduct(product);
                shoppingCartItem.setQuantity(quantity);

                shoppingCart.add(shoppingCartItem);


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return shoppingCart;
    }

    @Override
    public ShoppingCart addProduct(int userId, int productId) throws SQLException {
        String sql = """
            INSERT INTO shopping_cart (user_id, product_id, quantity)
            VALUES (?, ?, 1)
            ON DUPLICATE KEY UPDATE quantity = quantity + 1
        """;

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            statement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }

        return getByUserId(userId);
    }

    @Override
    public void update(int userId, int productId, int quantity) throws SQLException {

    }

    @Override
    public void delete(int userId, int productId) throws SQLException {

    }

    @Override
    public void clearCart(int userId) throws SQLException {

    }
}
