package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.BatchEmployeeDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.BatchEmployeeDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BatchEmployeeDetailDAOImpl implements BatchEmployeeDAO {

    @Override
    public String generateId() throws SQLException {
        return "";
    }

    @Override
    public List<BatchEmployeeDTO> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    public boolean save(BatchEmployeeDTO batchEmployeeDTO) throws SQLException, ClassNotFoundException {
     /*   String sql = "INSERT INTO batchEmployeeDetail  VALUES (?,?)";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, batchEmployeeDTO.getBatId());
        pstm.setObject(2, batchEmployeeDTO.getEmpId());

        return pstm.executeUpdate() > 0;*/

        return SqlUtil.execute("INSERT INTO batchEmployeeDetail  VALUES (?,?)",batchEmployeeDTO.getBatId(),batchEmployeeDTO.getEmpId());
    }

    @Override
    public boolean update(BatchEmployeeDTO batchDTO) throws SQLException {
        return false;
    }

    @Override
    public BatchEmployeeDTO searchById(String id) throws SQLException {
        return null;
    }
}
