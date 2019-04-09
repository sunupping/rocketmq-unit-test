import org.apache.log4j.Logger
import org.apache.rocketmq.client.producer.DefaultMQProducer
import org.apache.rocketmq.common.message.Message
import org.apache.rocketmq.remoting.common.RemotingHelper

class RMQProducer {

  def produceMsgs(topicName: String = "topicTest", content: String = "hello rocketmq", producerGroup: String = "test_group", cycles: Int = 1): Unit = {
    val producer = new DefaultMQProducer(producerGroup)
    producer.setNamesrvAddr("localhost:9876")
    producer.start()

    for (i <- 0 until cycles) {
      val msg = new Message(topicName, content.getBytes(RemotingHelper.DEFAULT_CHARSET))
      val sendResult = producer.send(msg)
    }
    producer.shutdown()
  }
}
