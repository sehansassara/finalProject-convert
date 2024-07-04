package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.SupplierDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.SupplierDTO;
import lk.ijse.entity.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {

    public String generateId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT SUP_ID FROM supplier ORDER BY SUP_ID DESC LIMIT 1");
        if (rst.next()) {
            String supId = rst.getString(1);
            String prefix = "S";
            String[] split = supId.split(prefix);
            int idNum = Integer.parseInt(split[1]);
            String nextId = prefix + String.format("%03d", ++idNum);
            return nextId;

        }
        return"S001";
    }

    public List<Supplier> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM supplier WHERE status = 'ACTIVE'");

        List<Supplier> suppliers = new ArrayList<>();

        while (resultSet.next()){
            Supplier supplier = new Supplier( resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            suppliers.add(supplier);
        }
        return suppliers;
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE supplier SET status = 'DELETE' WHERE SUP_ID = ?",id);
    }

    public boolean save(Supplier entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO supplier VALUES (?,?,?,?,?,'ACTIVE')",entity.getSupId(),entity.getCompanyName(),entity.getAddress(),entity.getContact(),entity.getIngId());
    }

    public boolean update(Supplier entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE supplier SET companyName = ?, address = ?, contact = ?, ING_ID = ? WHERE SUP_ID = ?",entity.getCompanyName(),entity.getAddress(),entity.getContact(),entity.getIngId(),entity.getSupId());
    }

    public Supplier searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute( "SELECT * FROM supplier WHERE SUP_ID = ?",id);
        if (resultSet.next()){
            Supplier supplier = new Supplier( resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            return supplier;
        }
        return null;
    }
}
