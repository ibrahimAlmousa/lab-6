package inspringboot.employeesystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "ID cannot be empty")
    @Size(min = 2, message = "ID must be at least 2 characters long")
    private String ID;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 4, message = "Name must be at least 4 characters long")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name can contain only letters and spaces")
    private String name;

    @Email(message = "Email must be a valid format")
    private String emile;

    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with '05' and contain exactly 10 digits")
    private String phoneNumber;

    @Min(value = 26, message = "Age must be greater than 25")
    private int age;

    @NotNull(message = "Position cannot be null")
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "Position must be either 'supervisor' or 'coordinator'")
    private String position;

    private boolean onLeave;

    @NotNull(message = "Hire date cannot be null")
    @PastOrPresent(message = "Hire date must be in the present or past")
    private LocalDate hireDate;

    @NotNull(message = "Annual leave cannot be null")
    @PositiveOrZero(message = "Annual leave must be a positive number")
    private int annualLeave;
}
