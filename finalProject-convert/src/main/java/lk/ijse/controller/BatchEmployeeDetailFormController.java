package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.QueryBO;
import lk.ijse.bo.custom.impl.QueryBOImpl;
import lk.ijse.dto.BatchEmployeeDTO;
import lk.ijse.tm.BatchEmployeeTm;

import java.sql.SQLException;
import java.util.List;

public class BatchEmployeeDetailFormController {

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableView<BatchEmployeeTm> tblBatEmp;

    @FXML
    private TextField txtBatId;

    @FXML
    private TextField txtEMPId;

    QueryBO queryBO = (QueryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.QUERY);

    public void initialize() {
            setCellValueFactory();
            loadAllBatchEmp();
    }

    private void setCellValueFactory() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
    }

    private void loadAllBatchEmp() {
        ObservableList<BatchEmployeeTm> batEmpde = FXCollections.observableArrayList();
        try {
            List<BatchEmployeeDTO> batEmp = queryBO.getAll();
            for (BatchEmployeeDTO batchEmployeeDTO1 : batEmp){
                BatchEmployeeTm tmt = new BatchEmployeeTm(
                        batchEmployeeDTO1.getBatId(),
                        batchEmployeeDTO1.getEmpId()
                        );
                batEmpde.add(tmt);
            }
            tblBatEmp.setItems(batEmpde);
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);

        }
    }

    public void tblBatEmpOnMouse(MouseEvent mouseEvent) {
        int index = tblBatEmp.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String batId = colBatId.getCellData(index).toString();
        String empId = colEmpId.getCellData(index).toString();

        try {
             txtBatId.setText(batId);
             txtEMPId.setText(empId);
        }catch (NullPointerException e){}
    }
}
