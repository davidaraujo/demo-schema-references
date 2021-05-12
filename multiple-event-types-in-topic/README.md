
Bank account multiple event types in same topic with schema references demo
================


Avro
----------
---

Create POJOs for a schema with references:
-------------
pom.xml:
```xml
<plugin>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro-maven-plugin</artifactId>
    <version>${avro-maven-plugin}</version>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <goals>
                <goal>schema</goal>
            </goals>
            <configuration>
                <sourceDirectory>src/main/java/io/confluent/demo/bankaccount/avro/schemas/</sourceDirectory>
                <outputDirectory>src/main/java/</outputDirectory>
                <imports>
                    <import>src/main/java/io/confluent/demo/bankaccount/avro/schemas/newaccount.avsc</import>
                    <import>src/main/java/io/confluent/demo/bankaccount/avro/schemas/deposit.avsc</import>
                    <import>src/main/java/io/confluent/demo/bankaccount/avro/schemas/withdrawal.avsc</import>
                </imports>
            </configuration>
        </execution>
    </executions>
</plugin>
```
Run:
```
mvn generate-sources
```

Register a schema with references using the Schema Registry maven plugin:
-------------
pom.xml:
```xml
<plugin>
    <groupId>io.confluent</groupId>
    <artifactId>kafka-schema-registry-maven-plugin</artifactId>
    <version>${confluent.version}</version>
    <configuration>
        <schemaRegistryUrls>
            <param>${schema.registry.url}</param>
        </schemaRegistryUrls>
        <userInfoConfig>${schema.registry.basic.auth.user.info}</userInfoConfig>
        <outputDirectory>schemas</outputDirectory>
        <subjects>
            <customer.bankaccount.avro-value>src/main/java/io/confluent/demo/bankaccount/avro/schemas/bankaccount.avsc</customer.bankaccount.avro-value>
            <customer.newaccount>src/main/java/io/confluent/demo/bankaccount/avro/schemas/newaccount.avsc</customer.newaccount>
            <customer.deposit>src/main/java/io/confluent/demo/bankaccount/avro/schemas/deposit.avsc</customer.deposit>
            <customer.withdrawal>src/main/java/io/confluent/demo/bankaccount/avro/schemas/withdrawal.avsc</customer.withdrawal>
            <customer.random>src/main/java/io/confluent/demo/bankaccount/avro/schemas/random.avsc</customer.random>
        </subjects>
        <schemaTypes>
            <customer.bankaccount.avro-value>AVRO</customer.bankaccount.avro-value>
            <customer.newaccount>AVRO</customer.newaccount>
            <customer.deposit>AVRO</customer.deposit>
            <customer.withdrawal>AVRO</customer.withdrawal>
            <customer.random>AVRO</customer.random>
        </schemaTypes>
        <references>
            <customer.bankaccount.avro-value>
                <!-- IMPORTANT: the reference schema name is the fully qualified name -->
                <reference>
                    <name>io.confluent.demo.bankaccount.avro.pojo.newaccount</name>
                    <subject>customer.newaccount</subject>
                </reference>
                <reference>
                    <name>io.confluent.demo.bankaccount.avro.pojo.deposit</name>
                    <subject>customer.deposit</subject>
                </reference>
                <reference>
                    <name>io.confluent.demo.bankaccount.avro.pojo.withdrawal</name>
                    <subject>customer.withdrawal</subject>
                </reference>
            </customer.bankaccount.avro-value>
        </references>
    </configuration>
</plugin>
```
Run:
```
mvn schema-registry:register
```
Output:
```shell
[INFO] Scanning for projects...
[INFO]
[INFO] -------------< io.confluent:multiple-event-types-in-topic >-------------
[INFO] Building multiple-event-types-in-topic 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- kafka-schema-registry-maven-plugin:5.5.1:register (default-cli) @ multiple-event-types-in-topic ---
[INFO] Registered subject(customer.newaccount) with id 100045 version 1
[INFO] Registered subject(customer.deposit) with id 100046 version 1
[INFO] Registered subject(customer.withdrawal) with id 100047 version 1
[INFO] Registered subject(customer.bankaccount.avro-value) with id 100048 version 1
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

Run apps with schema references
-----
New Account Producer :
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.producer.NewAccountService" -Dexec.args="./src/main/resources ccloud_prod_catalog.properties customer.bankaccount.avro NewAccountService.avro"
```
Deposit Producer :
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.producer.DepositService" -Dexec.args="./src/main/resources ccloud_prod_catalog.properties customer.bankaccount.avro DepositService.avro"
```
Withdrawal Producer :
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.producer.WithdrawalService" -Dexec.args="./src/main/resources ccloud_prod_catalog.properties customer.bankaccount.avro WithdrawalService.avro"
```
Random Producer :
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.producer.RandomService" -Dexec.args="./src/main/resources ccloud_prod_catalog.properties customer.bankaccount.avro RandomService.avro"
```


Consumer:
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.consumer.GenericAvroConsumerService" -Dexec.args="./src/main/resources ccloud_prod_catalog.properties customer.bankaccount.avro BalanceService.avro"
```

