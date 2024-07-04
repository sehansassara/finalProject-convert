package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.OrderDetailDTO;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailBO extends SuperBO {
    public List<OrderDetailDTO> getAllOrderDetail() throws SQLException, ClassNotFoundException ;
    }
