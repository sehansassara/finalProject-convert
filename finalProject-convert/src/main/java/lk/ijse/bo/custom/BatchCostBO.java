package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.BatchCostDTO;
import lk.ijse.dto.BatchDTO;
import lk.ijse.dto.IngredientDTO;

import java.sql.SQLException;
import java.util.List;

public interface BatchCostBO extends SuperBO {
    public List<String> getIngredientIds() throws SQLException, ClassNotFoundException ;

    public List<String> getBatchIds() throws SQLException, ClassNotFoundException ;

    public BatchDTO searchByBatchId(String id) throws SQLException, ClassNotFoundException ;

    public IngredientDTO searchByIngredientId(String id) throws SQLException, ClassNotFoundException ;

    public boolean placeCost(BatchCostDTO bc) throws SQLException ;
    public String calculateNetTotalBatch(String batId) throws SQLException, ClassNotFoundException ;
}
