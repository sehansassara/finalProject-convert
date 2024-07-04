package lk.ijse.bo.custom;

import lk.ijse.dto.BatchDTO;

import java.sql.SQLException;
import java.util.List;

public interface BatchBO extends StoreBO{
    public String generateBatchId() throws SQLException, ClassNotFoundException ;

    public List<BatchDTO> getAllBatch() throws SQLException, ClassNotFoundException ;

    public boolean deleteBatch(String id) throws SQLException, ClassNotFoundException ;

    public boolean saveBatch(BatchDTO dto) throws SQLException, ClassNotFoundException ;

    public boolean updateBatch(BatchDTO dto) throws SQLException, ClassNotFoundException ;

    public BatchDTO searchByBatchId(String id) throws SQLException, ClassNotFoundException ;
}
