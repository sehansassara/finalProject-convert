package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.IngredientDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.IngredientDTO;
import lk.ijse.dto.batchIngredientDTO;
import lk.ijse.entity.Ingredient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAOImpl implements IngredientDAO {

    public String generateId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT ING_ID FROM ingredient ORDER BY ING_ID DESC LIMIT 1");
        if (rst.next()) {
            String ingId = rst.getString(1);
            String prefix = "I";
            String[] split = ingId.split(prefix);
            int idNum = Integer.parseInt(split[1]);
            String nextId = prefix + String.format("%03d", ++idNum);
            return nextId;

        }
        return"I001";
    }

    public List<Ingredient> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM ingredient WHERE status = 'ACTIVE'");
        List<Ingredient> ingredientDTOList = new ArrayList<>();
        while (resultSet.next()){
            Ingredient ingredient = new Ingredient(resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4));
            ingredientDTOList.add(ingredient);
        }
        return ingredientDTOList;
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE ingredient SET status = 'DELETE' WHERE ING_ID = ?",id);
    }

    public boolean save(Ingredient entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO ingredient VALUES( ?,?,?,?,'ACTIVE')",entity.getIngId(),entity.getType(),entity.getUnitPrice(),entity.getQtyOnHand());
    }

    public boolean update(Ingredient entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE ingredient SET type = ?, unitPrice = ?, qtyOnHand = ? WHERE ING_ID = ?",entity.getType(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getIngId());
    }

    public Ingredient searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM ingredient WHERE ING_ID = ?",id);
        if (resultSet.next()){
            Ingredient ingredient = new Ingredient( resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4));
            return ingredient;
        }
        return null;
    }

    public List<String> getIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT ING_ID FROM ingredient WHERE status = 'ACTIVE'");
        List<String> idList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

    public boolean updateCost(List<batchIngredientDTO> bcList) throws SQLException, ClassNotFoundException {
        for (batchIngredientDTO bi : bcList) {
            boolean isUpdateQty = SqlUtil.execute("UPDATE ingredient SET qtyOnHand = qtyOnHand - ? WHERE ING_ID = ?",bi.getQty(),bi.getIngId());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }
}
