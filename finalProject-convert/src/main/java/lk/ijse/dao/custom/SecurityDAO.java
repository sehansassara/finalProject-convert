package lk.ijse.dao.custom;

import java.sql.SQLException;

public interface SecurityDAO {
    public String check(String userId) throws SQLException, ClassNotFoundException;

    public boolean updatePass(String newPassword) throws SQLException, ClassNotFoundException;
}
