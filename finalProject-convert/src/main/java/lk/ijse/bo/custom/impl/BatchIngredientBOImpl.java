package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.BatchIngredientBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.BatchIngredientDAO;
import lk.ijse.dao.custom.impl.BatchIngredientDAOImpl;
import lk.ijse.dto.batchIngredientDTO;
import lk.ijse.entity.batchIngredient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchIngredientBOImpl implements BatchIngredientBO {

    BatchIngredientDAO batchIngredientDAO = (BatchIngredientDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCHINGREDIENT);
    @Override
    public List<batchIngredientDTO> getAllBatchIngredient() throws SQLException, ClassNotFoundException {
       List<batchIngredientDTO> batchIngredientDTOS = new ArrayList<>();
       List<batchIngredient> batchIngredients = batchIngredientDAO.getAll();
       for (batchIngredient batchIngredient : batchIngredients) {
           batchIngredientDTOS.add(new batchIngredientDTO(batchIngredient.getBatId(),batchIngredient.getQty(),batchIngredient.getIngId()));
       }
       return batchIngredientDTOS;
    }
}
