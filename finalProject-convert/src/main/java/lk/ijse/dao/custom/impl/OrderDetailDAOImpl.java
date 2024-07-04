package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.OrderDetailDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.OrderDetailDTO;
import lk.ijse.entity.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public String generateId() throws SQLException, ClassNotFoundException {
        return "";
    }

    public List<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT ORD_ID, BAT_ID, qty FROM orderDetail");
        List<OrderDetail> orderDetailDTOList = new ArrayList<>();

        while (resultSet.next()) {
            OrderDetail ba = new OrderDetail( resultSet.getString("ORD_ID"), resultSet.getString("BAT_ID"), Integer.parseInt(resultSet.getString("qty")));
            orderDetailDTOList.add(ba);
        }
        return orderDetailDTOList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean save(OrderDetail batchDTO) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(OrderDetail batchDTO) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetail searchById(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    public boolean save(List<OrderDetailDTO> odList) throws SQLException, ClassNotFoundException {
        for (OrderDetailDTO od : odList) {
            boolean isSaved = SqlUtil.execute("INSERT INTO orderDetail VALUES(?, ?, ?)",od.getOrdId(),od.getBatId(),od.getQty());
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

   /* public boolean savePl(OrderDetailDTO od) throws SQLException {
        String sql = "INSERT INTO orderDetail VALUES(?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, od.getOrdId());
        pstm.setString(2, od.getBatId());
        pstm.setInt(3, od.getQty());

        return pstm.executeUpdate() > 0;
    }*/
}
