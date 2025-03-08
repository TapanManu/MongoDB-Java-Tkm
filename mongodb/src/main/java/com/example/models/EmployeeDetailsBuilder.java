package com.example.models;

public class EmployeeDetailsBuilder {

    String employee;
    int age;
    String department;

    public EmployeeDetailsBuilder withEmployee(String employee){
        this.employee = employee;
        return this;
    }

    public EmployeeDetailsBuilder withDepartment(String department){
        this.department = department;
        return this;
    }

    public EmployeeDetailsBuilder withAge(int age){
        this.age = age;
        return this;
    }

    public EmployeeDetails build(){
        return new EmployeeDetails(this.employee, this.age, this.department);
    }
    
}
