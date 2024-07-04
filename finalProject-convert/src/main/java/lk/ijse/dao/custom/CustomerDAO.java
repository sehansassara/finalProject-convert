package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer> {
    public List<String> getCon() throws SQLException, ClassNotFoundException;

    public Customer searchByTel(String tel) throws SQLException, ClassNotFoundException;
}
