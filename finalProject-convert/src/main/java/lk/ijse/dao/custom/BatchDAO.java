package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.BatchDTO;
import lk.ijse.dto.OrderDetailDTO;
import lk.ijse.entity.Batch;
import lk.ijse.entity.OrderDetail;

import java.sql.SQLException;
import java.util.List;

public interface BatchDAO extends CrudDAO<Batch> {
        public List<String> getIds() throws SQLException, ClassNotFoundException;

        public boolean updatePlace(List<OrderDetailDTO> odList) throws SQLException, ClassNotFoundException;

       // public boolean updateQty(String batId, int qty) throws SQLException ;

}
