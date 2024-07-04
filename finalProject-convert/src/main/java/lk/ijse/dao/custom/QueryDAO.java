package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.BatchEmployeeDTO;
import lk.ijse.entity.BatchEmployee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface QueryDAO extends SuperDAO {
    public List<BatchEmployee> getAll() throws SQLException, ClassNotFoundException;

    public String calculateNetTotalBatch(String batId) throws SQLException, ClassNotFoundException;

    public String calculateNetTotalOrd(String orderId) throws SQLException, ClassNotFoundException;
}
