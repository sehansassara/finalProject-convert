package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.EmployeeDAO;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.EmployeeDTO;
import lk.ijse.entity.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    public List<Employee> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM employee WHERE status = 'ACTIVE'");
        List<Employee> employeeDTOList = new ArrayList<>();

        while (resultSet.next()){
            Employee employee = new Employee( resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getDouble(6),resultSet.getString(7));
            employeeDTOList.add(employee);
        }
        return employeeDTOList;
    }

    public String generateId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute( "SELECT EMP_ID FROM employee ORDER BY EMP_ID DESC LIMIT 1");
        if (rst.next()) {
            String empId = rst.getString(1);
            String prefix = "E";
            String[] split = empId.split(prefix);
            int idNum = Integer.parseInt(split[1]);
            String nextId = prefix + String.format("%03d", ++idNum);
            return nextId;

        }
        return"E001";
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute( "UPDATE employee SET status = 'DELETE' WHERE EMP_ID = ?",id);
    }

    public boolean save(Employee entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO employee VALUES (?,?,?,?,?,?,?,'ACTIVE')",entity.getEmpId(),entity.getFirstName(),entity.getLastName(),entity.getAddress(),entity.getTel(),entity.getSalary(),entity.getPosition());
    }

    public boolean update(Employee entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE employee SET firstName = ?, lastName = ?, address = ?, tel = ?, salary = ?, position = ? WHERE EMP_ID = ?",entity.getFirstName(),entity.getLastName(),entity.getAddress(),entity.getTel(),entity.getSalary(),entity.getPosition(),entity.getEmpId());
    }

    public Employee searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM employee WHERE EMP_ID = ?",id);
        if (resultSet.next()){
            Employee employee = new Employee( resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getDouble(6), resultSet.getString(7));
            return employee;
        }
        return null;
    }
}
