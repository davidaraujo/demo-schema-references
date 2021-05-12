/**
 * Copyright 2019 Confluent Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.confluent.demo.bankaccount.avro.producer;

import io.confluent.demo.bankaccount.avro.pojo.Deposit;
import io.confluent.demo.bankaccount.utils.ClientsUtils;
import io.confluent.demo.bankaccount.utils.ColouredSystemOutPrintln;
import io.confluent.demo.bankaccount.utils.PrettyPrint;
import org.apache.kafka.clients.producer.*;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class DepositService implements Runnable {

    private final String resourcesDir;
    private final String confluentPropsFile;
    private final String topicName;
    private final String clientId;

    public DepositService(String resourcesDir,
                        String confluentPropsFile,
                        String topicName,
                        String clientId) {
        this.resourcesDir = resourcesDir;
        this.confluentPropsFile = confluentPropsFile;
        this.topicName = topicName;
        this.clientId = clientId;
    }

    public void publishAircraftLocation()
            throws IOException, ExecutionException, InterruptedException {

        // ----------------------------- Load properties -----------------------------
        // Kafka and Schema Registry properties
        final Properties props = ClientsUtils.loadConfig(resourcesDir + "/" + confluentPropsFile);

        // ----------------------------- Set producer properties -----------------------------
        // Assign a client id to the producer
        if (clientId != null)
            props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        // Set the subject naming strategy to use
        //props.setProperty("value.subject.name.strategy", RecordNameStrategy.class.getName());

        // If we auto register a schema with references it will expand the references
        // within the main schema and not use references. As a result the registering of
        // schema references should be performed via the SR API or SR maven plugin
        props.setProperty("auto.register.schemas", "false");
        // Key serializer - String
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // Value serializer - KafkaAvroSerializer
        props.setProperty("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");

        // ----------------------------- Create Kafka topic and producer -----------------------------
        // topic creation (if it doesn't exist)
        ClientsUtils.createTopic(props, topicName);
        // producer creation
        Producer<String, Object> producer = new KafkaProducer<>(props);

        // ----------------------------- Produce aircraft location events to Kafka -----------------------------
        while (true) {
            try {
                Deposit value = DepositEvent.getDeposit();
                String key = ""+value.getAccountId();
                producer.send(new ProducerRecord<>(topicName, key, value), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata m, Exception e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            PrettyPrint.producerRecord(((clientId == null)) ? "Unidentified": clientId, topicName, m.partition(), m.offset(), key.toString(), value.toString(), "blue");
                        }
                    }
                });
                // producing interval
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                System.out.println(ColouredSystemOutPrintln.ANSI_BLACK + ColouredSystemOutPrintln.ANSI_BG_RED);
                //System.out.println(ex.toString());
                System.out.println(ex.getCause().getCause());
                ex.printStackTrace();
                System.out.println(ColouredSystemOutPrintln.ANSI_WHITE + ColouredSystemOutPrintln.ANSI_BG_RED);
                System.out.println(">>> Skipping bad record.");
                continue;
            }
        }
    }

    public static void main(final String[] args) throws IOException, ExecutionException, InterruptedException {

        int numArgs = args.length;
        if (numArgs < 3) {
            System.out.println("Please provide command line arguments: resourcesDir kafkaPropertiesFile topicName clientID(optional) numberThreads(optional)");
            System.exit(1);
        }
        String resourcesDir = args[0];
        String confluentPropsFile = args[1];
        String topicName = args[2];

        // clientId is optional
        String clientId = ((numArgs >= 4)) ? args[3] : null;

        // numberThreads is optional
        int numberThreads = ((numArgs == 5)) ? Integer.parseInt(args[4]) : 0;

        DepositService depositService = null;
        // run one producer thread
        if (numberThreads == 0) {
            depositService = new DepositService(resourcesDir, confluentPropsFile, topicName, clientId);
            new Thread(depositService).start();
        }
        // run multiple producer threads
        else for (int i = 0; i < numberThreads; i++) {
            depositService = new DepositService(resourcesDir, confluentPropsFile, topicName, clientId + "." + i);
            new Thread(depositService).start();
        }
        try {
            Thread.sleep(10L * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            this.publishAircraftLocation();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}