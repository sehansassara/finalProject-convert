package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    public String generateId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT CUS_ID FROM customer ORDER BY CUS_ID DESC LIMIT 1");
        if (rst.next()) {
            String cusId = rst.getString(1);
            String prefix = "C";
            String[] split = cusId.split(prefix); // Split using the prefix
            int idNum = Integer.parseInt(split[1]);
            String nextId = prefix + String.format("%03d", ++idNum);
            return nextId;

        }
        return"C001";
    }

    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM customer WHERE  status = 'ACTIVE'");
        List<Customer> cusList = new ArrayList<>();
        while (resultSet.next()){
            Customer customer = new Customer(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
            cusList.add(customer);
        }
        return cusList;
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute( "UPDATE customer SET status = 'DELETE' WHERE CUS_ID = ?",id);
    }

    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO customer VALUES(?,?,?,?,'ACTIVE')",entity.getId(),entity.getName(),entity.getTel(),entity.getAddress());
    }

    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE customer SET name = ? , tel = ? , address = ?  WHERE CUS_ID = ?",entity.getName(),entity.getTel(),entity.getAddress(),entity.getId());
    }

    public Customer searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM customer WHERE CUS_ID = ?",id);
        if (resultSet.next()){
            Customer customer = new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
            return customer;
        }
        return null;
    }

    public List<String> getCon() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT tel FROM customer WHERE status = 'ACTIVE'");
        List<String> telList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            telList.add(id);
        }
        return telList;
    }

    public Customer searchByTel(String tel) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM customer WHERE tel = ?",tel);
        if (resultSet.next()) {
            Customer customer = new Customer( resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
            return customer;
        }
        return null;
    }
}
