package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentBO extends SuperBO {
    public String generatePaymentId() throws SQLException, ClassNotFoundException ;

    public List<PaymentDTO> getAllPayment() throws SQLException, ClassNotFoundException ;

    public boolean deletePayment(String payId) throws SQLException, ClassNotFoundException ;

    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException ;

    public boolean updatePayment(PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException ;

    public PaymentDTO searchByIdPayment(String payId) throws SQLException, ClassNotFoundException ;
}
