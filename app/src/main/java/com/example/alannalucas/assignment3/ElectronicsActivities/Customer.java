package com.example.alannalucas.assignment3.ElectronicsActivities;

public class Customer {

    public String name;
    public String address;
    public String payment;
    public String cardSpinner;

    //creating a customer with the builder pattern

    private Customer(final Builder builder) {
        name = builder.name;
        address = builder.address;
        payment = builder.payment;

        new Customer.Builder()
                .setName("Leonardo")
                .setAddress("da Vinci")
                .setPayment("123456")
                .setCardSpinner("Debit Card")
                .create();
    }



    static class Builder{
        private String name;
        private String address;
        private String payment;
        private String cardSpinner;

        public Builder setName(final String name){
            this.name = name;
            return this;
        }


        public Builder setAddress(final String address) {
            this.address = address;
            return this;
        }

        public Builder setPayment(final String payment) {
            this.payment = payment;
            return this;
        }

        public Builder setCardSpinner(final String cardSpinner) {
            this.cardSpinner = cardSpinner;
            return this;
        }

        public Customer create(){
            return new Customer(this);
        }
    }


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
