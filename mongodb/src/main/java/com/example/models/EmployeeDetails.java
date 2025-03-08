package com.example.models;

public class EmployeeDetails {
    String employee;
    int age;
    String department;

    public String getEmployee(){
        return this.employee;
    }

    public int getAge(){
        return this.age;
    }

    public String getDepartment(){
        return this.department;
    }

    public void setEmployee(String employee){
        this.employee = employee;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public EmployeeDetails(String employee, int age, String department){
        setEmployee(employee);
        setDepartment(department);
        setAge(age);
    }

}
