package lk.ijse.bo;

import lk.ijse.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {}

    public static BOFactory getBoFactory() {
        return (boFactory==null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes{
        BATCH,BATCHCOST,BATCHINGREDIENT,CUSTOMER,EMPLOYEE,INGREDIENT,ORDERDETAIL,PAYMENT,PLACEORDER,QUERY,STORE,SUPPLIER
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case BATCH:
                return new BatchBOImpl();
            case BATCHCOST:
                return new BatchCostBOImpl();
            case BATCHINGREDIENT:
                return new BatchIngredientBOImpl();
            case CUSTOMER:
                return new CustomerBOImpl();
            case EMPLOYEE:
                return new EmployeeBOImpl();
            case INGREDIENT:
                return new IngredientBOImpl();
            case ORDERDETAIL:
                return new OrderDetailBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case PLACEORDER:
                return new PlaceOrderBOImpl();
            case QUERY:
                return new QueryBOImpl();
            case STORE:
                return new StoreBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            default:
                return null;
        }
    }
}
