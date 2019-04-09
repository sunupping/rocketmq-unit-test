import org.apache.rocketmq.client.producer.DefaultMQProducer
import org.apache.rocketmq.common.message.Message
import org.apache.rocketmq.remoting.common.RemotingHelper
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class SimpleRocketMqTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    //清除本地生成的目录文件
    CleanRmqDir.clean()

    Thread.sleep(3*1000)
    //开启RocketMq服务
//    EmebeddedRocketMqServer.start()  //需要依赖logback xml文件
    NamesrvLauncher.startUp()
    BrokerLauncher.startUp()

    //生产者生产数据
    val producer = new RMQProducer
    producer.produceMsgs(content = "this is a test", cycles = 5)
    val producer1 = new RMQProducer
    producer1.produceMsgs(topicName = "others", cycles = 3)
    super.beforeAll()
  }

  "Producer" should "send message successfully" in {
    val producer = new DefaultMQProducer("rocketmq_group_producer")
    producer.setNamesrvAddr("localhost:9876")
    producer.start()
    val message = new Message("topicTest", "tagA", "01", "this is a message from producer".getBytes(RemotingHelper.DEFAULT_CHARSET))
    val sendResult = producer.send(message)
    sendResult.getSendStatus.toString shouldBe "SEND_OK"
    producer.shutdown()
  }

  "Consumer" should "consume two topics messages successfully" in {
    val consumer = new RMQConsumer
    val count = consumer.consumeMsgs()
    count.get() shouldBe 6
    val consumer1 = new RMQConsumer
    val count1 = consumer1.consumeMsgs(topicName = "others",consumerGroup = "test_group1")
    count1.get() shouldBe 3
  }

}

