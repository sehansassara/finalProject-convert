package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.batchIngredientDTO;

import java.sql.SQLException;
import java.util.List;

public interface BatchIngredientBO extends SuperBO {
    public List<batchIngredientDTO> getAllBatchIngredient() throws SQLException, ClassNotFoundException ;
}
