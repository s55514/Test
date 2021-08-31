package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.utils.SerializationUtils;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitMQUtil {
    private static final String QUEUE_NAME = "Rabbit_QueueMQ_Deduction";
    private static final String DELAY_QUEUE_NAME = "test_delay_queue";

    /**
     * 获取连接
     * @return Connection
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setPort(5672);
        /** 空铁 */
        factory.setHost("47.110.187.133");
        factory.setUsername("admin");
        factory.setPassword("rPWD0qjF9sP6");
        /** 双鱼 */
//        factory.setHost("47.96.237.40");
//        factory.setUsername("admin");
//        factory.setPassword("vWbuNaLAJ3Or5");
        /** 线下测试 */
//        factory.setHost("116.62.124.216");
//        factory.setUsername("admin");
//        factory.setPassword("ectb2u23O8XL0jOB");
        //通过工厂获取连接
        Connection connection = factory.newConnection();
        return connection;
    }

    /**
     * @Description:  批量发扣款
     */
    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = RabbitMQUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        channel.queueDeclare("Rabbit_QueueMQ_Deduction", true, false, false, null);
        //消息内容
        List<String> ids = FileReadUtil.readLines("C:\\Users\\Administrator\\Desktop\\new 1.txt", "UTF-8");
        for (int i = 0;i<ids.size();i++){
            JSONObject json = new JSONObject();
            json.put("orderid", ids.get(i));
            json.put("type", "4");//1 是其他所有的（包括联程）   4 是接续扣款  空铁双鱼一样
            HashMap<String, String> message = new HashMap<String, String>();
            message.put("message number", json.toJSONString());
    //        String message = "";
            channel.basicPublish("","Rabbit_QueueMQ_Deduction",null, SerializationUtils.serialize(message));
        }
        channel.close();
        connection.close();
        System.out.println("结束");
    }

    //创建队列，发送消息
    public static void dlxMessage() throws Exception {
        //获取连接
        Connection connection = RabbitMQUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //创建DLX及死信队列
        channel.exchangeDeclare("dlx.exchange", "direct");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, "dlx.exchange","dlx.routingKey");
        //创建测试超时的Exchange及Queue
//        channel.exchangeDeclare("delay.exchange", "direct");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-exchange", "dlx.exchange");
        arguments.put("x-dead-letter-routing-key", "dlx.routingKey");
        channel.queueDeclare(DELAY_QUEUE_NAME, false, false, false, arguments);
//        channel.queueBind(DELAY_QUEUE_NAME, "delay.exchange", "delay.routingKey");
        //消息内容
        String message = "Hello World!";
        channel.basicPublish("",DELAY_QUEUE_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        System.out.println("发送消息："+message);
        //关闭连接和通道
        channel.close();
        connection.close();
        consume();
    }

    //消费者消费消息
    public static void consume() throws Exception {
        //获取连接和通道
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明通道
//        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME,false,consumer);

        while(true){
            //这个方法会阻塞住，直到获取到消息
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            String message = new String(delivery.getBody());
            Map<?, ?> message = (HashMap<?, ?>) SerializationUtils.deserialize(delivery.getBody());
            System.out.println("接收到消息："+message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }
    }
}
