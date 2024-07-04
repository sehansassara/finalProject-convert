package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.BatchEmployeeDTO;

import java.sql.SQLException;
import java.util.List;

public interface QueryBO extends SuperBO {
    public List<BatchEmployeeDTO> getAll() throws SQLException, ClassNotFoundException ;
}
