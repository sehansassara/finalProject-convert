package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.OrderDTO;
import lk.ijse.entity.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAO extends CrudDAO<Order> {
    public boolean savePlace(OrderDTO orderDTO) throws SQLException, ClassNotFoundException ;

        public List<String> getIds() throws SQLException, ClassNotFoundException;

    public String getCurrentId() throws SQLException, ClassNotFoundException;
}
