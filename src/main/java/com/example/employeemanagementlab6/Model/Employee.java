package com.example.employeemanagementlab6.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "id: cannot be empty!")
    @Size(min = 2 ,message = "id: length must more then 2")
    private String id;
    @NotEmpty(message = "name: cannot be empty!")
    @Size(min = 4 ,message = "name: length must more then 4")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "name:Must contain only characters (no numbers). ")
    private String name;
    @Email(message = "email:must be a valid email format")
    private String email;
    @Pattern(regexp = "^05\\d{8}$",message = "phone number: \n-Must start with 05.\n" +
            "- Must consists of exactly 10 digits. ")
    private String phoneNumber;
    @NotNull(message = "age: cannot be null")
    @Positive
    @NumberFormat(style = NumberFormat.Style.NUMBER )
    @Min(value = 25,message = "age: only accept ages more then 25")
    private int age;
    @NotEmpty(message = "position: cannot be empty!")
    @Pattern(regexp = "\\b(?:supervisor|coordinator)\\b", message = "Must be either supervisor or coordinator only. ")
    private String position;
   @AssertFalse(message = "on leave :Must be initially set to false. ")
    private boolean onLeave;
    @NotNull(message = "Hire Date: cannot be null")
    @PastOrPresent(message = "Hire Date:should be a date in the present or the past. ")
    private LocalDate hireDate;
    @NotNull(message = "Annual Leave Date: cannot be null")
    @Positive
    private int annualLeave;
}
