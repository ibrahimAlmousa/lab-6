package inspringboot.employeesystem.Controller;

import inspringboot.employeesystem.ApiResponse.ApiResponse;
import inspringboot.employeesystem.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();


    @GetMapping("/get-employee")
    public ResponseEntity<?> getEmployee(){
        if(!employees.isEmpty()){
           return ResponseEntity.status(200).body(employees);
        }
        return ResponseEntity.status(400).body(new ApiResponse("no employee registers"));
    }


    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestBody ArrayList<@Valid Employee> employeeList, Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        employees.addAll(employeeList);

        return ResponseEntity.status(200).body(new ApiResponse("Employees added successfully"));
    }


    @PutMapping("/update-employee/{index}")
    public ResponseEntity<?> updateEmployee(@PathVariable int index , @RequestBody @Valid Employee employee , Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employees.set(index , employee);
        return ResponseEntity.status(200).body(new ApiResponse("update successfully"));
    }

    @DeleteMapping("/delete-employee/{index}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int index){
        if(index>=0 &&index<employees.size())
            return ResponseEntity.status(404).body(new ApiResponse("not found employee"));
        employees.remove(index);
        return ResponseEntity.status(200).body(new ApiResponse("delete successfully"));
    }

    @GetMapping("/search-by-position/{position}")
    public ResponseEntity<?> searchByPosition(@PathVariable String position) {
        ArrayList<Employee> positionEmployees = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getPosition().equalsIgnoreCase(position)) {
                positionEmployees.add(e);
            }
        }

        if (positionEmployees.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No employees found with this position"));
        }

        return ResponseEntity.status(200).body(positionEmployees);
    }


    @GetMapping("/search-by-age/{min}/{max}")
    public ResponseEntity<?> getEmployeesByAgeRange(@PathVariable int min, @PathVariable int max) {
        if (min < 0 || max < 0 || min > max) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid age range values"));
        }
        ArrayList<Employee> result = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getAge() >= min && e.getAge() <= max) {
                result.add(e);
            }
        }
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No employees found in this age range"));
        }
        return ResponseEntity.status(200).body(result);
    }


    @PutMapping("/leave/{ID}")
    public ResponseEntity<?> leave(@PathVariable String ID) {
        for (Employee e : employees) {
            if (e.getID().equals(ID)) {
                if (!e.isOnLeave()) {
                    if (e.getAnnualLeave() > 0) {
                        e.setAnnualLeave(e.getAnnualLeave() - 1);
                        e.setOnLeave(true);
                        return ResponseEntity.status(200).body(new ApiResponse("Employee is now on leave. Annual leave decreased by 1."));
                    } else {
                        return ResponseEntity.status(400).body(new ApiResponse("No remaining annual leave."));
                    }
                } else {
                    return ResponseEntity.status(400).body(new ApiResponse("Employee is already on leave."));
                }
            }
        }

        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }



    @GetMapping("/employee-with-no-leave")
    public ResponseEntity<?> employeeWithNoLeave() {
        ArrayList<Employee> employeesWithNoLeave = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getAnnualLeave() == 0) {
                employeesWithNoLeave.add(e);
            }
        }

        if (employeesWithNoLeave.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No employees found with zero annual leave"));
        }

        return ResponseEntity.status(200).body(employeesWithNoLeave);
    }


    @PutMapping("/promote-employee/{IDSupervisor}/{IDCoordinator}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String IDSupervisor, @PathVariable String IDCoordinator) {

        Employee supervisor  = null ;
        Employee coordinator = null ;

        for (Employee e : employees) {
            if (e.getID().equals(IDSupervisor) && e.getPosition().equals("supervisor")) {
                supervisor = e;
                break;
            }
        }



        for (Employee e : employees) {
            if (e.getID().equals(IDCoordinator)) {
                coordinator = e;
                break;
            }
        }


        if (coordinator.getPosition().equals("supervisor")) {
            return ResponseEntity.status(400).body(new ApiResponse("Employee is already a supervisor"));
        }

        if (coordinator.getAge() < 30) {
            return ResponseEntity.status(400).body(new ApiResponse("Coordinator must be at least 30 years old to be promoted"));
        }

        coordinator.setPosition("supervisor");
        return ResponseEntity.status(200).body(new ApiResponse("Employee promoted to supervisor successfully"));
    }

}
