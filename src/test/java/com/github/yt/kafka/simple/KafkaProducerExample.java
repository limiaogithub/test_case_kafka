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

    /**
     * 需要修改！！！
     */
    private static final String servers = "kafka地址";

    private static final String topic = "testTopic";

    private static final boolean isAsync = false;

    @Test
    public void test() {
        Properties props = new Properties();

        /*
         * kafka集群地址，ip+端口，以逗号隔开。
         * */
        props.put("bootstrap.servers", servers);

        /*
            acks：配置可以设定发送消息后是否需要Broker端返回确认
            0：不需要进行确认，速度最快。存在丢失数据的风险。
            1：仅需要Leader进行确认，不需要ISR进行确认。是一种效率和安全折中的方式。
            all：需要ISR中所有的Replica给予接收确认，速度最慢，安全性最高，但是由于ISR可能会缩小到仅包含一个Replica，所以设置参数为all并不能一定避免数据丢失。
        */
        props.put("acks", "all");

        /*
         * 配置为大于0的值的话，客户端会在消息发送失败时重新发送。重试等同于在发送有异常时重新发送消息。如果不把max.in.flight.requests.per.connection设为1，
         * 重试可能会改变消息的顺序。两条消息同时发送到同一个分区，第一条失败了，并在第二条发送成功后重新发送，那么第二条消息可能在第一条消息前到达。
         * */
        props.put("retries", 0);

        /*
         * 消息延迟发送的毫秒数，目的是为了等待多个消息，在同一批次发送，减少网络请求。
         * */
        props.put("linger.ms", 1);

        /*
          Producer可以用来缓存数据的内存大小。该值实际为RecordAccumulator类中的BufferPool，即Producer所管理的最大内存。
          如果数据产生速度大于向broker发送的速度，producer会阻塞max.block.ms，超时则抛出异常
        */
        props.put("buffer.memory", 33554432);


        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        /*
         * Producer用于压缩数据的压缩类型，取值：none, gzip, snappy, or lz4
         * */
        props.put("compression.type", "snappy");

        /*
           Producer可以将发往同一个Partition的数据做成一个Produce Request发送请求，即Batch批处理，以减少请求次数，该值即为每次批处理的大小。
           另外每个Request请求包含多个Batch，每个Batch对应一个Partition，且一个Request发送的目的Broker均为这些partition的leader副本。
           若将该值设为0，则不会进行批处理
        */
        props.put("batch.size", 16384);

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

