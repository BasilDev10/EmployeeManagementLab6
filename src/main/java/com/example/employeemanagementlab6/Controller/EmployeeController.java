package com.example.employeemanagementlab6.Controller;

import com.example.employeemanagementlab6.ApiResponse.ApiResponse;
import com.example.employeemanagementlab6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {


    ArrayList<Employee> employees = new ArrayList<>();


    public EmployeeController(){
        employees.add(new Employee("EMP123","Basil","basil@altasan.com","0512345678",33,"coordinator",false, LocalDate.of(2020, 1, 8),5));
        employees.add(new Employee("EMP444","Moahmmed","Moahmmed@altasan.com","0512345678",28,"supervisor",false, LocalDate.of(2024, 1, 8),30));
        employees.add(new Employee("EMP555","Sami","Sami@altasan.com","0512345678",35,"coordinator",false, LocalDate.of(2022, 1, 8),10));
        employees.add(new Employee("EMP666","Abdullah","Abdullah@altasan.com","0512345678",40,"coordinator",false, LocalDate.of(2020, 1, 8),40));
        employees.add(new Employee("EMP777","Anas","Anas@altasan.com","0512345678",30,"supervisor",false, LocalDate.of(2023, 1, 8),30));
    }

    @GetMapping("/get")
    public ResponseEntity getEmployee(){

        return ResponseEntity.ok(employees);
    }
    @GetMapping("/get-employee-by-position/{position}")
    public ResponseEntity getEmployee(@PathVariable String position){
        ArrayList<Employee> employeesFound = new ArrayList<>();
        for (Employee employee : employees){
            if(employee.getPosition().toLowerCase().equals(position.toLowerCase())){
                employeesFound.add(employee);
            }
        }
        return ResponseEntity.ok(employeesFound);
    }

    @GetMapping("/get-employee-by-range-age/{ageFrom}/{ageTo}")
    public ResponseEntity getEmployee(@PathVariable int ageFrom,@PathVariable int ageTo){
        ArrayList<Employee> employeesFound = new ArrayList<>();
        if(ageFrom < 25 || ageTo < 25)return ResponseEntity.status(400).body(new ApiResponse("Error:Make sure age from and to is greater then 25"));
        if(ageTo < ageFrom) return ResponseEntity.status(400).body(new ApiResponse("Error:Age From is greater then age to"));

        for (Employee employee : employees){
            if(employee.getAge() >= ageFrom && employee.getAge() <= ageTo) employeesFound.add(employee);
        }
        return ResponseEntity.ok(employeesFound);
    }

    @GetMapping("/get-no-annual-leave-employee")
    public ResponseEntity getEmployeeNoAnnualLeave(){
        ArrayList<Employee> employeesFound = new ArrayList<>();
        for (Employee employee : employees){
            if(employee.getAnnualLeave() == 0)employeesFound.add(employee);
        }
        return ResponseEntity.ok(employeesFound);
    }

    @PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee , Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }
        employees.add(employee);

        return ResponseEntity.status(201).body(new ApiResponse("Employee is added"));
    }
    @PutMapping("/update/{index}")
    public ResponseEntity updateEmployee(@PathVariable int index,@RequestBody @Valid Employee employee ,Errors errors){

        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        if(index >= employees.size() || index<0)return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));

        employees.set(index,employee);
        return ResponseEntity.ok(new ApiResponse("Employee is updated"));
    }
    @PutMapping("/update-annual-leave/{index}")
    public ResponseEntity updateEmployeeAnnualLeave(@PathVariable int index){
        if(index >= employees.size() || index<0)return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
        Employee employee = employees.get(index);
        if(employee.getAnnualLeave() < 1 )return ResponseEntity.status(400).body(new ApiResponse("Error:Employee don't have annual leave balance"));
        if(employee.isOnLeave())return ResponseEntity.status(400).body(new ApiResponse("Error:Employee is on leave"));

        employee.setAnnualLeave(employee.getAnnualLeave()-1);
        employee.setOnLeave(true);

        return ResponseEntity.status(200).body(new ApiResponse("Updated Employee Annual leave"));
    }

    @PutMapping("/promote-employee/{id}/{userRole}")
    public ResponseEntity promoteEmployee(@PathVariable String id ,@PathVariable String userRole){

        if(!userRole.toLowerCase().equals("supervisor"))return ResponseEntity.status(400).body(new ApiResponse("Error:Employee role not supervisor"));
        Employee employee = null;
        for (Employee employee1 : employees){
            if (employee1.getId().toLowerCase().equals(id.toLowerCase())){
                employee =employee1;
                break;
            }
        }

        if(employee == null)return ResponseEntity.status(400).body(new ApiResponse("Error: Employee not found with this id : "+id));
        if(employee.getPosition().toLowerCase().equals("supervisor"))return ResponseEntity.status(400).body(new ApiResponse("Error:Employee is already supervisor"));
        if(employee.getAge() < 30) return ResponseEntity.status(400).body(new ApiResponse("Error: Employee is less then 30"));
        if(employee.isOnLeave())return ResponseEntity.status(400).body(new ApiResponse("Error:Employee is on leave"));


        employee.setPosition("supervisor");

        return ResponseEntity.status(200).body(new ApiResponse("Employee is promoted"));
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity deleteEmployee(@PathVariable int index){
        if(index >= employees.size() || index<0)return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));

        employees.remove(index);
        return ResponseEntity.ok(new ApiResponse("Employee is deleted"));
    }
}
