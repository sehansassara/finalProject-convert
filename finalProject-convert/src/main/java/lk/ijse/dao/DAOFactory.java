package lk.ijse.dao;

import lk.ijse.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {}

    public static DAOFactory getDaoFactory() {
        return (daoFactory==null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes{
        BATCH,BATCHINGREDIENT,BATCHEMPLOYEE,CUSTOMER,EMPLOYEE,INGREDIENT,ORDER,ORDERDETAIL,PAYMENT,STORE,SUPPLIE,QUERY
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case BATCH:
                return new BatchDAOImpl();
            case BATCHINGREDIENT:
                return new BatchIngredientDAOImpl();
            case BATCHEMPLOYEE:
                return new BatchEmployeeDetailDAOImpl();
            case CUSTOMER:
                return new CustomerDAOImpl();
            case EMPLOYEE:
                return new EmployeeDAOImpl();
            case INGREDIENT:
                return new IngredientDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDERDETAIL:
                return new OrderDetailDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case STORE:
                return new StoreDAOImpl();
            case SUPPLIE:
                return new SupplierDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            default:
                return null;
        }
    }
}
