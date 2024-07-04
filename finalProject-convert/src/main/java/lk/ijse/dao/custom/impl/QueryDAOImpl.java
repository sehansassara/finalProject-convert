package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.QueryDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.BatchEmployeeDTO;
import lk.ijse.entity.BatchEmployee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    public List<BatchEmployee> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * " +
                "FROM batch b " +
                "INNER JOIN batchEmployeeDetail be ON b.BAT_ID = be.BAT_ID " +
                "INNER JOIN employee e ON be.EMP_ID = e.EMP_ID " +
                "ORDER BY b.BAT_ID ASC");

        List<BatchEmployee> batchEmpList = new ArrayList<>();

        while (resultSet.next()) {
            BatchEmployee batchEmp = new BatchEmployee( resultSet.getString("BAT_ID"), resultSet.getString("EMP_ID"));
            batchEmpList.add(batchEmp);
        }
        return batchEmpList;
    }

    public String calculateNetTotalBatch(String batId) throws SQLException, ClassNotFoundException {
        double netTotal = 0.0;
            ResultSet resultSet = SqlUtil.execute("SELECT SUM(bid.qty * i.unitPrice) AS batch_cost FROM batchIngredientDetail bid JOIN ingredient i ON bid.ING_ID = i.ING_ID JOIN batch b ON bid.BAT_ID = b.BAT_ID WHERE b.BAT_ID = ?",batId);
                if (resultSet.next()) {
                    netTotal = resultSet.getDouble("batch_cost");
                }
                return String.valueOf(netTotal);
    }

    public String calculateNetTotalOrd(String orderId) throws SQLException, ClassNotFoundException {
        double netTotal = 0.0;
            ResultSet resultSet = SqlUtil.execute( "SELECT SUM(b.price * od.qty) " +
                    "FROM batch b " +
                    "JOIN orderDetail od ON b.BAT_ID = od.BAT_ID " +
                    "WHERE od.ORD_ID = ?",orderId);

                while (resultSet.next()) {
                    double c = resultSet.getDouble(1);
                    netTotal=netTotal+c;
                }
                return String.valueOf((netTotal));
    }
}
