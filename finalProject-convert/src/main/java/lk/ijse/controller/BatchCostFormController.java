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
import lk.ijse.bo.custom.BatchCostBO;
import lk.ijse.bo.custom.impl.BatchCostBOImpl;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.BatchDAO;
import lk.ijse.dao.custom.IngredientDAO;
import lk.ijse.dao.custom.OrderDAO;
import lk.ijse.dao.custom.impl.BatchDAOImpl;
import lk.ijse.dao.custom.impl.IngredientDAOImpl;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.BatchCostDTO;
import lk.ijse.dto.batchIngredientDTO;
import lk.ijse.entity.Batch;
import lk.ijse.entity.Ingredient;
import lk.ijse.tm.BatchCostTm;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.*;

public class BatchCostFormController {
    @FXML
    private JFXButton btnAddToTable;

    @FXML
    private JFXButton btnPlaceCost;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colIngId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private JFXComboBox<String> comBatId;

    @FXML
    private JFXComboBox<String> comIngId;

    @FXML
    private Label lblBatchType;

    @FXML
    private Label lblIngType;

    @FXML
    private Label lblProductionDate;

    @FXML
    private Label lblQtyOnHand;
    @FXML
    private Label lblBatchUnitPrice;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private Label lblTotalCost;

    @FXML
    private TableView<BatchCostTm> tblCost;

    @FXML
    private TextField txtQty;

    private ObservableList<BatchCostTm> obList = FXCollections.observableArrayList();

    BatchCostBO batchCostBO = (BatchCostBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BATCHCOST);
    BatchDAO batchDAO = (BatchDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCH);
    IngredientDAO ingredientDAO = (IngredientDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.INGREDIENT);

    public void initialize() {
        getBatIds();
        getIngIds();
        setCellValueFactory();
        clearFields();
        comBatId.setEditable(true);
        comBatId.setEditable(true);
    }
    private void clearFields() {
        comBatId.setValue(null);
        comIngId.setValue(null);
        lblBatchUnitPrice.setText("");
        lblProductionDate.setText("");
        lblUnitPrice.setText("");
        txtQty.setText("");
        lblIngType.setText("");
        lblBatchUnitPrice.setText("");
        lblQtyOnHand.setText("");
        lblBatchType.setText("");
    }
    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colIngId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }

    private void getIngIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> ingList = batchCostBO.getIngredientIds();

            for (String id : ingList) {
                obList.add(id);
            }
            comIngId.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void getBatIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> batIdList = batchCostBO.getBatchIds();

            for (String id : batIdList) {
                obList.add(id);
            }
            comBatId.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ComBatIdOnAction(ActionEvent event) {
        String batId = comBatId.getValue();

        try {
            Batch batch = batchDAO.searchById(batId);
            if(batch != null) {
                lblBatchUnitPrice.setText(String.valueOf(batch.getPrice()));
                lblBatchType.setText(String.valueOf(batch.getType()));
                lblProductionDate.setText(String.valueOf(batch.getProductionDate()));
            }

            txtQty.requestFocus();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    String batId="";
    @FXML
    void btnAddToTableOnAction(ActionEvent event) {
        batId = comBatId.getValue();
        String ingId = comIngId.getValue();
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double total = qty * unitPrice;

        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> types = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if(types.orElse(no) == yes) {
                int selectedIndex = tblCost.getSelectionModel().getSelectedIndex();
                obList.remove(selectedIndex);

                tblCost.refresh();
                calculateNetTotal();
            }
        });

        for (int i = 0;i<tblCost.getItems().size(); i++){
            if (ingId.equals(colIngId.getCellData(i))) {

                BatchCostTm tm = obList.get(i);
                qty += tm.getQty();
                total = qty * unitPrice;

                tm.setQty(qty);
                tm.setTotal(total);

                tblCost.refresh();

                calculateNetTotal();
                return;
            }
        }
        BatchCostTm tm = new BatchCostTm(batId, ingId, unitPrice, qty, total, btnRemove);
        obList.add(tm);
        System.out.println("fhfjccf");
        tblCost.setItems(obList);
        calculateNetTotal();
        txtQty.setText("");
        }

    private void calculateNetTotal() {
        int netTotal = 0;
        for (int i = 0; i < tblCost.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }
        lblTotalCost.setText(String.valueOf(netTotal));
    }

    @FXML
    void btnPlaceCostOnAction(ActionEvent event) {
        List<batchIngredientDTO> bcList = new ArrayList<>();

        for (int i = 0; i < tblCost.getItems().size(); i++) {
            BatchCostTm tm = obList.get(i);

            batchIngredientDTO od = new batchIngredientDTO(
                    tm.getBatId(),
                    tm.getQty(),
                    tm.getIngId()
            );

            bcList.add(od);
        }

        BatchCostDTO bc = new BatchCostDTO(bcList);

        try {
            boolean isPlaced = batchCostBO.placeCost(bc);
            if (isPlaced){
                new Alert(Alert.AlertType.CONFIRMATION, "batch cost Placed!").show();
                obList.clear();
                tblCost.setItems(obList);
                generateBill(batId);
                calculateNetTotal();
                clearFields();
            }else{
                new Alert(Alert.AlertType.WARNING, "batch cost Placed Unsuccessfully!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateBill(String batId) {
        try {
            String netTotal = calculateNetTotal(batId);

            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/BatchCost.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("batId", batId);
            parameters.put("total", netTotal);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String calculateNetTotal(String batId) throws SQLException, ClassNotFoundException {
        return batchCostBO.calculateNetTotalBatch(batId);
    }

    @FXML
    void comIngIdOnAction(ActionEvent event) {
        String ingId = comIngId.getValue();

        try {
            Ingredient ingredi = ingredientDAO.searchById(ingId);
            if(ingredi != null) {
                lblIngType.setText(String.valueOf(ingredi.getType()));
                lblUnitPrice.setText(String.valueOf(ingredi.getUnitPrice()));
                lblQtyOnHand.setText(String.valueOf(ingredi.getQtyOnHand()));
            }

            txtQty.requestFocus();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddToTableOnAction(event);
    }

    @FXML
    void comBatIdOnMouseClicked(MouseEvent event) {
        comBatId.getSelectionModel().clearSelection();
    }

    @FXML
    void comIngIdOnMouseClicked(MouseEvent event) {
        comIngId.getSelectionModel().clearSelection();
    }

    @FXML
    void filterBatId(KeyEvent event) {
        ObservableList<String > filterCon = FXCollections.observableArrayList();
        String enteredText = comBatId.getEditor().getText();

        try {
            List<String> conList = batchCostBO.getBatchIds();

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
    void filterIngId(KeyEvent event) {
        ObservableList<String > filterCon = FXCollections.observableArrayList();
        String enteredText = comIngId.getEditor().getText();

        try {
            List<String> conList = batchCostBO.getIngredientIds();

            for (String con : conList){
                if (con.contains(enteredText)){
                    filterCon.add(con);
                }
            }
            comIngId.setItems(filterCon);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
