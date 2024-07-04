package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.StoreDTO;

import java.sql.SQLException;
import java.util.List;

public interface StoreBO extends SuperBO {
    public List<String> getStoreIds() throws SQLException, ClassNotFoundException ;

    public StoreDTO searchByStoreId(String id) throws SQLException, ClassNotFoundException ;

    public List<StoreDTO> getAllStore() throws SQLException, ClassNotFoundException ;

    public boolean deleteStore(String id) throws SQLException, ClassNotFoundException ;

    public boolean saveStore(StoreDTO storeDTO) throws SQLException, ClassNotFoundException ;

    public boolean updateStore(StoreDTO storeDTO) throws SQLException, ClassNotFoundException ;
}
