package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.StoreBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.OrderDAO;
import lk.ijse.dao.custom.StoreDAO;
import lk.ijse.dao.custom.impl.StoreDAOImpl;
import lk.ijse.dto.StoreDTO;
import lk.ijse.entity.Store;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreBOImpl implements StoreBO {

    StoreDAO storeDAO = (StoreDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.STORE);

    public List<String> getStoreIds() throws SQLException, ClassNotFoundException {
        return storeDAO.getIds();
    }

    public StoreDTO searchByStoreId(String id) throws SQLException, ClassNotFoundException {
        Store s = storeDAO.searchById(id);
        return new StoreDTO(s.getStoId(),s.getCapacity(),s.getLocation());
    }

    public List<StoreDTO> getAllStore() throws SQLException, ClassNotFoundException {
        List<StoreDTO> storeDTOList = new ArrayList<>();
        List<Store> storeList = storeDAO.getAll();
        for (Store store : storeList) {
            storeDTOList.add(new StoreDTO(store.getStoId(),store.getCapacity(),store.getLocation()));
        }
        return storeDTOList;
    }

    public boolean deleteStore(String id) throws SQLException, ClassNotFoundException {
        return storeDAO.delete(id);
    }

    public boolean saveStore(StoreDTO storeDTO) throws SQLException, ClassNotFoundException {
        return storeDAO.save(new Store(storeDTO.getStoId(),storeDTO.getCapacity(),storeDTO.getLocation()));
    }

    public boolean updateStore(StoreDTO storeDTO) throws SQLException, ClassNotFoundException {
        return storeDAO.update(new Store(storeDTO.getStoId(),storeDTO.getCapacity(),storeDTO.getLocation()));
    }
}
