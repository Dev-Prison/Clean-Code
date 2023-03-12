package yonghoon.ch03_ex1.employee;

public class CommissionedEmployee extends Employee{
    @Override
    public boolean isPayday() {
        return false;
    }
    @Override
    public int calculatePay() {
        return 0;
    }
    @Override
    public void deliverPay(int pay) {

    }
}