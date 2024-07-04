package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.BatchBO;
import lk.ijse.bo.custom.EmployeeBO;
import lk.ijse.bo.custom.StoreBO;
import lk.ijse.bo.custom.impl.BatchBOImpl;
import lk.ijse.bo.custom.impl.EmployeeBOImpl;
import lk.ijse.bo.custom.impl.StoreBOImpl;
import lk.ijse.controller.Util.Regex;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.*;
import lk.ijse.dao.custom.impl.BatchEmployeeDetailDAOImpl;
import lk.ijse.dao.custom.impl.StoreDAOImpl;
import lk.ijse.dto.BatchDTO;
import lk.ijse.dto.BatchEmployeeDTO;
import lk.ijse.dto.EmployeeDTO;
import lk.ijse.entity.Store;
import lk.ijse.tm.BatchTm;
import lk.ijse.tm.EmployeeTm;
import lombok.SneakyThrows;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BatchFormController {

    @FXML
    private TextField txtBatId;
    @FXML
    private TableColumn<?, ?> colAction11;

    @FXML
    private TableView<BatchTm> tblBatch;

    @FXML
    private ChoiceBox<String> choiceType;

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colNumberOfRejectedItem;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colProductionDate;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colStoId;

    @FXML
    private TableColumn<?, ?> colType;


    @FXML
    private TextField txtPrice;

    @FXML
    private JFXComboBox<String> comStoId;
    @FXML
    private Label lblBatId;

    @FXML
    private TextField txtNumOfReject;

    @FXML
    private TextField txtProductionDate;

    @FXML
    private TextField txtQty;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableView<EmployeeTm> tblEmp;

    private ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

    StoreDAO storeDAO = (StoreDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.STORE);
    BatchEmployeeDAO batchEmployeeDAO = (BatchEmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCHEMPLOYEE);
    StoreBO storeBO = (StoreBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STORE);
    BatchBO batchBO = (BatchBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BATCH);
    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.EMPLOYEE);

    public void initialize() {
        setCellValueFactory();
        setCellValueFactoryEmp();
        loadAllBatch();
        loadAllEmp();
        getStoreIds();
        generateNexrBatchId();
        txtProductionDate.setText(String.valueOf(LocalDate.now()));

        ObservableList<String> batchType = FXCollections.observableArrayList("Jack Mackerel", "Tuna Mackeral","Mackerel","Sardin");
        choiceType.setItems(batchType);
    }

    @SneakyThrows
    private void generateNexrBatchId() {
        String currentId = batchBO.generateBatchId();
        txtBatId.setText(currentId);
    }

    private void loadAllEmp() {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDTO> employeeDTOList = employeeBO.getAllEmployee();
            for (EmployeeDTO employeeDTO : employeeDTOList){
                final EmployeeDTO emp = employeeDTO;

                JFXButton btn = new JFXButton("Add");
                btn.setCursor(Cursor.HAND);

                EmployeeTm tm = new EmployeeTm(employeeDTO.getEmpId(), employeeDTO.getFirstName(), employeeDTO.getAddress(), employeeDTO.getTel(), employeeDTO.getSalary(), employeeDTO.getPosition(), btn);
                obList.add(tm);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Add?", yes, no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = obList.indexOf(tm);

                        String Empid = emp.getEmpId();
                        String batchId=txtBatId.getText();
                        System.out.println(batchId);

                        try {
                            if (batchEmployeeDAO.save(new BatchEmployeeDTO(batchId,Empid))){
                                new Alert(Alert.AlertType.CONFIRMATION,"employee is saved").show();
                            }
                        } catch (SQLException ex) {
                            new Alert(Alert.AlertType.ERROR,ex.getMessage()).show();
                            System.out.println(ex.getMessage());
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }

                        // Update the existing EmployeeTm object in the obList
                        tm.setBtnSave(new JFXButton("Added"));
                    }
                });
            }

            tblEmp.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void setCellValueFactoryEmp() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnSave"));
    }

    private void getStoreIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> storeList = storeBO.getStoreIds();
            for(String id : storeList){
                obList.add(id);
            }
            comStoId.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colStoId.setCellValueFactory(new PropertyValueFactory<>("stoId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colProductionDate.setCellValueFactory(new PropertyValueFactory<>("productionDate"));
        colNumberOfRejectedItem.setCellValueFactory(new PropertyValueFactory<>("numberOfReject"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    private void loadAllBatch() {
        ObservableList<BatchTm> obList = FXCollections.observableArrayList();

        try {
            List<BatchDTO> batchDTOList = batchBO.getAllBatch();
            for (BatchDTO batchDTO : batchDTOList){
                BatchTm tm = new BatchTm(batchDTO.getBatId(), batchDTO.getStoId(), batchDTO.getPrice(), batchDTO.getType(), batchDTO.getProductionDate(), batchDTO.getNumberOfReject(), batchDTO.getQty());
                obList.add(tm);
            }
            tblBatch.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtBatId.setText("");
        comStoId.setValue(null);
        txtPrice.setText("");
        txtProductionDate.setText("");
        txtNumOfReject.setText("");
        txtQty.setText("");
        choiceType.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtBatId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter batch ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (batchBO.deleteBatch(id)){
                new Alert(Alert.AlertType.CONFIRMATION,"batch is deleted").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllBatch();
        clearFields();
        generateNexrBatchId();
        txtProductionDate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtBatId.getText();
        String stoId = comStoId.getValue();

        double price = 0.0;
        if (!txtPrice.getText().isEmpty()) {
            price = Double.parseDouble((txtPrice.getText()));
        }

        String type = (String) choiceType.getValue();

        Date productionDate = null;
        if (!txtProductionDate.getText().isEmpty()){
             productionDate = Date.valueOf(LocalDate.now());
        }
        int numOfReject = 0;
        if (!txtNumOfReject.getText().isEmpty()) {
            numOfReject = Integer.parseInt(txtNumOfReject.getText());
        }

        int qty = 0;
        if (!txtQty.getText().isEmpty()) {
            qty = Integer.parseInt(txtQty.getText());
        }

        if (id.isEmpty() || stoId == null|| type == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

    try {
        if (batchBO.saveBatch(new BatchDTO(id,stoId,price,type,productionDate,numOfReject,qty))) {
            new Alert(Alert.AlertType.CONFIRMATION, "batch is saved").show();
        }
    } catch (SQLException | ClassNotFoundException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }

        loadAllBatch();
        clearFields();
        generateNexrBatchId();
        txtProductionDate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtBatId.getText();
        String stoId = comStoId.getValue();
        double price = 0.0;
        if (!txtPrice.getText().isEmpty()) {
            price = Double.parseDouble((txtPrice.getText()));
        }
        String type = (String) choiceType.getValue();

        Date productionDate = null;
        if (!txtProductionDate.getText().isEmpty()){
            productionDate = Date.valueOf(LocalDate.now());
        }


        int numOfReject = 0;
        if (!txtNumOfReject.getText().isEmpty()) {
            numOfReject = Integer.parseInt(txtNumOfReject.getText());
        }

        int qty = 0;
        if (!txtQty.getText().isEmpty()) {
            qty = Integer.parseInt(txtQty.getText());
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

    try {
        if (batchBO.updateBatch(new  BatchDTO(id,stoId,price,type,productionDate,numOfReject,qty))) {
            new Alert(Alert.AlertType.CONFIRMATION, "batch is updated").show();
        }
    } catch (SQLException | ClassNotFoundException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }
        loadAllBatch();
        clearFields();
        generateNexrBatchId();
        txtProductionDate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void comStoIdOnAction(ActionEvent event) {
        String id = comStoId.getValue();

        try {
            Store store = storeDAO.searchById(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtBatId.getText();

        try {
            BatchDTO batchDTO = batchBO.searchByBatchId(id);

            if(batchDTO != null){
                txtBatId.setText(batchDTO.getBatId());
                comStoId.setValue(batchDTO.getStoId());
                txtPrice.setText(String.valueOf(batchDTO.getPrice()));
                txtProductionDate.setText(String.valueOf(batchDTO.getProductionDate()));
                txtNumOfReject.setText(String.valueOf(batchDTO.getNumberOfReject()));
                txtQty.setText(String.valueOf(batchDTO.getQty()));
                choiceType.setValue(batchDTO.getType());
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.INFORMATION,"batch is not found !").show();
        }
    }

    public void tblBatOnClick(MouseEvent mouseEvent) {
        int index = tblBatch.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String batId = colBatId.getCellData(index).toString();
        String stoId = colStoId.getCellData(index).toString();
        String unitPrice = colPrice.getCellData(index).toString();
        String type = colType.getCellData(index).toString();
        String productionDate = colProductionDate.getCellData(index).toString();
        String numOfReject = colNumberOfRejectedItem.getCellData(index).toString();
        String qty = colQty.getCellData(index).toString();

        txtBatId.setText(batId);
        comStoId.setValue(stoId);
        txtPrice.setText(unitPrice);
        choiceType.setValue(type);
        txtProductionDate.setText(productionDate);
        txtNumOfReject.setText(numOfReject);
        txtQty.setText(qty);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtBatId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtPrice)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtQty)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtNumOfReject)) return false;

        return true;
    }

    public void txtBatIdOnKeyReleased(javafx.scene.input.KeyEvent keyEvent) {Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtBatId);}

    @FXML
    void txtPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtPrice);
    }

    @FXML
    void txtQtyOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtQty);
    }

    @FXML
    void txtNumOfRejectOnKeyReleased(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtNumOfReject);}

}