package io.confluent.demo.bankaccount.avro.producer;

import com.github.javafaker.Faker;
import io.confluent.demo.bankaccount.avro.pojo.NewAccount;

public class NewAccountEvent {

    public static NewAccount getNewAccount() {
        Faker faker = new Faker();
        NewAccount newAccount = new NewAccount();
        newAccount.setAccountId(faker.number().randomNumber());
        newAccount.setCustomerName(faker.name().fullName());
        newAccount.setCustomerAddress(faker.address().fullAddress());
        newAccount.setCustomerEmail(faker.internet().emailAddress());
        return newAccount;
    }
}