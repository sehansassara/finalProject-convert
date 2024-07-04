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
import lk.ijse.bo.custom.StoreBO;
import lk.ijse.bo.custom.impl.StoreBOImpl;
import lk.ijse.controller.Util.Regex;
import lk.ijse.dto.StoreDTO;
import lk.ijse.tm.StoreTm;

import java.sql.SQLException;
import java.util.List;

public class StoreFormController {
    @FXML
    private ChoiceBox<String> choiceStore;
    @FXML
    private TableColumn<?, ?> colCapacity;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colStoId;

    @FXML
    private TableView<StoreTm> tblStore;

    @FXML
    private TextField txtCapacity;

    @FXML
    private TextField txtLocation;

    StoreBO storeBO = (StoreBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STORE);

    public void initialize() {
        setCellValueFactory();
        ObservableList<String> stores = FXCollections.observableArrayList("ST001", "ST002","ST003","ST004");
        choiceStore.setItems(stores);
        loadAllStore();
    }

    private void setCellValueFactory() {
        colStoId.setCellValueFactory(new PropertyValueFactory<>("stoId"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
    }

    private void loadAllStore() {
        ObservableList<StoreTm> obList = FXCollections.observableArrayList();

        try {
            List<StoreDTO> storeDTOList = storeBO.getAllStore();
            for (StoreDTO storeDTO : storeDTOList){
                StoreTm tm = new StoreTm(
                        storeDTO.getStoId(),
                        storeDTO.getCapacity(),
                        storeDTO.getLocation()
                );
                obList.add(tm);
            }
            tblStore.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        choiceStore.setValue(null);
        txtCapacity.setText("");
        txtLocation.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = choiceStore.getValue();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter store ID.").show();
            return;
        }

        try {
            if (storeBO.deleteStore(id)){
                new Alert(Alert.AlertType.CONFIRMATION,"store is deleted").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllStore();
        clearFields();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = choiceStore.getValue();

        int capacity = 0;
        if (!txtCapacity.getText().isEmpty()) {
            capacity = Integer.parseInt(txtCapacity.getText());
        }

        String location = txtLocation.getText();

        if (id.isEmpty() || location.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

    try {
        if (storeBO.saveStore(new StoreDTO(id,capacity,location))) {
            new Alert(Alert.AlertType.CONFIRMATION, "store is saved").show();
        }
    } catch (SQLException | ClassNotFoundException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }
        loadAllStore();
        clearFields();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = choiceStore.getValue();

        int capacity = 0;
        if (!txtCapacity.getText().isEmpty()) {
            capacity = Integer.parseInt(txtCapacity.getText());
        }

        String location = txtLocation.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter store ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

    try {
        if (storeBO.updateStore(new StoreDTO(id,capacity,location))) {
            new Alert(Alert.AlertType.CONFIRMATION, "store is updated").show();
        }
    } catch (SQLException | ClassNotFoundException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }
        loadAllStore();
        clearFields();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = choiceStore.getValue();

        try {
            StoreDTO storeDTO = storeBO.searchByStoreId(id);
            if (storeDTO != null){
                choiceStore.setValue(storeDTO.getStoId());
                txtCapacity.setText(String.valueOf(storeDTO.getCapacity()));
                txtLocation.setText(storeDTO.getLocation());
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.INFORMATION,"store is not found !").show();
        }
    }

    public void tblStoreOnMouse(MouseEvent mouseEvent) {
        int index = tblStore.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String stoId = colStoId.getCellData(index).toString();
        String capacity = colCapacity.getCellData(index).toString();
        String location = colLocation.getCellData(index).toString();

        choiceStore.setValue(stoId);
        txtCapacity.setText(capacity);
        txtLocation.setText(location);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtCapacity)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtLocation)) return false;
        return true;
    }

    @FXML
    void txtCapacityOnKeyReleased(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtCapacity);}

    @FXML
    void txtLocationKeyReleasedOnAction(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtLocation);}
}
