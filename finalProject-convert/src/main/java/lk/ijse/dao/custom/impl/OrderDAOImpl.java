package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.OrderDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.OrderDTO;
import lk.ijse.entity.Order;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    public List<String> getIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT ORD_ID FROM orders WHERE status = 'ACTIVE'");
        List<String> idList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

    public String getCurrentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT ORD_ID FROM orders ORDER BY ORD_ID DESC LIMIT 1");
        if(resultSet.next()) {
            String orderId = resultSet.getString(1);
            return orderId;
        }
        return null;
    }

    public boolean savePlace(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO orders VALUES( ?,?,?,'ACTIVE')",orderDTO.getOrdId(),orderDTO.getCusId(),orderDTO.getDateOfPlace());
    }

    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM orders WHERE status = 'ACTIVE'");

        List<Order> orderList = new ArrayList<>();

        while (resultSet.next()) {
            Order order = new Order(  resultSet.getString(1), resultSet.getString(2), Date.valueOf(resultSet.getString(3)));
            orderList.add(order);
        }
        return orderList;
    }

    @Override
    public String generateId() throws SQLException {
        return "";
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    @Override
    public boolean save(Order entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Order entity) throws SQLException {
        return false;
    }

    @Override
    public Order searchById(String id) throws SQLException {
        return null;
    }
}
