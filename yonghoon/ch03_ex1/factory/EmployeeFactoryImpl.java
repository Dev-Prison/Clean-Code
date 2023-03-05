package yonghoon.ch03_ex1.factory;

import yonghoon.ch03_ex1.employee.*;

public class EmployeeFactoryImpl implements EmployeeFactory{
    @Override
    public Employee makeEmployee(EmployeeRecord r) throws Exception {
        switch (r){
            case COMMISSION -> {
                return new CommissionedEmployee();
            }
            case HOURLY -> {
                return new HourlyEmployee();
            }
            case SALARIED -> {
                return new SalaredEmployee();
            }
            default -> throw new Exception();
        }
    }
}