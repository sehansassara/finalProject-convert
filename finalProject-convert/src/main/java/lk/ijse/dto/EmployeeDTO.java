package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDTO {
    private String empId;
    private String  firstName;
    private String lastName;
    private String address;
    private String tel;
    private double salary;
    private String position;

}
