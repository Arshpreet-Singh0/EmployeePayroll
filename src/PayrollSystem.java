package src;

import java.util.Scanner;

public class PayrollSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmployeeDAO dao = new EmployeeDAO();

        while (true) {
            System.out.println("\n==== Employee Payroll System ====");
            System.out.println("1. Add Employee");
            System.out.println("2. Generate Payslip");
            System.out.println("3. View All Employees");
            System.out.println("4. Update Employee Details");
            System.out.println("5. Delete Employee");
            System.out.println("6. Search Employee");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Designation: ");
                    String desig = sc.nextLine();
                    System.out.print("Enter Basic Salary: ");
                    double salary = sc.nextDouble();
                    System.out.print("Enter Overtime Hours: ");
                    int hours = sc.nextInt();
                    System.out.print("Enter Tax Percentage: ");
                    double tax = sc.nextDouble();

                    Employee emp = new Employee(name, desig, salary, hours, tax);
                    dao.addEmployee(emp);
                    break;

                case 2:
                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();
                    dao.generateFancyPayslip(empId);
                    break;

                case 3:
                    dao.viewAllEmployees();
                    break;

                case 4:
                    System.out.print("Enter Employee ID to Update: ");
                    int id = sc.nextInt();
                    System.out.println("What do you want to update?");
                    System.out.println("1. Name");
                    System.out.println("2. Designation");
                    System.out.println("3. Basic Salary");
                    System.out.println("4. Overtime Hours");
                    System.out.println("5. Tax Percentage");
                    System.out.print("Enter choice: ");
                    int updateChoice = sc.nextInt();
                    dao.updateEmployee(id, updateChoice, sc);
                    break;

                case 5:
                    System.out.print("Enter Employee ID to Delete: ");
                    int deleteId = sc.nextInt();
                    dao.deleteEmployeeById(deleteId);
                    break;

                case 6:
                    sc.nextLine(); // clear buffer
                    System.out.println("Search by: 1. ID  2. Name");
                    int searchOption = sc.nextInt();
                    sc.nextLine(); // clear buffer
                    if (searchOption == 1) {
                        System.out.print("Enter Employee ID: ");
                        int searchId = sc.nextInt();
                        dao.searchEmployeeById(searchId);
                    } else if (searchOption == 2) {
                        System.out.print("Enter Employee Name: ");
                        String searchName = sc.nextLine();
                        dao.searchEmployeeByName(searchName);
                    } else {
                        System.out.println("❌ Invalid Search Option!");
                    }
                    break;

                case 7:
                    System.out.println("👋 Exiting...");
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}
