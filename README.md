# test_case_kafka
<h2>�����ǽ�����windows������kafka��ʵ����������</h2>

1.��ѹkafka_2.12-1.0.1</br>
2.�޸�kafka_2.12-1.0.1\config\zookeeper.properties�ļ�</br>
dataDir=D:\zookeeperlog</br>
3.�޸�kafka_2.12-1.0.1\config\server.properties�ļ�</br>
log.dirs=D:\kafkalog</br>

4.������ cd kafka_2.12-1.0.1\bin\windows</br>
zookeeper-server-start ../../config/zookeeper.properties</br>

5.����һ�������� cd kafka_2.12-1.0.1\bin\windows</br>
kafka-server-start ../../config/server.properties</br>


6.����topic ����һ�������� cd kafka_2.12-1.0.1\bin\windows</br>
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic yt</br>

7.����KafkaProducerExample.java

8.����KafkaConsumerExample.java 