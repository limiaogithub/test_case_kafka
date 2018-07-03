运行com.github.yt.kafka.simple.KafkaProducerExample.java生产N条数据
    1.修改servers地址
    2.修改77行
    for (int messageNo = 1; messageNo < 1000; messageNo++) {

    }
    把循环次数加大，多造点数据
    3.运行

运行TestRebalance.java
    1.修改servers地址
    2.每次运行前修改group，
    因为设置了 props.put("auto.offset.reset", "earliest")，每次都从头读数据。
    3.运行，查看是否出现Rebalance