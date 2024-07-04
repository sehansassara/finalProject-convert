package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.SupplierDTO;

import java.sql.SQLException;
import java.util.List;

public interface SupplierBO extends SuperBO {
    public String generateSupplierId() throws SQLException, ClassNotFoundException ;

    public List<SupplierDTO> getAllSupplier() throws SQLException, ClassNotFoundException ;

    public boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException ;

    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException ;

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException ;

    public SupplierDTO searchBySupplierId(String id) throws SQLException, ClassNotFoundException ;
}
