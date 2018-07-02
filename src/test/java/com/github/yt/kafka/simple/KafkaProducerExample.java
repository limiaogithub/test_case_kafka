package com.github.yt.kafka.simple;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.testng.annotations.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * kafka生产者.
 *
 * @author limiao
 */
public class KafkaProducerExample {

    private static final String servers = "xj-71.yoyosys:9092,xj-72.yoyosys:9092,xj-73.yoyosys:9092";

    private static final String topic = "limiao";

    private static final boolean isAsync = false;

    @Test
    public void test() {
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);

        /*
            acks：配置可以设定发送消息后是否需要Broker端返回确认
            0：不需要进行确认，速度最快。存在丢失数据的风险。
            1：仅需要Leader进行确认，不需要ISR进行确认。是一种效率和安全折中的方式。
            all：需要ISR中所有的Replica给予接收确认，速度最慢，安全性最高，但是由于ISR可能会缩小到仅包含一个Replica，所以设置参数为all并不能一定避免数据丢失。
        */
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<Integer, String> producer = new KafkaProducer<>(props);

        for (int messageNo = 1; messageNo < 1000; messageNo++) {
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            if (isAsync) {
                producer.send(new ProducerRecord<>(topic,
                        messageNo,
                        messageStr), new ProducerCallBack(startTime, messageNo, messageStr));
            } else {
                try {
                    RecordMetadata recordMetadata = producer.send(new ProducerRecord<>(topic,
                            messageNo,
                            messageStr)).get();
                    System.out.println("Sent message: (" + messageNo + ", " + messageStr + "," + recordMetadata.topic() + "," + recordMetadata.offset() + "," + recordMetadata.partition() + ")");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        producer.close();
    }

}

