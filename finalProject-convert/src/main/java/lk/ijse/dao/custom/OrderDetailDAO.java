package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.OrderDetailDTO;
import lk.ijse.entity.OrderDetail;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetail> {
   // public List<OrderDetailDTO> getAll() throws SQLException, ClassNotFoundException;

    public boolean save(List<OrderDetailDTO> odList) throws SQLException, ClassNotFoundException;

   // public boolean savePl(OrderDetailDTO od) throws SQLException ;
}
