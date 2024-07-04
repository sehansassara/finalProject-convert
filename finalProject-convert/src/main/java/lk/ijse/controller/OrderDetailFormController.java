package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.OrderDetailBO;
import lk.ijse.bo.custom.impl.OrderDetailBOImpl;
import lk.ijse.dto.OrderDetailDTO;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailFormController {

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colOrdId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableView<OrderDetailDTO> tblOrderDetail;

    OrderDetailBO orderDetailBO = (OrderDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDERDETAIL);

    public  void initialize() {
        loadAllOrderDetails();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colOrdId.setCellValueFactory(new PropertyValueFactory<>("ordId"));
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    private void loadAllOrderDetails() {
        ObservableList<OrderDetailDTO> obList = FXCollections.observableArrayList();

        try {
            List<OrderDetailDTO> orderList = orderDetailBO.getAllOrderDetail();
            for (OrderDetailDTO orderDetailDTO : orderList){
                OrderDetailDTO tm = new OrderDetailDTO(orderDetailDTO.getOrdId(), orderDetailDTO.getBatId(), orderDetailDTO.getQty());
                obList.add(tm);
            }
            tblOrderDetail.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
