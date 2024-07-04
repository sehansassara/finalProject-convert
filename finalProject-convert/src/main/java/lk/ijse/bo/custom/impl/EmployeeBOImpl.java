package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.EmployeeBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.EmployeeDAO;
import lk.ijse.dao.custom.impl.EmployeeDAOImpl;
import lk.ijse.dto.EmployeeDTO;
import lk.ijse.entity.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeBOImpl implements EmployeeBO {

    EmployeeDAO employeeDAO = (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.EMPLOYEE);
    @Override
    public List<EmployeeDTO> getAllEmployee() throws SQLException, ClassNotFoundException {
       List<EmployeeDTO> employees = new ArrayList<>();
       List<Employee> emp = employeeDAO.getAll();
       for (Employee employee : emp) {
           employees.add(new EmployeeDTO(employee.getEmpId(),employee.getFirstName(),employee.getLastName(),employee.getAddress(),employee.getTel(),employee.getSalary(),employee.getPosition()));
       }
       return employees;
    }

    public String generateEmployeeId() throws SQLException, ClassNotFoundException {
        return employeeDAO.generateId();
    }

    public boolean deleteEmployee(String id) throws SQLException, ClassNotFoundException {
        return employeeDAO.delete(id);
    }

    public boolean saveEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {
        return employeeDAO.save(new Employee(dto.getEmpId(),dto.getFirstName(),dto.getLastName(),dto.getAddress(),dto.getTel(),dto.getSalary(),dto.getPosition()));
    }

    public boolean updateEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {
        return employeeDAO.update(new Employee(dto.getEmpId(),dto.getFirstName(),dto.getLastName(),dto.getAddress(),dto.getTel(),dto.getSalary(),dto.getPosition()));
    }

    public EmployeeDTO searchByEmployeeId(String id) throws SQLException, ClassNotFoundException {
       Employee emp = employeeDAO.searchById(id);
       return new EmployeeDTO(emp.getEmpId(),emp.getFirstName(),emp.getLastName(),emp.getAddress(),emp.getTel(),emp.getSalary(),emp.getPosition());
    }
}
