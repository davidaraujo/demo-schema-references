package io.confluent.demo.bankaccount.avro.producer;

import com.github.javafaker.Faker;
import io.confluent.demo.bankaccount.avro.pojo.Deposit;
import io.confluent.demo.bankaccount.avro.pojo.NewAccount;

public class DepositEvent {

    public static Deposit getDeposit() {
        Faker faker = new Faker();
        Deposit deposit = new Deposit();
        deposit.setAccountId(faker.number().randomNumber());
        deposit.setAmount(new Double(faker.commerce().price()));
        deposit.setDepositDate(System.currentTimeMillis());
        return deposit;
    }
}