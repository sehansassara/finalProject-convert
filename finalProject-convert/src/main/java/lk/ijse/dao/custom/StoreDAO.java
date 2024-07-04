package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.StoreDTO;
import lk.ijse.entity.Store;

import java.sql.SQLException;
import java.util.List;

public interface StoreDAO extends CrudDAO<Store> {
    public List<String> getIds() throws SQLException, ClassNotFoundException;
}
