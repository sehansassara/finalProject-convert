package lk.ijse.dao.custom;

import lk.ijse.dto.IngredientDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DashBoardDAO {
    public int getCustomerCount() throws SQLException, ClassNotFoundException;

    public int getEmployeeCount() throws SQLException, ClassNotFoundException;

    public int getOrderCount() throws SQLException, ClassNotFoundException;

    public int getSupplierCount() throws SQLException, ClassNotFoundException;

    public Map<String, Double> getPaymentsByDay() throws SQLException, ClassNotFoundException;

    public List<IngredientDTO> getAllIng() throws SQLException, ClassNotFoundException;

    public Map<String, Integer> getOrdersByDay() throws SQLException, ClassNotFoundException;
}
