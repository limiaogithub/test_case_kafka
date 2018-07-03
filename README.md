# test_case_kafka
<h2>这里介绍了如何在windows下安装kakfa，并且提供了单元测试程序进行生产消费。</h2>

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


<h2>kafka常用命令：</h2>
#创建topic </br>
./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic topicA </br>
 </br>
#查看最大偏移量 </br>
./kafka-run-class.sh kafka.tools.GetOffsetShell --topic topicA  --broker-list node:port  </br>
 </br>
#查看所有topic列表 </br>
./kafka-topics.sh --zookeeper node:port --list </br>
 </br>
#查看指定topic信息 </br>
./kafka-topics.sh --zookeeper node:port --describe --topic topicA </br>
 </br>
#控制台消费数据 </br>
./kafka-console-consumer.sh  --zookeeper node:port  --topic topicA --from-beginning --group groupA </br>
