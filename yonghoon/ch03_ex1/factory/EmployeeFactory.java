package yonghoon.ch03_ex1.factory;

import yonghoon.ch03_ex1.employee.Employee;
import yonghoon.ch03_ex1.employee.EmployeeRecord;

public interface EmployeeFactory{
    public Employee makeEmployee (EmployeeRecord r) throws Exception;
}