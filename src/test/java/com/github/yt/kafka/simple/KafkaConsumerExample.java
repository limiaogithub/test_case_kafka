package com.github.yt.kafka.simple;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * kafka消费者.
 *
 * @author limiao
 */
public class KafkaConsumerExample {

    /**
     * 需要修改！！！
     */
    private static final String servers = "kafka地址";

    private static final String topic = "testTopic";

    private static final String group = "testGroup1";

    private static final boolean autoSubmit = true;

    @Test()
    public void test() {
        Properties props = new Properties();

        /**
         * 消费者初始连接kafka集群时的地址列表。
         * */
        props.put("bootstrap.servers", servers);

        /*
         * 每次请求，kafka返回的最小的数据量。如果数据量不够，这个请求会等待，直到数据量到达最小指标时，才会返回给消费者。如果设置大于1，会提高kafka的吞吐量，但是会有额外的等待期的代价。
         * */
        props.put("fetch.min.bytes", 30);

        /**
         * 标识这台消费者属于那个消费组。如果消费者通过订阅主题来实现组管理功能，或者使用基于kafka的偏移量管理策略，这个配置是必须的。
         * */
        props.put("group.id", group);

        /**
         * 当kafka的初始偏移量没了，或者当前的偏移量不存在的情况下，应该怎么办？下面有几种策略：
         * earliest（将偏移量自动重置为最初的值）、latest（自动将偏移量置为最新的值）、none（如果在消费者组中没有发现前一个偏移量，就向消费者抛出一个异常）、anything else（向消费者抛出异常）
         * */
        props.put("auto.offset.reset", "earliest");


        /**
         * 如果设为true，消费者的偏移量会定期在后台提交。
         * */
        props.put("enable.auto.commit", autoSubmit);

        /**
         * The frequency in milliseconds that the consumer offsets are auto-committed to Kafka if enable.auto.commit is set to true. 默认5000
         * */
        props.put("auto.commit.interval.ms", 1000);


        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        /**
         * 使用kafka集群管理工具时，消费者协调器之间的预计心跳时间。
         * 心跳的作用是确保消费者的session是活跃的，
         * 同时当新的机器加入集群或有机器挂掉的情况下触发再平衡操作。
         * 这个配置必须小于heartbeat.interval.ms，而且应该不大于这个值的1/3。为了控制正常的负载均衡的预期时间，这个值可以设置的更小。
         * */
        props.put("heartbeat.interval.ms", 3000);

        /**
         * 使用kafka集群管理工具时检测失败的超时时间。如果在session超时时间范围内，没有收到消费者的心跳，
         * broker会把这个消费者置为失效，并触发消费者负载均衡。
         * 因为只有在调用poll方法时才会发送心跳，更大的session超时时间允许消费者在poll循环周期内处理消息内容，
         * 尽管这会有花费更长时间检测失效的代价。如果想控制消费者处理消息的时间，还可以参考max.poll.records。
         * 注意这个值的大小应该在group.min.session.timeout.ms和group.max.session.timeout.ms范围内。
         * */
        props.put("session.timeout.ms", 30000);


        /**
         * kafka集群每个分区一次返回的最大数据量。
         * 一次请求的最大内存使用量应该等于#partitions * max.partition.fetch.bytes。
         * 这个值必须与kafka集群允许的最大消息数据量差不多大小，
         * 否则可能生产者发送了一个消息，大于消费者配置的值。这种情况下，消费者可能会在获取那条消息时堵住。
         * */
        props.put("max.partition.fetch.bytes", 1048576);


        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList(topic));
        System.out.println("Subscribed to topic " + topic);

        ArrayList<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());
            }
            if (autoSubmit) {
                continue;
            }

            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }

            if (buffer.size() >= 50) {
                System.out.println("▲submit offset!");
                //处理完之后进行提交
                //同步或异步提交
                //consumer.commitSync();
                consumer.commitAsync();
                //清除list, 继续接收
                buffer.clear();
            }
        }
    }

}