package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.BatchBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.BatchDAO;
import lk.ijse.dao.custom.impl.BatchDAOImpl;
import lk.ijse.dto.BatchDTO;
import lk.ijse.entity.Batch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchBOImpl implements BatchBO {

    BatchDAO batchDAO = (BatchDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCH);

    public String generateBatchId() throws SQLException, ClassNotFoundException {
      return batchDAO.generateId();
    }

    public List<BatchDTO> getAllBatch() throws SQLException, ClassNotFoundException {
      List<BatchDTO> batchDTOS = new ArrayList<>();
      List<Batch> batchList = batchDAO.getAll();
      for (Batch batch : batchList) {
          batchDTOS.add(new BatchDTO(batch.getBatId(),batch.getStoId(),batch.getPrice(),batch.getType(),batch.getProductionDate(),batch.getNumberOfReject(),batch.getQty()));
      }
      return batchDTOS;
    }

    public boolean deleteBatch(String id) throws SQLException, ClassNotFoundException {
        return batchDAO.delete(id);
    }

    public boolean saveBatch(BatchDTO dto) throws SQLException, ClassNotFoundException {
        return batchDAO.save(new Batch(dto.getBatId(),dto.getStoId(),dto.getPrice(),dto.getType(),dto.getProductionDate(),dto.getNumberOfReject(),dto.getQty()));
    }

    public boolean updateBatch(BatchDTO dto) throws SQLException, ClassNotFoundException {
        return batchDAO.update(new Batch(dto.getBatId(),dto.getStoId(),dto.getPrice(),dto.getType(),dto.getProductionDate(),dto.getNumberOfReject(),dto.getQty()));
    }

    public BatchDTO searchByBatchId(String id) throws SQLException, ClassNotFoundException {
       Batch b = batchDAO.searchById(id);
       return new BatchDTO(b.getBatId(),b.getStoId(),b.getPrice(),b.getType(),b.getProductionDate(),b.getNumberOfReject(),b.getQty());
    }
}
