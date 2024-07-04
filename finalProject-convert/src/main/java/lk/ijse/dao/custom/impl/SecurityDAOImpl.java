package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SqlUtil;
import lk.ijse.dao.custom.SecurityDAO;
import lk.ijse.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecurityDAOImpl implements SecurityDAO {

    public String check(String userId) throws SQLException, ClassNotFoundException {
       /* Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT userId, password FROM user WHERE userId = ?");

        preparedStatement.setString(1, userId);*/
        ResultSet resultSet = SqlUtil.execute("SELECT userId, password FROM user WHERE userId = ?",userId);

        if (resultSet.next()) {
            return resultSet.getString("password");
        } else {
            return null;
    }
    }

    public boolean updatePass(String newPassword) throws SQLException, ClassNotFoundException {
       /* String sql = "UPDATE user SET password = ? WHERE userId = 'U001'";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, newPassword);
        return pstm.executeUpdate() > 0;*/
        return SqlUtil.execute("UPDATE user SET password = ? WHERE userId = 'U001'",newPassword);
    }
}
