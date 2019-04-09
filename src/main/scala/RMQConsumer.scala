import java.util
import java.util.concurrent.atomic.AtomicInteger

import org.apache.log4j.Logger
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
import org.apache.rocketmq.client.consumer.listener.{ConsumeConcurrentlyContext, ConsumeConcurrentlyStatus, MessageListenerConcurrently}
import org.apache.rocketmq.common.consumer.ConsumeFromWhere
import org.apache.rocketmq.common.message.MessageExt

class RMQConsumer {
  var count = new AtomicInteger(0)

  def consumeMsgs(topicName: String = "topicTest", consumerGroup: String = "test_group",
                  consumeFromWhere: ConsumeFromWhere = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET): AtomicInteger = {
    val consumer = new DefaultMQPushConsumer(consumerGroup)
    consumer.setNamesrvAddr("localhost:9876")
    consumer.setConsumeFromWhere(consumeFromWhere)
    consumer.subscribe(topicName, "*")
    consumer.registerMessageListener(new MessageListenerConcurrently() {
      override def consumeMessage(msgs: util.List[MessageExt], context: ConsumeConcurrentlyContext): ConsumeConcurrentlyStatus = {
        count.addAndGet(msgs.size())
        ConsumeConcurrentlyStatus.CONSUME_SUCCESS
      }
    })
    consumer.start()
    Thread.sleep(6 * 1000)
    consumer.shutdown()
    count
  }
}
