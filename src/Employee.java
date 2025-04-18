package src;
public class Employee {
    int id;
    String name;
    String designation;
    double basicSalary;
    int overtimeHours;
    double taxPercentage;

    public Employee(String name, String designation, double basicSalary, int overtimeHours, double taxPercentage) {
        this.name = name;
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.overtimeHours = overtimeHours;
        this.taxPercentage = taxPercentage;
    }
}
