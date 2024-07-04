package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.SupplierBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.OrderDAO;
import lk.ijse.dao.custom.SupplierDAO;
import lk.ijse.dao.custom.impl.SupplierDAOImpl;
import lk.ijse.dto.SupplierDTO;
import lk.ijse.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierBOImpl implements SupplierBO {

    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.SUPPLIE);

    public String generateSupplierId() throws SQLException, ClassNotFoundException {
        return supplierDAO.generateId();
    }

    public List<SupplierDTO> getAllSupplier() throws SQLException, ClassNotFoundException {
        List<SupplierDTO> dtos = new ArrayList<>();
        List<Supplier> suppliers = supplierDAO.getAll();
        for (Supplier supplier : suppliers) {
            dtos.add(new SupplierDTO(supplier.getSupId(),supplier.getCompanyName(),supplier.getAddress(),supplier.getContact(),supplier.getIngId()));
        }
        return dtos;
    }

    public boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException {
        return supplierDAO.delete(id);
    }

    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException {
        return supplierDAO.save(new Supplier(supplierDTO.getSupId(),supplierDTO.getCompanyName(),supplierDTO.getAddress(),supplierDTO.getContact(),supplierDTO.getIngId()));
    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException {
        return supplierDAO.update(new Supplier(supplierDTO.getSupId(),supplierDTO.getCompanyName(),supplierDTO.getAddress(),supplierDTO.getContact(),supplierDTO.getIngId()));
    }

    public SupplierDTO searchBySupplierId(String id) throws SQLException, ClassNotFoundException {
      Supplier s = supplierDAO.searchById(id);
      return new SupplierDTO(s.getSupId(),s.getCompanyName(),s.getAddress(),s.getContact(),s.getIngId());
    }
}
