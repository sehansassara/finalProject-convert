package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.PlaceOrderBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.*;
import lk.ijse.dao.custom.impl.*;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.BatchDTO;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.dto.OrderDTO;
import lk.ijse.dto.PlaceOrderDTO;
import lk.ijse.entity.Batch;
import lk.ijse.entity.Customer;
import lk.ijse.entity.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderBOImpl implements PlaceOrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    BatchDAO batchDAO = (BatchDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCH);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    public List<OrderDTO> getAllOrder() throws SQLException, ClassNotFoundException {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        List<Order> orders = orderDAO.getAll();
        for (Order order : orders) {
            orderDTOS.add(new OrderDTO(order.getOrdId(),order.getCusId(),order.getDateOfPlace()));
        }
        return orderDTOS;
    }

    public List<String> getBatchIds() throws SQLException, ClassNotFoundException {
        return batchDAO.getIds();
    }

    public List<String> getCustomerCon() throws SQLException, ClassNotFoundException {
        return customerDAO.getCon();
    }

    public String getCurrentOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.getCurrentId();
    }

    public BatchDTO searchByBatchId(String id) throws SQLException, ClassNotFoundException {
        Batch b = batchDAO.searchById(id);
        return new BatchDTO(b.getBatId(),b.getStoId(),b.getPrice(),b.getType(),b.getProductionDate(),b.getNumberOfReject(),b.getQty());
    }

    public CustomerDTO searchByCustomerTel(String tel) throws SQLException, ClassNotFoundException {
        Customer customer = customerDAO.searchByTel(tel);
        return new CustomerDTO(customer.getId(),customer.getName(),customer.getTel(),customer.getAddress());
    }
    public String calculateNetTotalOrd(String orderId) throws SQLException, ClassNotFoundException {
       return queryDAO.calculateNetTotalOrd(orderId);
    }

    public boolean placeOrder(PlaceOrderDTO po) throws SQLException {
        OrderDetailDAO orderDetailDAO = new OrderDetailDAOImpl();
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isOrderSaved = orderDAO.savePlace(po.getOrderDTO());
            if (isOrderSaved) {
                boolean isQtyUpdated = batchDAO.updatePlace(po.getOdList());
                if (isQtyUpdated){
                    boolean isOrderDetailSaved = orderDetailDAO.save(po.getOdList());
                    if (isOrderDetailSaved){
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;
        }catch (Exception e){
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }
}
