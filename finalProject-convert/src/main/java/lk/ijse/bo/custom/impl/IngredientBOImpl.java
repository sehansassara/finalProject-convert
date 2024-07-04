package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.IngredientBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.IngredientDAO;
import lk.ijse.dao.custom.impl.IngredientDAOImpl;
import lk.ijse.dto.IngredientDTO;
import lk.ijse.entity.Ingredient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientBOImpl implements IngredientBO {

    IngredientDAO ingredientDAO = (IngredientDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.INGREDIENT);
    @Override
    public String generateIngredientId() throws SQLException, ClassNotFoundException {
      return ingredientDAO.generateId();
    }

    public List<IngredientDTO> getAllIngredient() throws SQLException, ClassNotFoundException {
        List<IngredientDTO> list = new ArrayList<>();
        List<Ingredient> ingredients = ingredientDAO.getAll();
        for (Ingredient ingredient : ingredients) {
            list.add(new IngredientDTO(ingredient.getIngId(),ingredient.getType(),ingredient.getUnitPrice(),ingredient.getQtyOnHand()));
        }
        return list;
    }

    public boolean deleteIngredient(String id) throws SQLException, ClassNotFoundException {
        return ingredientDAO.delete(id);
    }

    public boolean saveIngredient(IngredientDTO ingredientDTO) throws SQLException, ClassNotFoundException {
        return ingredientDAO.save(new Ingredient(ingredientDTO.getIngId(),ingredientDTO.getType(),ingredientDTO.getUnitPrice(),ingredientDTO.getQtyOnHand()));
    }

    public boolean updateIngredient(IngredientDTO ingredientDTO) throws SQLException, ClassNotFoundException {
        return ingredientDAO.update(new Ingredient(ingredientDTO.getIngId(),ingredientDTO.getType(),ingredientDTO.getUnitPrice(),ingredientDTO.getQtyOnHand()));
    }

    public IngredientDTO searchByIngredientId(String id) throws SQLException, ClassNotFoundException {
        Ingredient i = ingredientDAO.searchById(id);
        return new IngredientDTO(i.getIngId(),i.getType(),i.getUnitPrice(),i.getQtyOnHand());
    }

    public List<String> getIds() throws SQLException, ClassNotFoundException {
        return ingredientDAO.getIds();
    }
}
