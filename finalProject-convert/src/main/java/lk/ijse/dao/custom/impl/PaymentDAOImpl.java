package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.entity.Payment;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    public String generateId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT PAY_ID FROM payment ORDER BY PAY_ID DESC LIMIT 1");
        if (rst.next()) {
            String payId = rst.getString(1);
            String prefix = "P";
            String[] split = payId.split(prefix);
            int idNum = Integer.parseInt(split[1]);
            String nextId = prefix + String.format("%03d", ++idNum);
            return nextId;

        }
        return"P001";
    }

    public List<Payment> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM payment WHERE status = 'ACTIVE'");
        List<Payment> paymentList = new ArrayList<>();

        while (resultSet.next()){
            Payment payment = new Payment( resultSet.getString(1), resultSet.getDouble(2), Date.valueOf(resultSet.getString(3)), resultSet.getString(4), resultSet.getString(5));
            paymentList.add(payment);
        }
        return paymentList;
    }

    public boolean delete(String payId) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE payment SET status = 'DELETE' WHERE PAY_ID = ?",payId);
    }

    public boolean save(Payment entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO payment VALUES( ?,?,?,?,?,'ACTIVE')",entity.getPayId(),entity.getAmount(),entity.getDate(),entity.getType(),entity.getOrdId());
    }

    public boolean update(Payment entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE payment SET amount = ?, date = ?, type = ?, ORD_ID = ? WHERE PAY_ID = ?",entity.getAmount(),entity.getDate(),entity.getType(),entity.getOrdId(),entity.getPayId());
    }

    public Payment searchById(String payId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM payment WHERE PAY_ID = ?",payId);
        if (resultSet.next()){
            Payment payment = new Payment( resultSet.getString(1), resultSet.getDouble(2), Date.valueOf(resultSet.getString(3)), resultSet.getString(4), resultSet.getString(5));
             return payment;
        }
        return null;
    }
}
