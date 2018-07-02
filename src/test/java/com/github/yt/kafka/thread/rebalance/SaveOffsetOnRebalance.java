package com.github.yt.kafka.thread.rebalance;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

public class SaveOffsetOnRebalance implements ConsumerRebalanceListener {

    private Consumer<String, String> consumer;

    //初始化方法，传入consumer对象，否则无法调用外部的consumer对象，必须传入
    public SaveOffsetOnRebalance(Consumer<String, String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
        System.out.println("Thread:[" + Thread.currentThread().getName() + "]------------------------- in ralance:onPartitionsRevoked");
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
    }
}
