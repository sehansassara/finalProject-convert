package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.BatchIngredientBO;
import lk.ijse.bo.custom.impl.BatchIngredientBOImpl;
import lk.ijse.dto.batchIngredientDTO;
import lk.ijse.tm.batchIngredientTM;

import java.sql.SQLException;
import java.util.List;

public class BatchIngredientDetailFormController {

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colIngId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableView<batchIngredientTM> tblBatchIng;

    @FXML
    private TextField txtBatId;

    @FXML
    private TextField txtIngId;

    @FXML
    private TextField txtQty;

    BatchIngredientBO batchIngredientBO = (BatchIngredientBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BATCHINGREDIENT);

    public void initialize() {
        setCellValueFactory();
        loadAllBatchIng();
    }

    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colIngId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
    }

    private void loadAllBatchIng() {
        ObservableList<batchIngredientTM> batIng = FXCollections.observableArrayList();
        try {
            List<batchIngredientDTO> batIng1 = batchIngredientBO.getAllBatchIngredient();
            for (batchIngredientDTO batchIngredientDTO : batIng1){
                batchIngredientTM tmt = new batchIngredientTM(
                        batchIngredientDTO.getBatId(),
                        batchIngredientDTO.getIngId(),
                        batchIngredientDTO.getQty()
                );
                batIng.add(tmt);
            }
            tblBatchIng.setItems(batIng);
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);

        }
    }

}
