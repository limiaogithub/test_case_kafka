package com.github.yt.kafka.thread.rebalance;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * kafka消费者.
 *
 * @author limiao
 */
public class KafkaConsumerThread implements Runnable {


    private boolean fault;

    private Properties props;

    private String topic;

    public KafkaConsumerThread(String topic, Properties props, boolean fault) {
        this.topic = topic;
        this.props = props;
        this.fault = fault;
    }

    @Override
    public void run() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic), new SaveOffsetOnRebalance(consumer));
        ArrayList<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Thread:[" + Thread.currentThread().getName() + "],offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());
            }
            if (fault) {
                //方法一：异常退出
                //int i = 1 / 0;
                //方法二：取消订阅
                consumer.unsubscribe();
            }
        }
    }
}