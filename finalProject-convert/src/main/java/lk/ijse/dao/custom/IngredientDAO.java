package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.IngredientDTO;
import lk.ijse.dto.batchIngredientDTO;
import lk.ijse.entity.Ingredient;

import java.sql.SQLException;
import java.util.List;

public interface IngredientDAO extends CrudDAO<Ingredient> {
    public List<String> getIds() throws SQLException, ClassNotFoundException;

    public boolean updateCost(List<batchIngredientDTO> bcList) throws SQLException, ClassNotFoundException;

   // public boolean updateQty(String batId, int qty, String ingId) throws SQLException ;
}
