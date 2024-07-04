package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.bo.custom.impl.PaymentBOImpl;
import lk.ijse.controller.Util.Regex;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.OrderDAO;
import lk.ijse.dao.custom.impl.OrderDAOImpl;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.tm.PaymentTm;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentFormController {

    @FXML
    private AnchorPane AnpPay;

    @FXML
    private ChoiceBox<String> choiceType;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colOrd;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colPayId;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableView<PaymentTm> tblPayment;

    @FXML
    private JFXComboBox<String> comOrd;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtPayId;

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    PaymentBO paymentBO = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);

    public void initialize() {
        setCellValueFactory();
        loadAllPayments();
        getOrdIds();
        generateNexrPayId();
        ObservableList<String> paymentTypes = FXCollections.observableArrayList("Cash", "Card");
        choiceType.setItems(paymentTypes);
        txtDate.setText(String.valueOf(LocalDate.now()));
        comOrd.setEditable(true);

       // String orderId = PlaceOrderFormController.getCurrentId();

       // setOrderId(orderId);

    }

    void setOrderId(String orderId) {
        comOrd.setValue(orderId);
    }

    void setNetTotal(double netTotal) {
        txtAmount.setText(String.valueOf(netTotal));
    }

    @SneakyThrows
    private void generateNexrPayId() {
        String id = paymentBO.generatePaymentId();
        txtPayId.setText(id);
    }

    private void getOrdIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> idList = orderDAO.getIds();
            for (String id : idList){
                obList.add(id);
            }
            comOrd.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colPayId.setCellValueFactory(new PropertyValueFactory<>("payId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colOrd.setCellValueFactory(new PropertyValueFactory<>("ordId"));
    }

    private void loadAllPayments() {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();

        try {
            List<PaymentDTO> paymentDTOList = paymentBO.getAllPayment();
            for (PaymentDTO paymentDTO : paymentDTOList){
                PaymentTm tm = new PaymentTm(
                        paymentDTO.getPayId(),
                        paymentDTO.getAmount(),
                        paymentDTO.getDate(),
                        paymentDTO.getType(),
                        paymentDTO.getOrdId()
                );
                obList.add(tm);
            }

            tblPayment.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtPayId.setText("");
        txtAmount.setText("");
        txtDate.setText("");
        comOrd.setValue(null);
        choiceType.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        if (payId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter payment ID.").show();
            return;
        }

        try {
            if (paymentBO.deletePayment(payId)){
                new Alert(Alert.AlertType.CONFIRMATION,"payment is Deleted").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllPayments();
        clearFields();
        generateNexrPayId();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        double amount  = 0.0;
        if (!txtAmount.getText().isEmpty()) {
            amount = Double.parseDouble(txtAmount.getText());
        }

        Date date = null;
        if (!txtDate.getText().isEmpty()){
            date = Date.valueOf(LocalDate.now());
        }

        String choiceTypeValue = (String) choiceType.getValue();
        String ordId = comOrd.getValue();

        if (payId.isEmpty() || choiceTypeValue == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (paymentBO.savePayment(new PaymentDTO(payId,amount,date,choiceTypeValue,ordId))) {
                new Alert(Alert.AlertType.CONFIRMATION, "payment is saved").show();
                AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/placeOrder_form.fxml"));


                AnpPay.getChildren().clear();
                AnpPay.getChildren().add(orderPane);
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadAllPayments();
        clearFields();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        double amount  = 0.0;
        if (!txtAmount.getText().isEmpty()) {
            amount = Double.parseDouble(txtAmount.getText());
        }

        Date date = null;
        if (!txtDate.getText().isEmpty()){
            date = Date.valueOf(LocalDate.now());
        }
        
        String choiceTypeValue = (String) choiceType.getValue();
        String ordId = comOrd.getValue();

        if (payId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter payment ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (paymentBO.updatePayment(new PaymentDTO(payId,amount,date,choiceTypeValue,ordId))) {
                new Alert(Alert.AlertType.CONFIRMATION, "payment is updated").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllPayments();
        clearFields();
        generateNexrPayId();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        try {
            PaymentDTO paymentDTO = paymentBO.searchByIdPayment(payId);
            if (paymentDTO != null){
                txtPayId.setText(paymentDTO.getPayId());
                txtAmount.setText(String.valueOf(paymentDTO.getAmount()));
                txtDate.setText(String.valueOf(paymentDTO.getDate()));
                comOrd.setValue(paymentDTO.getOrdId());
                choiceType.setValue(paymentDTO.getType());
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.INFORMATION,"payment is not found !").show();
        }
    }

    public void choiceTypeOnAction(MouseEvent mouseEvent) {

    }

    public void tblPayOnMouse(MouseEvent mouseEvent) {
        int index = tblPayment.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String payId = colPayId.getCellData(index).toString();
        String amount = colAmount.getCellData(index).toString();
        String date = colDate.getCellData(index).toString();
        String type = colType.getCellData(index).toString();
        String ordId = colOrd.getCellData(index).toString();

        txtPayId.setText(payId);
        txtAmount.setText(amount);
        txtDate.setText(date);
        choiceType.setValue(type);
        comOrd.setValue(ordId);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtPayId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtAmount)) return false;
        return true;
    }
    @FXML
    void txtAmountOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtAmount);
    }

    @FXML
    void txtPayIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtPayId);
    }

    @FXML
    void btnBillOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/Payment.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("payId",txtPayId.getText());
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport,data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void filterOrderId(KeyEvent event) {
        ObservableList<String > filterCon = FXCollections.observableArrayList();
        String enteredText = comOrd.getEditor().getText();

        try {
            List<String> conList = orderDAO.getIds();

            for (String con : conList){
                if (con.contains(enteredText)){
                    filterCon.add(con);
                }
            }
            comOrd.setItems(filterCon);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void orderIdMousedClick(MouseEvent event) {
        comOrd.getSelectionModel().clearSelection();
    }
}
