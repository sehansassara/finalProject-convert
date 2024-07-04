package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.DashBoardDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.IngredientDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBordDAOImpl implements DashBoardDAO {

    public int getCustomerCount() throws SQLException, ClassNotFoundException {
        /*String sql = "SELECT COUNT(*) AS customer_count FROM customer  WHERE status = 'ACTIVE'";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);*/
        ResultSet resultSet = SqlUtil.execute( "SELECT COUNT(*) AS customer_count FROM customer  WHERE status = 'ACTIVE'");
        if(resultSet.next()) {
            return resultSet.getInt("customer_count");
        }
        return 0;
    }

    public int getEmployeeCount() throws SQLException, ClassNotFoundException {
        /*String sql = "SELECT COUNT(*) AS employee_count FROM employee WHERE status = 'ACTIVE'";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);*/
        ResultSet resultSet = SqlUtil.execute("SELECT COUNT(*) AS employee_count FROM employee WHERE status = 'ACTIVE'");
        if(resultSet.next()) {
            return resultSet.getInt("employee_count");
        }
        return 0;
    }

    public int getOrderCount() throws SQLException, ClassNotFoundException {
        /*String sql = "SELECT COUNT(*) AS order_count FROM orders  WHERE status = 'ACTIVE'";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);*/
        ResultSet resultSet = SqlUtil.execute("SELECT COUNT(*) AS order_count FROM orders  WHERE status = 'ACTIVE'");
        if(resultSet.next()) {
            return resultSet.getInt("order_count");
        }
        return 0;
    }

    public int getSupplierCount() throws SQLException, ClassNotFoundException {
       /* String sql = "SELECT COUNT(*) AS supplier_count FROM supplier  WHERE status = 'ACTIVE'";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);*/
        ResultSet resultSet = SqlUtil.execute("SELECT COUNT(*) AS supplier_count FROM supplier  WHERE status = 'ACTIVE'");
        if(resultSet.next()) {
            return resultSet.getInt("supplier_count");
        }
        return 0;
    }

    public Map<String, Double> getPaymentsByDay() throws SQLException, ClassNotFoundException {
        Map<String, Double> paymentsByDay = new HashMap<>();

        /*String sql = "SELECT date, SUM(amount) AS total_amount FROM payment GROUP BY date";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);*/
             ResultSet resultSet = SqlUtil.execute( "SELECT date, SUM(amount) AS total_amount FROM payment GROUP BY date");

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                double totalAmount = resultSet.getDouble("total_amount");
                paymentsByDay.put(date, totalAmount);
            }

        return paymentsByDay;
    }

    public List<IngredientDTO> getAllIng() throws SQLException, ClassNotFoundException {
       /* String sql = "SELECT * FROM ingredient WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);*/

        ResultSet resultSet = SqlUtil.execute("SELECT * FROM ingredient WHERE status = 'ACTIVE'");

        List<IngredientDTO> ingredientDTOList = new ArrayList<>();

        while (resultSet.next()){
            IngredientDTO ingredientDTO = new IngredientDTO(resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4));
            ingredientDTOList.add(ingredientDTO);
        }
        return ingredientDTOList;
    }

    public Map<String, Integer> getOrdersByDay() throws SQLException, ClassNotFoundException {
        Map<String, Integer> ordersByDay = new HashMap<>();

        /*String sql = "SELECT dateOfPlace, COUNT(*) AS order_count FROM orders GROUP BY dateOfPlace";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);*/
             ResultSet resultSet = SqlUtil.execute( "SELECT dateOfPlace, COUNT(*) AS order_count FROM orders GROUP BY dateOfPlace");

            while (resultSet.next()) {
                String orderDay = resultSet.getString("dateOfPlace");
                int orderCount = resultSet.getInt("order_count");
                ordersByDay.put(orderDay, orderCount);
            }
        return ordersByDay;
    }
}
