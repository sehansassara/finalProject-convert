package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.BatchIngredientDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.batchIngredientDTO;
import lk.ijse.entity.batchIngredient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchIngredientDAOImpl implements BatchIngredientDAO {

    @Override
    public String generateId() throws SQLException {
        return "";
    }

    public List<batchIngredient> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT BAT_ID, qty, ING_ID FROM batchIngredientDetail");
        List<batchIngredient> batchIngredientList = new ArrayList<>();
        while (resultSet.next()) {
            batchIngredient ba = new batchIngredient( resultSet.getString("BAT_ID"), Integer.parseInt(resultSet.getString("qty")),resultSet.getString("ING_ID"));
            batchIngredientList.add(ba);
        }
        return batchIngredientList;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    @Override
    public boolean save(batchIngredient entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(batchIngredient entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public batchIngredient searchById(String id) throws SQLException {
        return null;
    }

    public boolean save(List<batchIngredientDTO> bcList) throws SQLException, ClassNotFoundException {
        for (batchIngredientDTO bi : bcList) {
            boolean isSaved = SqlUtil.execute("INSERT INTO batchIngredientDetail VALUES(?, ?, ?)",bi.getBatId(),bi.getQty(),bi.getIngId());
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }
}
