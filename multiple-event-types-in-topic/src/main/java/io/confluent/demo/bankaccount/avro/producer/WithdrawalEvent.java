package io.confluent.demo.bankaccount.avro.producer;

import com.github.javafaker.Faker;
import io.confluent.demo.bankaccount.avro.pojo.Deposit;
import io.confluent.demo.bankaccount.avro.pojo.Withdrawal;

public class WithdrawalEvent {

    public static Withdrawal getWithdrawal() {
        Faker faker = new Faker();
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAccountId(faker.number().randomNumber());
        withdrawal.setAmount(new Double(faker.commerce().price()));
        withdrawal.setWithdrawalDate(System.currentTimeMillis());
        return withdrawal;
    }
}