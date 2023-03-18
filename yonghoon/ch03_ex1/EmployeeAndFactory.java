package yonghoon.ch03_ex1;

import yonghoon.ch03_ex1.employee.*;
import yonghoon.ch03_ex1.factory.EmployeeFactory;
import yonghoon.ch03_ex1.factory.EmployeeFactoryImpl;

public class EmployeeAndFactory {
    public static void main(String[] args) {
        Employee employee;
        EmployeeFactory employeeFactory = new EmployeeFactoryImpl();
        try{
            employee = employeeFactory.makeEmployee(EmployeeRecord.SALARIED);
            employee.calculatePay();
            employee.deliverPay(10);
            employee.isPayday();

        }catch (Exception e){
            System.out.println("employee type is wrong");
        }
    }
}
