package lk.ijse.dao;

import lk.ijse.dto.BatchDTO;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO{
    public  String generateId() throws SQLException, ClassNotFoundException;

    public List<T> getAll() throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public boolean save(T batchDTO) throws SQLException, ClassNotFoundException;

    public boolean update(T batchDTO) throws SQLException, ClassNotFoundException;

    public T searchById(String id) throws SQLException, ClassNotFoundException;

}
