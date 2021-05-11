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

package io.confluent.demo.aircraft.avro.producer;

import io.confluent.demo.aircraft.avro.pojo.AircraftState;
import io.confluent.demo.aircraft.utils.*;
import org.apache.kafka.clients.producer.*;
import org.opensky.model.StateVector;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class TrackingService implements Runnable {

    private final String resourcesDir;
    private final String confluentPropsFile;
    private final String openSkyPropsFile;
    private final String topicName;
    private final String clientId;
    private boolean doStop = false;

    public TrackingService(String resourcesDir,
                           String confluentPropsFile,
                           String openSkyPropsFile,
                           String topicName,
                           String clientId) {
        this.resourcesDir = resourcesDir;
        this.confluentPropsFile = confluentPropsFile;
        this.openSkyPropsFile = openSkyPropsFile;
        this.topicName = topicName;
        this.clientId = clientId;
    }

    public void publishAircraftLocation()
            throws IOException, ExecutionException, InterruptedException {

        // ----------------------------- Load properties -----------------------------
        // Kafka and Schema Registry properties
        final Properties props = ClientsUtils.loadConfig(resourcesDir + "/" + confluentPropsFile);
        // OpenSky credentials
        final Properties openSkyProps = ClientsUtils.loadConfig(resourcesDir + "/" + openSkyPropsFile);

        // ----------------------------- Set producer properties -----------------------------
        // Assign a client id to the producer
        if (clientId != null)
            props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        // Set the subject naming strategy to use
        //props.setProperty("value.subject.name.strategy", RecordNameStrategy.class.getName());
        // Auto register the schema
        props.setProperty("auto.register.schemas", "true");
        // Key serializer - String
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // Value serializer - KafkaAvroSerializer
        props.setProperty("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");

        // ----------------------------- Create Kafka topic and producer -----------------------------
        // topic creation (if it doesn't exist)
        TopicAdmin.createTopic(props, topicName);
        // producer creation
        Producer<String, Object> producer = new KafkaProducer<>(props);

        // ----------------------------- Connect to OpenSKY network API to get real-time aircraft location -----------------------------
        OpenSkyNetwork openSky = new OpenSkyNetwork(openSkyProps);
        openSky.connect();
        Collection states = openSky.getAircraftLocation();
        Iterator aircraftEvents = states.iterator();

        // ----------------------------- Produce aircraft location events to Kafka -----------------------------
        while (aircraftEvents.hasNext()) {
            try {
                AircraftState value = AircraftEvent.create((StateVector) aircraftEvents.next());
                String key = value.getCallsign() == null ? "" : value.getCallsign().toString();
                producer.send(new ProducerRecord<>(topicName, key, value), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata m, Exception e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            PrettyPrint.producerRecord(((clientId == null)) ? "Unidentified": clientId, topicName, m.partition(), m.offset(), key.toString(), value.toString(), "avro");
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
                // NOTE: some records are failing because fields are coming null, e.g. squawk field, and the Avro schema is not setup with null union for those
                // this is expected to showcase clients failing on serialization from incorrect data
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
            System.out.println("Please provide command line arguments: resourcesDir kafkaPropertiesFile openSkyPropertiesFile topicName clientID(optional) numberThreads(optional)");
            System.exit(1);
        }
        String resourcesDir = args[0];
        String confluentPropsFile = args[1];
        String openSkyPropsFile = args[2];
        String topicName = args[3];

        // clientId is optional
        String clientId = ((numArgs >= 5)) ? args[4] : null;

        // numberThreads is optional
        int numberThreads = ((numArgs == 6)) ? Integer.parseInt(args[5]) : 0;

        TrackingService airspaceInformation = null;
        // run one producer thread
        if (numberThreads == 0) {
            airspaceInformation = new TrackingService(resourcesDir, confluentPropsFile, openSkyPropsFile, topicName, clientId);
            new Thread(airspaceInformation).start();
        }
        // run multiple producer threads
        else for (int i = 0; i < numberThreads; i++) {
            airspaceInformation = new TrackingService(resourcesDir, confluentPropsFile, openSkyPropsFile, topicName, clientId + "." + i);
            new Thread(airspaceInformation).start();
        }

        try {
            Thread.sleep(10L * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        airspaceInformation.doStop();
    }

    public synchronized void doStop() {
        this.doStop = true;
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