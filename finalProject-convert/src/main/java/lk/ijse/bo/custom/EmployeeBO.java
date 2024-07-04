package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.EmployeeDTO;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeBO extends SuperBO {
    public List<EmployeeDTO> getAllEmployee() throws SQLException, ClassNotFoundException ;

    public String generateEmployeeId() throws SQLException, ClassNotFoundException ;

    public boolean deleteEmployee(String id) throws SQLException, ClassNotFoundException ;

    public boolean saveEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException ;

    public boolean updateEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException ;

    public EmployeeDTO searchByEmployeeId(String id) throws SQLException, ClassNotFoundException ;
}
