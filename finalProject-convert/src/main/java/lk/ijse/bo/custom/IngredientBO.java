package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.IngredientDTO;

import java.sql.SQLException;
import java.util.List;

public interface IngredientBO extends SuperBO {
    public String generateIngredientId() throws SQLException, ClassNotFoundException ;

    public List<IngredientDTO> getAllIngredient() throws SQLException, ClassNotFoundException ;

    public boolean deleteIngredient(String id) throws SQLException, ClassNotFoundException ;

    public boolean saveIngredient(IngredientDTO ingredientDTO) throws SQLException, ClassNotFoundException ;

    public boolean updateIngredient(IngredientDTO ingredientDTO) throws SQLException, ClassNotFoundException ;

    public IngredientDTO searchByIngredientId(String id) throws SQLException, ClassNotFoundException ;

    public List<String> getIds() throws SQLException, ClassNotFoundException ;
    }
