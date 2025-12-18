package org.yearup.data.mysql;

import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        return null;
    }

    @Override
    public ShoppingCart addProduct(int userId, int productId) throws SQLException {
        return null;
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
