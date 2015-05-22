import org.junit.Test;

/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public class TestMessageQueue {

    private static final boolean SEND = true;
    private static final boolean RECEIVE = false;
    @Test
    public void test_multiple_send_and_simple_receive_filtered_message_queue() throws InterruptedException {
        MessageQueue<String> message = new MessageQueue<>();
        TestMessageQueueThread<String>
                t1 = new TestMessageQueueThread<>(1,2000,SEND,message,null),
                t2 = new TestMessageQueueThread<>(2,2000,SEND,message,null),
                t3 = new TestMessageQueueThread<>(3,3000,SEND,message,null),
                t4 = new TestMessageQueueThread<>(4,2000,RECEIVE,message, p -> p == 2 );

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(1000);
    }

    @Test
    public void test_multiple_send_and_multiple_receive_filtered_message_queue() throws InterruptedException {
        MessageQueue<String> message = new MessageQueue<>();
        TestMessageQueueThread<String>
                t1 = new TestMessageQueueThread<>(1,2000,SEND,message,null),
                t2 = new TestMessageQueueThread<>(2,2000,SEND,message,null),
                t3 = new TestMessageQueueThread<>(3,3000,SEND,message,null),
                t4 = new TestMessageQueueThread<>(4,2000,RECEIVE,message, p -> p == 2),
                t5 = new TestMessageQueueThread<>(5,2000,RECEIVE,message, p -> p == 1),
                t6 = new TestMessageQueueThread<>(6,2000,RECEIVE,message, p -> p == 3);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(1000);
        t5.start();
        Thread.sleep(1000);
        t6.start();
        Thread.sleep(1000);
    }

    @Test
    public void test_simple_empty_queue() throws InterruptedException {
        MessageQueue<String> message = new MessageQueue<>();
        TestMessageQueueThread<String>
                t1 = new TestMessageQueueThread<>(1,2000,RECEIVE,message, p -> p == 1);

        t1.start();
        Thread.sleep(500);
    }

    @Test
    public void test_simple_timeout_receive_message_because_no_match_with_selector() throws InterruptedException {
        MessageQueue<String> message = new MessageQueue<>();
        TestMessageQueueThread<String>
                t1 = new TestMessageQueueThread<>(1,2000,SEND,message,null),
                t2 = new TestMessageQueueThread<>(2,2000,RECEIVE,message, p -> p == 2);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(2500);
    }

    @Test
    public void test_simple_receive_message_but_wait_until_exists_match_with_selector() throws InterruptedException {
        MessageQueue<String> message = new MessageQueue<>();
        TestMessageQueueThread<String>
                t1 = new TestMessageQueueThread<>(1,2000,SEND,message,null),
                t2 = new TestMessageQueueThread<>(2,2000,RECEIVE,message, p -> p == 3),
                t3 = new TestMessageQueueThread<>(3,2000,SEND,message, null);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(1500);
        t3.start();
        Thread.sleep(200);
    }

    @Test
    public void test_simple_interrupt_receive_message_but_no_match_selector() throws InterruptedException {
        MessageQueue<String> message = new MessageQueue<>();
        TestMessageQueueThread<String>
                t1 = new TestMessageQueueThread<>(1,2000,SEND,message,null),
                t2 = new TestMessageQueueThread<>(2,2000,RECEIVE,message, p -> p == 2 );

        t1.start();
        Thread.sleep(500);
        t2.start();
        t2.interrupt();
        Thread.sleep(500);
    }

    @Test
    public void test_simple_interrupt_receive_message_but_match_selector() throws InterruptedException {
        MessageQueue<String> message = new MessageQueue<>();
        TestMessageQueueThread<String>
                t1 = new TestMessageQueueThread<>(1,2000,SEND,message,null),
                t2 = new TestMessageQueueThread<>(2,2000,RECEIVE,message, p -> p == 2 ),
                t3 = new TestMessageQueueThread<>(2,2000,SEND,message, null);

        t1.start();
        Thread.sleep(500);
        t2.start();
        t3.start();
        Thread.sleep(5);
        t2.interrupt();
        Thread.sleep(500);
    }




}
