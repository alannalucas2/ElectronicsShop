package com.example.alannalucas.assignment3.ElectronicsActivities;

public class CustomerBuilder {
    private String name;
    private String address;
    private String payment;
    private String cardSpinner;

    public CustomerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CustomerBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public CustomerBuilder setPayment(String payment) {
        this.payment = payment;
        return this;
    }

    public CustomerBuilder setCardSpinner(String cardSpinner) {
        this.cardSpinner = cardSpinner;
        return this;
    }

    public Customer createCustomer() {
        return new Customer(name, address, payment, cardSpinner);
    }
}