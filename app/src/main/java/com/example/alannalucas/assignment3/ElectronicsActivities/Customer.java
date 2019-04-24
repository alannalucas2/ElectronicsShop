package com.example.alannalucas.assignment3.ElectronicsActivities;

public class Customer {

    public String name;
    public String address;
    public String payment;
    public String cardSpinner;

    public Customer(){

    }

    public Customer(String name, String address, String payment, String cardSpinner) {
        this.name = name;
        this.address = address;
        this.payment = payment;
        this.cardSpinner = cardSpinner;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCardSpinner() {
        return cardSpinner;
    }

    public void setCardSpinner(String cardSpinner) {
        this.cardSpinner = cardSpinner;
    }
}
