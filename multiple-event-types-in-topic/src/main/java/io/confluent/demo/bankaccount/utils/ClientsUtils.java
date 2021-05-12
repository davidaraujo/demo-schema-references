package io.confluent.demo.bankaccount.utils;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ClientsUtils {

    public static Properties loadConfig(final String configFile) throws IOException {
        if (!Files.exists(Paths.get(configFile))) {
            throw new IOException(configFile + " not found.");
        }
        final Properties cfg = new Properties();
        try (InputStream inputStream = new FileInputStream(configFile)) {
            cfg.load(inputStream);
        }
        return cfg;
    }

    public static void createTopic(Properties props, String topicName) throws ExecutionException, InterruptedException {

        AdminClient adminClient = AdminClient.create(props);
        boolean topicExists = adminClient.listTopics().names().get().contains(topicName);

        if (!topicExists) {
            int partitions = new Integer(props.getProperty("num.partitions"));
            int replication = new Integer(props.getProperty("replication.factor"));
            NewTopic newTopic = new NewTopic(topicName, partitions, (short) replication);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        }
    }

    public static int sizeof(Object obj) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteOutputStream.toByteArray().length;
    }

    public static boolean isEven(int number) {
        if (((number % 2) == 0))  // even
            return true;
        else
            return false;
    }
}
