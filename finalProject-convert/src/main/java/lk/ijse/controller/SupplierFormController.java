package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.IngredientBO;
import lk.ijse.bo.custom.impl.IngredientBOImpl;
import lk.ijse.bo.custom.SupplierBO;
import lk.ijse.bo.custom.impl.SupplierBOImpl;
import lk.ijse.controller.Util.Regex;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.IngredientDAO;
import lk.ijse.dao.custom.OrderDAO;
import lk.ijse.dao.custom.impl.IngredientDAOImpl;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.SupplierDTO;
import lk.ijse.entity.Ingredient;
import lk.ijse.tm.SupplierTm;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCompanyName;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colIngId;

    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private TableView<SupplierTm> tblSupplier;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCompanyName;

    @FXML
    private TextField txtContact;

    @FXML
    private JFXComboBox<String> comIngId;

    @FXML
    private TextField txtIngId;

    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SUPPLIER);
    IngredientBO ingredientBO = (IngredientBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INGREDIENT);
    IngredientDAO ingredientDAO = (IngredientDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.INGREDIENT);

    @FXML
    private TextField txtSupId;
    public void initialize() {
        setCellValueFactory();
        loadAllSupplier();
        getIngrediantIds();
        generateNexrSupId();
    }

    @SneakyThrows
    private void generateNexrSupId() {
       String id = supplierBO.generateSupplierId();
       txtSupId.setText(id);
    }

    private void getIngrediantIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();


        try {
            List<String> idList = ingredientBO.getIds();
            for (String id : idList){
                obList.add(id);
            }
            comIngId.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colIngId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
    }

    private void loadAllSupplier() {
        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();
        try {
            List<SupplierDTO> supplierDTOList = supplierBO.getAllSupplier();
            for (SupplierDTO supplierDTO : supplierDTOList){
                SupplierTm tm = new SupplierTm(
                        supplierDTO.getSupId(),
                        supplierDTO.getCompanyName(),
                        supplierDTO.getAddress(),
                        supplierDTO.getContact(),
                        supplierDTO.getIngId()
                );
                obList.add(tm);
            }
            tblSupplier.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtSupId.setText("");
        txtCompanyName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        comIngId.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter supplier ID.").show();
            return;
        }

        try {
            if (supplierBO.deleteSupplier(id)){
                new Alert(Alert.AlertType.CONFIRMATION,"supplier is deleted").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllSupplier();
        clearFields();
        generateNexrSupId();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtSupId.getText();
        String companyName = txtCompanyName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String ingId = comIngId.getValue();

        if (id.isEmpty() || companyName.isEmpty() || address.isEmpty() || contact.isEmpty() || ingId == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (supplierBO.saveSupplier(new SupplierDTO(id,companyName,address,contact,ingId))) {
                new Alert(Alert.AlertType.CONFIRMATION, "supplier is saved").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllSupplier();
        clearFields();
        generateNexrSupId();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtSupId.getText();
        String companyName = txtCompanyName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String ingId = comIngId.getValue();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter supplier ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (supplierBO.updateSupplier(new SupplierDTO(id,companyName,address,contact,ingId))) {
                new Alert(Alert.AlertType.CONFIRMATION, "supplier is updated").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllSupplier();
        clearFields();
        generateNexrSupId();
    }

    @FXML
    void ComIngIdOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        try {
            Ingredient ingredientDTO = ingredientDAO.searchById(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        try {
            SupplierDTO supplierDTO = supplierBO.searchBySupplierId(id);
            if (supplierDTO != null){
                txtSupId.setText(supplierDTO.getSupId());
                txtCompanyName.setText(supplierDTO.getCompanyName());
                txtAddress.setText(supplierDTO.getAddress());
                txtContact.setText(supplierDTO.getContact());
                comIngId.setValue(supplierDTO.getIngId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.INFORMATION,"supplier is not found !").show();
        }
    }

    public void tblSupOnMouse(MouseEvent mouseEvent) {
        int index = tblSupplier.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String supId = colSupId.getCellData(index).toString();
        String companyName = colCompanyName.getCellData(index).toString();
        String address = colAddress.getCellData(index).toString();
        String contact = colContact.getCellData(index).toString();
        String ingId = colIngId.getCellData(index).toString();

        txtSupId.setText(supId);
        txtCompanyName.setText(companyName);
        txtAddress.setText(address);
        txtContact.setText(contact);
        comIngId.setValue(ingId);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtSupId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.CONTACT,txtContact)) return false;
        return true;
    }
    @FXML
    void txtAddressOnKeyReleased(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtAddress);}

    @FXML
    void txtContactOnKeyReleased(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.CONTACT,txtContact);}

    @FXML
    void txtSupIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtSupId);
    }

    @FXML
    void txtCompanyKeyReleasedOAction(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtCompanyName);}

    @FXML
    void btnBillOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/Supplier.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("supId",txtSupId.getText());
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport,data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }
}
