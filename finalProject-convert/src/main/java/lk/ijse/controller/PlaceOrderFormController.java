package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PlaceOrderBO;
import lk.ijse.bo.custom.impl.PlaceOrderBOImpl;
import lk.ijse.controller.Util.Regex;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.BatchDAO;
import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.dao.custom.OrderDAO;
import lk.ijse.dao.custom.impl.*;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.*;
import lk.ijse.entity.Batch;
import lk.ijse.entity.Customer;
import lk.ijse.tm.OrderTm;
import lombok.Getter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class PlaceOrderFormController {
    @FXML
    private AnchorPane AnchorpaneOrder;

    @FXML
    private JFXButton btnAddToCart;

    @FXML
    private JFXButton btnPlaceOrder;
    @FXML
    private TableColumn<?, ?> colCusId1;

    @FXML
    private TableColumn<?, ?> colDOP;

    @FXML
    private TableColumn<?, ?> colOrderId;
    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colCusId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private JFXComboBox<String> comBatId;

    @FXML
    private JFXComboBox<String> comCustTel;

    @FXML
    private Label lblBatQty;

    @FXML
    private Label lblCustId;

    @FXML
    private Label lblDop;

    @FXML
    private TextField txtOrdId;

    @FXML
    private Label lblType;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private  Label lblNetTotal;

    @FXML
    private TextField txtPaidAmount;

    @FXML
    private TableView<OrderTm> tblCart;

    @FXML
    private TableView<OrderDTO> tblOrder;

    @FXML
    private TextField txtQty;

    @FXML
    private Label lblBalance;
    private ObservableList<OrderTm> obList = FXCollections.observableArrayList();

    BatchDAO batchDAO = (BatchDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCH);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACEORDER);

    public void initialize() {
        setDate();
        getCurrentOrderIds();
        getCustomerCon();
        getBatchIds();
        setCellValueFactory();
        loadAllOrders();
        setCellValueFactoryOrder();
        clearFields();
        comCustTel.setEditable(true);
        comBatId.setEditable(true);
    }

    private void clearFields() {
        comCustTel.setValue(null);
        lblCustId.setText("");
        comBatId.setValue(null);
        lblType.setText("");
        lblUnitPrice.setText("");
        lblBatQty.setText("");
        txtPaidAmount.setText("");
        lblBalance.setText("");
    }

    private void setCellValueFactoryOrder() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("ordId"));
        colCusId1.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colDOP.setCellValueFactory(new PropertyValueFactory<>("dateOfPlace"));
    }

    private void loadAllOrders() {
        ObservableList<OrderDTO> obList = FXCollections.observableArrayList();

        try {
            List<OrderDTO> orderDTOList = placeOrderBO.getAllOrder();
            for (OrderDTO orderDTO : orderDTOList){
                OrderDTO tm = new OrderDTO(
                        orderDTO.getOrdId(),
                        orderDTO.getCusId(),
                        orderDTO.getDateOfPlace()
                );
                obList.add(tm);
            }

            tblOrder.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }

    private void getBatchIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> batIdList = placeOrderBO.getBatchIds();

            for (String id : batIdList) {
                obList.add(id);
            }
            comBatId.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void getCustomerCon() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = placeOrderBO.getCustomerCon();

            for (String tel : nameList) {
                obList.add(tel);
            }

            comCustTel.setItems(obList);

        } catch(SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
    @Getter
    private static String currentId = "";
    private static String nextOrderId = "";

    private void getCurrentOrderIds() {
        try {
             currentId = placeOrderBO.getCurrentOrderId();

            nextOrderId = generateNexrOrderId(currentId);
            txtOrdId.setText(nextOrderId);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexrOrderId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("O");
            int idNum = Integer.parseInt(split[1]);
            return "O" + String.format("%03d", ++idNum);
        }
        return "O001";
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDop.setText(String.valueOf(now));
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String batId = comBatId.getValue();
        String cusId = lblCustId.getText();
        String type = lblType.getText();
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());

        int qty = 0;
        if (!txtQty.getText().isEmpty()) {
            qty = Integer.parseInt(txtQty.getText());
        }

        double total = qty * unitPrice;

        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> types = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if(types.orElse(no) == yes) {
                int selectedIndex = tblCart.getSelectionModel().getSelectedIndex();
                obList.remove(selectedIndex);

                tblCart.refresh();
                calculateNetTotal();
            }
        });

        for (int i = 0;i<tblCart.getItems().size(); i++){
            if (batId.equals(colBatId.getCellData(i))) {

                OrderTm tm = obList.get(i);
                qty += tm.getQty();
                total = qty * unitPrice;

                tm.setQty(qty);
                tm.setTotal(total);

                tblCart.refresh();

                calculateNetTotal();
                return;
            }
        }

        OrderTm tm = new OrderTm(batId, cusId, type, unitPrice, qty, total, btnRemove);
        obList.add(tm);

        tblCart.setItems(obList);
        calculateNetTotal();
        txtQty.setText("");

    }

    private void calculateNetTotal() {
        double netTotal = 0;
        for (int i = 0; i < tblCart.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }
        lblNetTotal.setText(String.valueOf(netTotal));
    }
    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        String orderId = nextOrderId;
        String cusId = lblCustId.getText();
        Date date = Date.valueOf(LocalDate.now());

        var order = new OrderDTO(orderId, cusId, date);

        List<OrderDetailDTO> odList = new ArrayList<>();

        for (int i = 0; i < tblCart.getItems().size(); i++) {
            OrderTm tm = obList.get(i);

            OrderDetailDTO od = new OrderDetailDTO(
                    orderId,
                    tm.getBatId(),
                    tm.getQty()
            );

            odList.add(od);
        }

        PlaceOrderDTO po = new PlaceOrderDTO(order, odList);

        try {
            boolean isPlaced = placeOrderBO.placeOrder(po);
            if (isPlaced){
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
                obList.clear();
                tblCart.setItems(obList);
                calculateNetTotal();
                generateBill(orderId);
                loadAllOrders();
                clearFields();
                getCurrentOrderIds();

            }else{
                new Alert(Alert.AlertType.WARNING, "Order Placed Unsuccessfully!").show();

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void comBatchIdOnAction(ActionEvent event) {
        String batId = comBatId.getValue();

        try {
            Batch batch = batchDAO.searchById(batId);
            if(batch != null) {
                lblUnitPrice.setText(String.valueOf(batch.getPrice()));
                lblType.setText(String.valueOf(batch.getType()));
                lblBatQty.setText(String.valueOf(batch.getQty()));
            }

            txtQty.requestFocus();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void comCustTelOnAction(ActionEvent event) {
        String tel = comCustTel.getValue();
        try {
            Customer customer = customerDAO.searchByTel(tel);
            if (customer != null) {
                 lblCustId.setText(customer.getId());
            }
            txtQty.requestFocus();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddToCartOnAction(event);
    }
    @FXML
    void btnGenerateBillOnAction(ActionEvent event) {

    }

    double balance;
    double netTotal;
    private void generateBill(String orderId) {
        try {
             netTotal = Double.parseDouble(String.valueOf(Double.parseDouble(calculateNetTotal(orderId))));
            double total = Double.parseDouble(String.valueOf(Double.parseDouble(txtPaidAmount.getText())));
             balance = netTotal - total ;
            lblBalance.setText(String.format("%.2f", balance));
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/PlaceBill.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderId", orderId);
            parameters.put("total", netTotal);
            parameters.put("paid amount", total);
            parameters.put("balance", balance);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

            openPaymentForm(String.valueOf(netTotal),nextOrderId);
        } catch (JRException | SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void openPaymentForm(String s, String orderId) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/payment_form.fxml"));
            AnchorPane paymentForm = loader.load();
            PaymentFormController paymentFormController = loader.getController();
            paymentFormController.setNetTotal(netTotal);
            paymentFormController.setOrderId(nextOrderId);

            AnchorpaneOrder.getChildren().clear();
            AnchorpaneOrder.getChildren().add(paymentForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String calculateNetTotal(String orderId) throws SQLException, ClassNotFoundException {
        return placeOrderBO.calculateNetTotalOrd(orderId);
    }

    @FXML
    void filterCustomerCon(KeyEvent event) {
        ObservableList<String > filterCon = FXCollections.observableArrayList();
        String enteredText = comCustTel.getEditor().getText();

        try {
            List<String> conList = placeOrderBO.getCustomerCon();

            for (String con : conList){
                if (con.contains(enteredText)){
                    filterCon.add(con);
                }
            }
            comCustTel.setItems(filterCon);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void filterbatId(KeyEvent event) {
        ObservableList<String > filterCon = FXCollections.observableArrayList();
        String enteredText = comBatId.getEditor().getText();

        try {
            List<String> conList = placeOrderBO.getBatchIds();

            for (String con : conList){
                if (con.contains(enteredText)){
                    filterCon.add(con);
                }
            }
            comBatId.setItems(filterCon);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void comBatIdOnMousedClicked(MouseEvent event) {
        comBatId.getSelectionModel().clearSelection();
    }

    @FXML
    void comCustTelOnMouseClicked(MouseEvent event) {
        comCustTel.getSelectionModel().clearSelection();
    }

    @FXML
    void btnAddNewCustomerOnAction(ActionEvent event) throws IOException {

        AnchorPane customerPane = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));


        AnchorpaneOrder.getChildren().clear();
        AnchorpaneOrder.getChildren().add(customerPane);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtQty)) return false;
        return true;
    }

    @FXML
    void txtQtykeyReleasedOnAction(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtQty);

    }


    @FXML
    void txtpaidamountOnKey(KeyEvent event) {
        try {
            double netTotal = Double.parseDouble(lblNetTotal.getText());
            double paidAmount = Double.parseDouble(txtPaidAmount.getText());
            double balance = paidAmount - netTotal;
            lblBalance.setText(String.format("%.2f", balance));
        } catch (NumberFormatException e) {
            lblBalance.setText("Invalid input");
        }
    }}












/*package lk.ijse.controller;

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
import javafx.scene.layout.AnchorPane;
import lk.ijse.model.Customer;
import lk.ijse.model.Order;
import lk.ijse.model.Payment;
import lk.ijse.model.tm.OrderTm;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.OrderRepo;
import lk.ijse.repository.PaymentRepo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class PlaceOrderFormController {

    @FXML
    private AnchorPane AnchorpaneOrder;

    @FXML
    private TableColumn<?, ?> colCustId;

    @FXML
    private TableColumn<?, ?> colDateOfPlace;

    @FXML
    private TableColumn<?, ?> colDateOfRelease;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colPayId;

    @FXML
    private TableView<OrderTm> tblOrder;

    @FXML
    private JFXComboBox<String> comCustId;

    @FXML
    private JFXComboBox<String> comPayId;

    @FXML
    private TextField txtDateOfPlace;

    @FXML
    private TextField txtDateOfRelease;

    @FXML
    private TextField txtOrderId;

    public void initialize(){
        setCellValueFactory();
        loadAllOrders();
        getCustomerIds();
    }



    private void getCustomerIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();


        try {
            List<String> idList = CustomerRepo.getIds();
            for (String id : idList){
                obList.add(id);
            }
            comCustId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("ordId"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colDateOfPlace.setCellValueFactory(new PropertyValueFactory<>("dateOfPlace"));
    }

    private void loadAllOrders() {
        ObservableList<OrderTm> obList = FXCollections.observableArrayList();

        try {
            List<Order> orderList = OrderRepo.getAll();
            for (Order order : orderList){
                OrderTm tm = new OrderTm(
                        order.getOrdId(),
                        order.getCusId(),
                        order.getDateOfPlace()
                );
                obList.add(tm);
            }

            tblOrder.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtOrderId.setText("");
        txtDateOfPlace.setText("");
        comCustId.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtOrderId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter order ID for delete.").show();
            return;
        }


        try {
            boolean isDeleted = OrderRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "order is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllOrders();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String ordId = txtOrderId.getText();
        String cusId = comCustId.getValue();
        Date dop = Date.valueOf(txtDateOfPlace.getText());


        if (ordId.isEmpty() || cusId == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        Order order = new Order(ordId,cusId,dop);

        try {
            boolean isSaved = OrderRepo.save(order);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"order is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();

        }
        loadAllOrders();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String ordId = txtOrderId.getText();
        String cusId = comCustId.getValue();
        Date dop = Date.valueOf(txtDateOfPlace.getText());

        if (ordId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter order ID.").show();
            return;
        }


        Order order = new Order(ordId,cusId,dop);
        try {
            boolean isUpdated = OrderRepo.update(order);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"order is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllOrders();
    }

    @FXML
    void comCusIdOnAction(ActionEvent event) {
        String id = comCustId.getValue();

        try {
            Customer customer = CustomerRepo.searchById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void comPayIdOnAction(ActionEvent event) {
        String payId = comPayId.getValue();

        try {
            Payment payment = PaymentRepo.searchById(payId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void txtSearchOnAction(ActionEvent actionEvent) {
        String id = txtOrderId.getText();

        try {
            Order order = OrderRepo.searchById(id);
            if (order != null){
                txtOrderId.setText(order.getOrdId());
                comCustId.setValue(order.getCusId());
                txtDateOfPlace.setText(String.valueOf(order.getDateOfPlace()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"order is not found !").show();
        }
    }
}*/