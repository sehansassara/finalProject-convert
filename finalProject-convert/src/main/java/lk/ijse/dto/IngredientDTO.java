package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IngredientDTO {
    private String ingId;
    private String type;
    private double unitPrice;
    private double qtyOnHand;
}
