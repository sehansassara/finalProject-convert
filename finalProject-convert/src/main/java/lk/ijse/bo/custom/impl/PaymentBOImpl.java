package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.entity.Payment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentBOImpl implements PaymentBO {

    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);
    @Override
    public String generatePaymentId() throws SQLException, ClassNotFoundException {
       return paymentDAO.generateId();
    }

    public List<PaymentDTO> getAllPayment() throws SQLException, ClassNotFoundException {
       List<PaymentDTO> list = new ArrayList<>();
       List<Payment> payments = paymentDAO.getAll();
       for (Payment payment : payments) {
           list.add(new PaymentDTO(payment.getPayId(),payment.getAmount(),payment.getDate(),payment.getType(),payment.getOrdId()));
       }
       return list;
    }

    public boolean deletePayment(String payId) throws SQLException, ClassNotFoundException {
        return paymentDAO.delete(payId);
    }

    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException {
        return paymentDAO.save(new Payment(paymentDTO.getPayId(),paymentDTO.getAmount(),paymentDTO.getDate(),paymentDTO.getType(),paymentDTO.getOrdId()));
    }

    public boolean updatePayment(PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException {
        return paymentDAO.save(new Payment(paymentDTO.getPayId(),paymentDTO.getAmount(),paymentDTO.getDate(),paymentDTO.getType(),paymentDTO.getOrdId()));
    }

    public PaymentDTO searchByIdPayment(String payId) throws SQLException, ClassNotFoundException {
       Payment p = paymentDAO.searchById(payId);
       return new PaymentDTO(p.getPayId(),p.getAmount(),p.getDate(),p.getType(),p.getOrdId());
    }
}
