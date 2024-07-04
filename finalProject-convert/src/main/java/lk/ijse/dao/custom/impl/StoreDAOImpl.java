package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.StoreDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.StoreDTO;
import lk.ijse.entity.Store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDAOImpl implements StoreDAO {

    public List<String> getIds() throws SQLException, ClassNotFoundException {
        List<String> idList = new ArrayList<>();
        ResultSet resultSet = SqlUtil.execute("SELECT STO_ID FROM store");
        while (resultSet.next()){
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

    public Store searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM store WHERE STO_ID = ?",id);
        if (resultSet.next()){
            Store store = new Store(resultSet.getString(1),resultSet.getInt(2),resultSet.getString(3));
            return store;
        }
        return null;
    }

    public List<Store> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM store WHERE status = 'ACTIVE'");
        List<Store> stores = new ArrayList<>();
        while (resultSet.next()){
            Store store = new Store( resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3));
            stores.add(store);
        }
        return stores;
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE store SET status = 'DELETE' WHERE STO_ID = ?",id);
    }

    public boolean save(Store entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO store VALUES(?,?,?,'ACTIVE')",entity.getStoId(),entity.getCapacity(),entity.getLocation());
    }

    public boolean update(Store entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE store SET capacity = ?, location = ? WHERE STO_ID = ?",entity.getCapacity(),entity.getLocation(),entity.getStoId());
    }

    @Override
    public String generateId() throws SQLException {
        return "";
    }
}
