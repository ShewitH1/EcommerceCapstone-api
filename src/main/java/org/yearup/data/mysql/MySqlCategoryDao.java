package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {

        List<Category> categories = new ArrayList<>();

        String query = "select * from categories";

        // get all categories
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()){
                int categoryid = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");

                Category category = new Category(categoryid, name, description);

                categories.add(category);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) throws SQLException {

        String query = """
                select * from categories where category_id = ?
                """;

        // get category by id
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ) {

            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int category_id = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    Category category = new Category(category_id, name, description);

                    return category;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public Category create(Category category) throws SQLException
    {

        String query = """
                insert into categories(name, description) values (?, ?);
                """;

        // create a new category
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ){

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rows = statement.executeUpdate();

            if(rows > 0){
                try(ResultSet resultSet = statement.getGeneratedKeys()){
                    if(resultSet.next()){
                        int category_id = resultSet.getInt(1);
                        category.setCategoryId(category_id);
                        return category;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category) throws SQLException
    {
        //query
        String query = """
                update categories set name = ?, description = ? where category_id = ?;
                """;

        // update category
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();

        }

    }

    @Override
    public void delete(int categoryId) throws SQLException
    {
        // query
        String query = "delete from categories where category_id = ?";

        // delete category
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
