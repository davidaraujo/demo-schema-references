
# One event type in the same topic with schema references 

## Scenario
An order composed of multiple parts represented with different data structures/schemas.

Our order event is composed of:
* Order details
* One of more products 
* Customer details
* Payment details

![Alt text](order.png?raw=true "Title")

## Setup and build

### Schemas

#### Order schema (main)
```json
{
  "type": "record",
  "namespace": "io.confluent.demo.orders.avro.pojo",
  "name": "Order",
  "fields": [
    {"name": "order_id", "type": "long", "doc": "The id of the order."},
    {"name": "order_date", "type": "long", "logicalType": "date"},
    {"name": "order_amount", "type": "double"},
    {
      "name": "products",
      "type": {
        "type": "array",
        "items": {
          "name": "product",
          "type": "io.confluent.demo.orders.avro.pojo.Product"
        }
      }
    },
    {
      "name": "customer",
      "type": "io.confluent.demo.orders.avro.pojo.Customer"
    },
    {
      "name": "payment_method",
      "type": "io.confluent.demo.orders.avro.pojo.Payment"
    }
  ]
}
```

#### Product schema (reference)
```json
{
 "type": "record",
 "namespace": "io.confluent.demo.orders.avro.pojo",
 "name": "Product",

 "fields": [
     {"name": "product_id", "type": "long"},
     {"name": "product_name", "type": "string"},
     {"name": "product_price", "type": "double"}
 ]
}
```

#### Customer schema (reference)
```json
{
 "type": "record",
 "namespace": "io.confluent.demo.orders.avro.pojo",
 "name": "Customer",

 "fields": [
     {"name": "customer_id", "type": "long"},
     {"name": "customer_name", "type": "string"},
     {"name": "customer_email", "type": "string"},
     {"name": "customer_address", "type": "string"}
 ]
}
```

#### Payment schema (reference)
```json
{
 "type": "record",
 "namespace": "io.confluent.demo.orders.avro.pojo",
 "name": "Payment",
 "fields": [
     {"name": "payment_method_code", "type": "int"},
     {"name": "card_number", "type": "long"},
     {"name": "expiration_date", "type": "string", "default": "00/00"},
     {"name": "cvv", "type": "int"}
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
Run to register:
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

### Build
Run to compile:
```
mvn compile
```

## Run

### Order service application (producer)
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.orders.avro.producer.OrderService" -Dexec.args="./src/main/resources <CCLOUD_PROPERTIES> transaction.order.avro OrderService.avro"
```

### Fulfillment service application (consumer)
```bash
mvn exec:java -Dexec.mainClass="io.confluent.demo.orders.avro.consumer.GenericAvroConsumerService" -Dexec.args="./src/main/resources <CCLOUD_PROPERTIES> transaction.order.avro FulfillmentService.avro"
```

## Read more
* [Schema References](https://docs.confluent.io/platform/current/schema-registry/serdes-develop/index.html#schema-references)

## License
This project is licensed under the [Apache 2.0 License](./LICENSE).
