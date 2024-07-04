package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.BatchCostBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.BatchDAO;
import lk.ijse.dao.custom.BatchIngredientDAO;
import lk.ijse.dao.custom.IngredientDAO;
import lk.ijse.dao.custom.QueryDAO;
import lk.ijse.dao.custom.impl.BatchDAOImpl;
import lk.ijse.dao.custom.impl.BatchIngredientDAOImpl;
import lk.ijse.dao.custom.impl.IngredientDAOImpl;
import lk.ijse.dao.custom.impl.QueryDAOImpl;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.BatchCostDTO;
import lk.ijse.dto.BatchDTO;
import lk.ijse.dto.IngredientDTO;
import lk.ijse.entity.Batch;
import lk.ijse.entity.Ingredient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BatchCostBOImpl implements BatchCostBO {

    IngredientDAO ingredientDAO = (IngredientDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.INGREDIENT);
    BatchDAO batchDAO = (BatchDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCH);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    public List<String> getIngredientIds() throws SQLException, ClassNotFoundException {
        return ingredientDAO.getIds();
    }

    public List<String> getBatchIds() throws SQLException, ClassNotFoundException {
        return batchDAO.getIds();
    }

    public BatchDTO searchByBatchId(String id) throws SQLException, ClassNotFoundException {
        Batch b = batchDAO.searchById(id);
        return new BatchDTO(b.getBatId(),b.getStoId(),b.getPrice(),b.getType(),b.getProductionDate(),b.getNumberOfReject(),b.getQty());
    }

    public IngredientDTO searchByIngredientId(String id) throws SQLException, ClassNotFoundException {
        Ingredient i = ingredientDAO.searchById(id);
        return new IngredientDTO(i.getIngId(),i.getType(),i.getUnitPrice(),i.getQtyOnHand());
    }

    public String calculateNetTotalBatch(String batId) throws SQLException, ClassNotFoundException {
        return queryDAO.calculateNetTotalBatch(batId);
    }

    public boolean placeCost(BatchCostDTO bc) throws SQLException {
        BatchIngredientDAO batchIngredientDAO = new BatchIngredientDAOImpl();
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isIngredientUpdate = ingredientDAO.updateCost(bc.getBcList());
            if (isIngredientUpdate) {
                boolean isBatchIngredientSave = batchIngredientDAO.save(bc.getBcList());
                if (isBatchIngredientSave) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
    }
    }
}
