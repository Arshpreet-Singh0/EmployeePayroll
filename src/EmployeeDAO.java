package src;

import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class EmployeeDAO {
    public void addEmployee(Employee e) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO employees (name, designation, basic_salary, overtime_hours, tax_percentage) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, e.name);
            ps.setString(2, e.designation);
            ps.setDouble(3, e.basicSalary);
            ps.setInt(4, e.overtimeHours);
            ps.setDouble(5, e.taxPercentage);
            ps.executeUpdate();
            System.out.println("✅ Employee added successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void generatePayslip(int empId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String designation = rs.getString("designation");
                double basic = rs.getDouble("basic_salary");
                int overtime = rs.getInt("overtime_hours");
                double tax = rs.getDouble("tax_percentage");

                double overtimePay = overtime * 100;
                double gross = basic + overtimePay;
                double net = gross - (gross * tax / 100);

                File dir = new File("payslips");
                if (!dir.exists())
                    dir.mkdir();

                FileWriter writer = new FileWriter("payslips/" + name.replace(" ", "_") + "_payslip.txt");
                writer.write("===== PAYSLIP =====\n");
                writer.write("Name: " + name + "\n");
                writer.write("Designation: " + designation + "\n");
                writer.write("Basic Salary: ₹" + basic + "\n");
                writer.write("Overtime Pay (₹100/hr): ₹" + overtimePay + "\n");
                writer.write("Gross Salary: ₹" + gross + "\n");
                writer.write("Tax (" + tax + "%): ₹" + (gross * tax / 100) + "\n");
                writer.write("Net Salary: ₹" + net + "\n");
                writer.close();

                System.out.println("✅ Payslip generated in /payslips folder.");
            } else {
                System.out.println("❌ Employee not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateFancyPayslip(int empId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String designation = rs.getString("designation");
                double salary = rs.getDouble("basic_salary");
                int overtimeHours = rs.getInt("overtime_hours");
                double taxPercentage = rs.getDouble("tax_percentage");
                double totalSalary = calculateSalary(salary, overtimeHours, taxPercentage);

                String payslipHtml = generatePayslipHTML(name, designation, salary, overtimeHours, taxPercentage,
                        totalSalary);

                try (FileWriter writer = new FileWriter("payslips/" + name + "_Payslip.html")) {
                    writer.write(payslipHtml);
                    System.out.println("Payslip generated for " + name + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("❌ Error saving payslip file.");
                }
            } else {
                System.out.println("❌ Employee not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateSalary(double salary, int overtimeHours, double taxPercentage) {
        double overtimePay = overtimeHours * 100; 
        double grossSalary = salary + overtimePay;
        double tax = grossSalary * (taxPercentage / 100);
        return grossSalary - tax;
    }

    private String generatePayslipHTML(String name, String designation, double salary, int overtimeHours,
            double taxPercentage, double totalSalary) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 20px; }" +
                "h1 { color: #4CAF50; }" +
                "table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
                "th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }" +
                "th { background-color: #f2f2f2; }" +
                ".btn-print { background-color: #4CAF50; color: white; padding: 10px 20px; margin-top: 20px; border: none; cursor: pointer; font-size: 16px; }"
                +
                ".btn-print:hover { background-color: #45a049; }" +
                "</style>" +
                "<script>" +
                "function printPayslip() { window.print(); }" +
                "</script>" +
                "</head>" +
                "<body>" +
                "<h1>Employee Payslip</h1>" +
                "<table>" +
                "<tr><th>Name</th><td>" + name + "</td></tr>" +
                "<tr><th>Designation</th><td>" + designation + "</td></tr>" +
                "<tr><th>Basic Salary</th><td>₹" + salary + "</td></tr>" +
                "<tr><th>Overtime Hours</th><td>" + overtimeHours + " hrs</td></tr>" +
                "<tr><th>Tax Percentage</th><td>" + taxPercentage + "%</td></tr>" +
                "<tr><th>Total Salary</th><td>₹" + totalSalary + "</td></tr>" +
                "</table>" +
                "<button class='btn-print' onclick='printPayslip()'>🖨️ Print Payslip</button>" +
                "<footer><p>Generated by Employee Payroll System</p></footer>" +
                "</body>" +
                "</html>";
    }

    public void viewAllEmployees() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n==== All Employees ====");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Designation: " + rs.getString("designation") +
                        ", Salary: ₹" + rs.getDouble("basic_salary") +
                        ", OT Hours: " + rs.getInt("overtime_hours") +
                        ", Tax: " + rs.getDouble("tax_percentage") + "%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(int id, int field, Scanner sc) {
        String column = "";
        switch (field) {
            case 1:
                column = "name";
                break;
            case 2:
                column = "designation";
                break;
            case 3:
                column = "basic_salary";
                break;
            case 4:
                column = "overtime_hours";
                break;
            case 5:
                column = "tax_percentage";
                break;
            default:
                System.out.println("❌ Invalid option");
                return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE employees SET " + column + " = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            System.out.print("Enter new value: ");
            if (field == 1 || field == 2)
                ps.setString(1, sc.next());
            else if (field == 4)
                ps.setInt(1, sc.nextInt());
            else
                ps.setDouble(1, sc.nextDouble());

            ps.setInt(2, id);
            int updated = ps.executeUpdate();

            if (updated > 0) {
                System.out.println("✅ Employee detail updated.");
            } else {
                System.out.println("❌ Employee not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchEmployeeById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                printEmployee(rs);
            } else {
                System.out.println("❌ No employee found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchEmployeeByName(String name) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees WHERE name LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                printEmployee(rs);
            }
            if (!found) {
                System.out.println("❌ No employee found with name: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployeeById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM employees WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Employee deleted successfully.");
            } else {
                System.out.println("❌ No employee found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printEmployee(ResultSet rs) throws SQLException {
        System.out.printf("ID: %d | Name: %s | Designation: %s | Salary: %.2f | OT Hours: %d | Tax: %.2f%%\n",
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("designation"),
                rs.getDouble("basic_salary"),
                rs.getInt("overtime_hours"),
                rs.getDouble("tax_percentage"));
    }

}
