package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.BatchDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.BatchDTO;
import lk.ijse.dto.OrderDetailDTO;
import lk.ijse.entity.Batch;
import lk.ijse.entity.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchDAOImpl implements BatchDAO {

    public String generateId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT BAT_ID FROM batch ORDER BY BAT_ID DESC LIMIT 1");
        if (rst.next()) {
            String batchId = rst.getString(1);
            String prefix = "B";
            String[] split = batchId.split(prefix); // Split using the prefix
            int idNum = Integer.parseInt(split[1]);
            String nextId = prefix + String.format("%03d", ++idNum);
            return nextId;

        }
        return"B001";
        }

    public List<Batch> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM batch WHERE status = 'ACTIVE'");
        List<Batch> batcList = new ArrayList<>();
        while (resultSet.next()){
            Batch batch = new Batch( resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getString(4), resultSet.getDate(5), resultSet.getInt(6), resultSet.getInt(7));
            batcList.add(batch);
        }
        return batcList;
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE batch SET status = 'DELETE' WHERE BAT_ID = ?",id);
    }

    public boolean save(Batch entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO batch VALUES (?,?,?,?,?,?,?,'ACTIVE')",entity.getBatId(),entity.getStoId(),entity.getPrice(),entity.getType(),entity.getProductionDate(),entity.getNumberOfReject(),entity.getQty());
    }

    public boolean update(Batch entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE batch SET STO_ID = ?, price = ?, type = ?, productionDate = ?, numberOfRejectedItem = ?, qty = ? WHERE BAT_ID = ?",entity.getStoId(),entity.getPrice(),entity.getType(),entity.getProductionDate(),entity.getNumberOfReject(),entity.getQty(),entity.getBatId());
    }

    public Batch searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM batch WHERE BAT_ID = ?",id);
        if (resultSet.next()){
            Batch batch = new Batch(  resultSet.getString(1), resultSet.getString(2), Double.parseDouble(resultSet.getString(3)), resultSet.getString(4), resultSet.getDate(5), resultSet.getInt(6), resultSet.getInt(7));
            return batch;
        }
        return null;
    }

    public List<String> getIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT BAT_ID FROM batch WHERE status = 'ACTIVE'");
        List<String> batchList = new ArrayList<>();
        while (resultSet.next()) {
            batchList.add(resultSet.getString(1));
        }
        return batchList;
    }

    public boolean updatePlace(List<OrderDetailDTO> odList) throws SQLException, ClassNotFoundException {
        for (OrderDetailDTO od : odList) {
            boolean isUpdateQty = SqlUtil.execute("UPDATE batch SET qty = qty - ? WHERE BAT_ID = ?",od.getQty(),od.getBatId());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }
}
