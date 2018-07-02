package com.github.yt.kafka.simple;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Properties;

/**
 * kafka消费者.
 *
 * @author limiao
 */
public class KafkaConsumerExample {

    private static final String servers = "xj-71.yoyosys:9092,xj-72.yoyosys:9092,xj-73.yoyosys:9092";

    private static final String topic = "limiao";

    private static final String group = "test-group";

    @Test()
    public void test() {
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("group.id", group);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList(topic));
        System.out.println("Subscribed to topic " + topic);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());
            }
        }
    }

}