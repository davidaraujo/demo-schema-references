
Orders demo with Avro schema references
================


Create Avro POJOs for a schema with references:
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
                        <sourceDirectory>src/main/java/io/confluent/demo/orders/avro/schemas/</sourceDirectory>
                        <outputDirectory>src/main/java/</outputDirectory>
                        <imports>
                            <import>src/main/java/io/confluent/demo/orders/avro/schemas/product.avsc</import>
                            <import>src/main/java/io/confluent/demo/orders/avro/schemas/payment.avsc</import>
                            <import>src/main/java/io/confluent/demo/orders/avro/schemas/customer.avsc</import>
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
            <transaction.order.avro-value>src/main/java/io/confluent/demo/orders/avro/schemas/order.avsc</transaction.order.avro-value>
            <transaction.product>src/main/java/io/confluent/demo/orders/avro/schemas/product.avsc</transaction.product>
            <transaction.customer>src/main/java/io/confluent/demo/orders/avro/schemas/customer.avsc</transaction.customer>
            <transaction.payment>src/main/java/io/confluent/demo/orders/avro/schemas/payment.avsc</transaction.payment>
        </subjects>
        <schemaTypes>
            <transaction.order.avro-value>AVRO</transaction.order.avro-value>
            <transaction.product>AVRO</transaction.product>
            <transaction.customer>AVRO</transaction.customer>
            <transaction.payment>AVRO</transaction.payment>
        </schemaTypes>
        <references>
            <transaction.order.avro-value>
                <!-- IMPORTANT: the reference schema name is the fully qualified name -->
                <reference>
                    <name>io.confluent.demo.orders.avro.pojo.product</name>
                    <subject>transaction.product</subject>
                </reference>
                <reference>
                    <name>io.confluent.demo.orders.avro.pojo.customer</name>
                    <subject>transaction.customer</subject>
                </reference>
                <reference>
                    <name>io.confluent.demo.orders.avro.pojo.payment</name>
                    <subject>transaction.payment</subject>
                </reference>
            </transaction.order.avro-value>
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
[INFO] ----------------< io.confluent:one-event-type-in-topic >----------------
[INFO] Building one-event-type-in-topic 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- kafka-schema-registry-maven-plugin:5.5.1:register (default-cli) @ one-event-type-in-topic ---
[INFO] Registered subject(customer) with id 100200 version 1
[INFO] Registered subject(product) with id 100201 version 1
[INFO] Registered subject(payment) with id 100202 version 1
[INFO] Registered subject(order) with id 100203 version 1
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------

```

Run apps with schema references
-----
Producer:
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.orders.avro.producer.OrderService" -Dexec.args="./src/main/resources ccloud_prod_catalog.properties transaction.order.avro OrderService.avro"
```
Consumer:
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.orders.avro.consumer.GenericAvroConsumerService" -Dexec.args="./src/main/resources ccloud_prod_catalog.properties transaction.order.avro FulfillmentService.avro"
```

