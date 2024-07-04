package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.batchIngredientDTO;
import lk.ijse.entity.batchIngredient;

import java.sql.SQLException;
import java.util.List;

public interface BatchIngredientDAO extends CrudDAO<batchIngredient> {
    public boolean save(List<batchIngredientDTO> bcList) throws SQLException, ClassNotFoundException;
  //  public boolean saveBI(batchIngredientDTO bi) throws SQLException ;
}
