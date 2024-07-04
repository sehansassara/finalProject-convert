package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.BatchDTO;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.dto.OrderDTO;
import lk.ijse.dto.PlaceOrderDTO;

import java.sql.SQLException;
import java.util.List;

public interface PlaceOrderBO extends SuperBO {
    public List<OrderDTO> getAllOrder() throws SQLException, ClassNotFoundException ;

    public List<String> getBatchIds() throws SQLException, ClassNotFoundException ;

    public List<String> getCustomerCon() throws SQLException, ClassNotFoundException ;

    public String getCurrentOrderId() throws SQLException, ClassNotFoundException ;

    public BatchDTO searchByBatchId(String id) throws SQLException, ClassNotFoundException ;

    public CustomerDTO searchByCustomerTel(String tel) throws SQLException, ClassNotFoundException ;
    public String calculateNetTotalOrd(String orderId) throws SQLException, ClassNotFoundException ;

    public boolean placeOrder(PlaceOrderDTO po) throws SQLException ;
}
