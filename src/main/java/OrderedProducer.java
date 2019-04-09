import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

public class OrderedProducer {
    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("example_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr("data05:9876");
        //Launch the instance.
        producer.start();
        String[] tags = new String[] {"createTag", "payTag", "sendTag"};
        for (int i = 0; i < 100; i++) { //产生
            int orderId = i % 10; //订单id从0到9
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicOrdered", tags[i % tags.length], "KEY" + i,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);

            System.out.printf("%s%n", sendResult);
        }
        //server shutdown
        producer.shutdown();
    }
}
