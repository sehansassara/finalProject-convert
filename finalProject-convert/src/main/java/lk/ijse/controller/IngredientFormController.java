package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.IngredientBO;
import lk.ijse.bo.custom.impl.IngredientBOImpl;
import lk.ijse.controller.Util.Regex;
import lk.ijse.dto.IngredientDTO;
import lk.ijse.tm.IngredientTm;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.List;

public class IngredientFormController {

    @FXML
    private ChoiceBox<String> choiceType;

    @FXML
    private TableColumn<?, ?> colIngId;

    @FXML
    private TableColumn<?, ?> colQuantityOnHand;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<IngredientTm> tblIngredient;

    @FXML
    private TextField txtIngId;

    @FXML
    private TextField txtQuantityOnHand;

    @FXML
    private TextField txtUnitPrice;

    IngredientBO ingredientBO = (IngredientBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INGREDIENT);

    public void initialize() {
        setCellValueFactory();
        loadAllIngredient();
        generateNexrIngId();

        ObservableList<String> ingredientType = FXCollections.observableArrayList("Fish", "Salt","Chemicals","Tin");
        choiceType.setItems(ingredientType);
    }

    @SneakyThrows
    private void generateNexrIngId() {
      String id = ingredientBO.generateIngredientId();
      txtIngId.setText(id);
    }

    private void setCellValueFactory() {
        colIngId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantityOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
    }

    private void loadAllIngredient() {
        ObservableList<IngredientTm> obList = FXCollections.observableArrayList();
        try {
            List<IngredientDTO> ingredientDTOList = ingredientBO.getAllIngredient();
            for (IngredientDTO ingredientDTO : ingredientDTOList){
                IngredientTm tm = new IngredientTm(
                        ingredientDTO.getIngId(),
                        ingredientDTO.getType(),
                        ingredientDTO.getUnitPrice(),
                        ingredientDTO.getQtyOnHand()
                );
                obList.add(tm);
            }
            tblIngredient.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtIngId.setText("");
        txtUnitPrice.setText("");
        txtQuantityOnHand.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtIngId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter ingredient ID.").show();
            return;
        }

        try {
            if (ingredientBO.deleteIngredient(id)){
                new Alert(Alert.AlertType.CONFIRMATION,"ingredient is deleted").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllIngredient();
        clearFields();
        generateNexrIngId();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtIngId.getText();
        String type = (String) choiceType.getValue();

        double price = 0.0;
        if (!txtUnitPrice.getText().isEmpty()) {
            price = Double.parseDouble(txtUnitPrice.getText());
        }

        double qtyOnHand = Double.parseDouble(txtQuantityOnHand.getText());

        if (id.isEmpty() || type == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

    try {
        if (ingredientBO.saveIngredient(new IngredientDTO(id,type,price,qtyOnHand))) {
            new Alert(Alert.AlertType.CONFIRMATION, "ingredient is saved").show();
        }
    } catch (SQLException | ClassNotFoundException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }
        loadAllIngredient();
        clearFields();
        generateNexrIngId();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtIngId.getText();
        String type = (String) choiceType.getValue();

        double price = 0.0;
        if (!txtUnitPrice.getText().isEmpty()) {
            price = Double.parseDouble(txtUnitPrice.getText());
        }

        double qtyOnHand = Double.parseDouble(txtQuantityOnHand.getText());

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter ingredient ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (ingredientBO.updateIngredient(new IngredientDTO(id,type,price,qtyOnHand))) {
                new Alert(Alert.AlertType.CONFIRMATION, "ingredient is updated").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllIngredient();
        clearFields();
        generateNexrIngId();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtIngId.getText();

        try {
            IngredientDTO ingredientDTO = ingredientBO.searchByIngredientId(id);
            if (ingredientDTO != null){
                txtIngId.setText(ingredientDTO.getIngId());
                txtUnitPrice.setText(String.valueOf(ingredientDTO.getUnitPrice()));
                txtQuantityOnHand.setText(String.valueOf(ingredientDTO.getQtyOnHand()));
                choiceType.setValue(ingredientDTO.getType());
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void tblIngOnMouse(MouseEvent mouseEvent) {
        int index = tblIngredient.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String ingId = colIngId.getCellData(index).toString();
        String type = colType.getCellData(index).toString();
        String unitPrice = colUnitPrice.getCellData(index).toString();
        String qtyOnHand = colQuantityOnHand.getCellData(index).toString();

        txtIngId.setText(ingId);
        choiceType.setValue(type);
        txtUnitPrice.setText(unitPrice);
        txtQuantityOnHand.setText(qtyOnHand);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtIngId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtUnitPrice)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtQuantityOnHand)) return false;
        return true;
    }
    @FXML
    void txtIngIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtIngId);
    }

    @FXML
    void txtQuantityOnHandOnKeyReleased(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtQuantityOnHand);}

    @FXML
    void txtUnitPriceOnKeyReleased(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtUnitPrice);}
}
