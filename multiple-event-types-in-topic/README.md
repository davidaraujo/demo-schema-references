
# Multiple event types in the same topic with schema references and `TopicNameStrategy`

## Scenario 
A customer bank account represented as a time-ordered sequence of events, where the messages that contain those events have different data structures/schemas.

We'll work with events that describe the following operations:
* Creation of a new customer account
* Making a deposit 
* Withdrawal of money 

As a chain of events that belong together, we'll store them in the same Kafka topic on [Confluent Cloud](https://login.confluent.io/).

![Alt text](multiple_events_same_topic.png?raw=true "Title")

## Setup and build

### Schemas

#### Bank account schema (main)
```json
{
 "type": "record",
 "namespace": "io.confluent.demo.bankaccount.avro.pojo",
 "name": "BankAccount",
 "fields": [
   {
     "name": "oneof_type",
     "type": [
       "io.confluent.demo.bankaccount.avro.pojo.NewAccount",
       "io.confluent.demo.bankaccount.avro.pojo.Deposit",
       "io.confluent.demo.bankaccount.avro.pojo.Withdrawal"
    ]
   }
 ]
 }
```
#### New account schema (reference)
```json
{
  "type": "record",
  "namespace": "io.confluent.demo.bankaccount.avro.pojo",
  "name": "NewAccount",
  "fields": [
    {"name": "account_id", "type": "long"},
    {"name": "customer_name", "type": "string"},
    {"name": "customer_email", "type": "string"},
    {"name": "customer_address", "type": "string"},
    {"name": "account_creation_date", "type": "long", "logicalType": "date"}
  ]
}
```
#### Deposit schema (reference)
```json
{
  "type": "record",
  "namespace": "io.confluent.demo.bankaccount.avro.pojo",
  "name": "Deposit",
  "fields": [
    {"name": "account_id", "type": "long"},
    {"name": "amount", "type": "double"},
    {"name": "deposit_date", "type": "long", "logicalType": "date"}
  ]
}
```
#### Withdrawal schema (reference)
```json
{
  "type": "record",
  "namespace": "io.confluent.demo.bankaccount.avro.pojo",
  "name": "Withdrawal",
  "fields": [
    {"name": "account_id", "type": "long"},
    {"name": "amount", "type": "double"},
    {"name": "withdrawal_date", "type": "long", "logicalType": "date"}
  ]
}
```

### Generate POJOs 
Avro plugin on the pom.xml:
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
Run to generate sources:
```
mvn generate-sources
```

### Register schemas
Confluent Schema registry plugin on the pom.xml:
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
            <io.confluent.demo.bankaccount.avro.pojo.Random>src/main/java/io/confluent/demo/bankaccount/avro/schemas/random.avsc</io.confluent.demo.bankaccount.avro.pojo.Random>
        </subjects>
        <schemaTypes>
            <customer.bankaccount.avro-value>AVRO</customer.bankaccount.avro-value>
            <customer.newaccount>AVRO</customer.newaccount>
            <customer.deposit>AVRO</customer.deposit>
            <customer.withdrawal>AVRO</customer.withdrawal>
            <io.confluent.demo.bankaccount.avro.pojo.Random>AVRO</io.confluent.demo.bankaccount.avro.pojo.Random>
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
Run to register:
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
[INFO] Registered subject(io.confluent.demo.bankaccount.avro.pojo.Random) with id 100049 version 1
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### Build
Run to compile:
```
mvn compile
```

## Run 

### New account service application (producer)
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.producer.NewAccountService" -Dexec.args="./src/main/resources <CCLOUD_PROPERTIES> customer.bankaccount.avro NewAccountService.avro"
```

### Deposit service application (producer)
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.producer.DepositService" -Dexec.args="./src/main/resources <CCLOUD_PROPERTIES> customer.bankaccount.avro DepositService.avro"
```

### Withdrawal service application (producer)
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.producer.WithdrawalService" -Dexec.args="./src/main/resources <CCLOUD_PROPERTIES> customer.bankaccount.avro WithdrawalService.avro"
```

### Balance service application (consumer)
The consumer application reads the 3 different event types from the topic produced by the above producer applications.
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.consumer.GenericAvroConsumerService" -Dexec.args="./src/main/resources <CCLOUD_PROPERTIES> customer.bankaccount.avro BalanceService.avro"
```

### Random application (producer) 
The random producer application is configured to use `RecordNameStrategy` and if the topic is running on a **Kafka dedicated cluster** with `confluent.value.schema.validation=true` and `confluent.key.subject.name.strategy
=io.confluent.kafka.serializers.subject.TopicNameStrategy`, all the records produced will fail.

```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.bankaccount.avro.producer.RandomService" -Dexec.args="./src/main/resources <CCLOUD_PROPERTIES> customer.bankaccount.avro RandomService.avro"
```

## Read more
* [Putting Several Event Types in the Same Topic – Revisited](https://www.confluent.io/blog/multiple-event-types-in-the-same-kafka-topic/)
* [Schema References](https://docs.confluent.io/platform/current/schema-registry/serdes-develop/index.html#schema-references)
* [Broker-Side Schema Validation on Confluent Cloud](https://docs.confluent.io/cloud/current/client-apps/schemas-manage.html#using-broker-side-schema-validation-on-ccloud)

## License
This project is licensed under the [Apache 2.0 License](./LICENSE).
