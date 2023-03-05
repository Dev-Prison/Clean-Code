package yonghoon.ch03_ex1.employee;

public abstract class Employee {
    public abstract boolean isPayday();
    public abstract int calculatePay();
    public abstract void deliverPay(int pay);
}