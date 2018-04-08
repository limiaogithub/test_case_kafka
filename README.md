# test_case_kafka
<h2>本文是讲解在windows下运行kafka，实现生产消费</h2>

1.解压kafka_2.12-1.0.1</br>
2.修改kafka_2.12-1.0.1\config\zookeeper.properties文件</br>
dataDir=D:\zookeeperlog</br>
3.修改kafka_2.12-1.0.1\config\server.properties文件</br>
log.dirs=D:\kafkalog</br>

4.命令行 cd kafka_2.12-1.0.1\bin\windows</br>
zookeeper-server-start ../../config/zookeeper.properties</br>

5.再起一个命令行 cd kafka_2.12-1.0.1\bin\windows</br>
kafka-server-start ../../config/server.properties</br>


6.创建topic 再起一个命令行 cd kafka_2.12-1.0.1\bin\windows</br>
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic yt</br>


<h2>测试：</h2>
kafka-console-producer.bat --broker-list localhost:9092 --topic yt</br>
kafka-console-consumer.bat -zookeeper localhost:2181 --from-beginning --topic yt</br>


<h2>代码测试：</h2>
运行KafkaProducerExample.java</br>
运行KafkaConsumerExample.java </br>
