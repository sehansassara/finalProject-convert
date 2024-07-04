package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.QueryBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.OrderDAO;
import lk.ijse.dao.custom.QueryDAO;
import lk.ijse.dao.custom.impl.QueryDAOImpl;
import lk.ijse.dto.BatchEmployeeDTO;
import lk.ijse.entity.BatchEmployee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryBOImpl implements QueryBO {

    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    public List<BatchEmployeeDTO> getAll() throws SQLException, ClassNotFoundException {
       List<BatchEmployeeDTO> employees = new ArrayList<>();
       List<BatchEmployee> employees1 = queryDAO.getAll();
       for (BatchEmployee employee : employees1) {
           employees.add(new BatchEmployeeDTO(employee.getBatId(),employee.getEmpId()));
       }
       return employees;
    }

    public String calculateNetTotalBatch(String batId) throws SQLException, ClassNotFoundException {
       return queryDAO.calculateNetTotalBatch(batId);
    }

    public String calculateNetTotalOrd(String orderId) throws SQLException, ClassNotFoundException {
       return queryDAO.calculateNetTotalOrd(orderId);
    }
}
