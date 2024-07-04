package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.OrderDetailBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.OrderDetailDAO;
import lk.ijse.dao.custom.impl.OrderDetailDAOImpl;
import lk.ijse.dto.OrderDetailDTO;
import lk.ijse.entity.OrderDetail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailBOImpl implements OrderDetailBO {

    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAIL);
    @Override
    public List<OrderDetailDTO> getAllOrderDetail() throws SQLException, ClassNotFoundException {
        List<OrderDetailDTO> list = new ArrayList<>();
        List<OrderDetail> orderDetails = orderDetailDAO.getAll();
        for (OrderDetail orderDetail : orderDetails) {
            list.add(new OrderDetailDTO(orderDetail.getOrdId(),orderDetail.getBatId(),orderDetail.getQty()));
        }
        return list;
    }
}
