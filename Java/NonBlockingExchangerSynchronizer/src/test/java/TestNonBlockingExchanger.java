import org.junit.Test;

/**
 * Created by Ruben Gomes on 21/06/2015.
 */
public class TestNonBlockingExchanger {

    @Test
    public void test_simple_exchange_Message() throws InterruptedException {
        System.out.println("-------test_simple_exchange_Message - BEGIN---------");
        NonBlockingExchanger<String> exchanger = new NonBlockingExchanger<String>();
        NonBlockingExchangerThread<String>
                t1 = new NonBlockingExchangerThread<String>(exchanger,1,2000),
                t2 = new NonBlockingExchangerThread<String>(exchanger,2,2000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        System.out.println("-----------------------------------------");
    }

    @Test
    public void test_multiple_exchange_message() throws InterruptedException {
        System.out.println("-------test_multiple_exchange_Message - BEGIN---------");
        NonBlockingExchanger<String> exchanger = new NonBlockingExchanger<String>();
        NonBlockingExchangerThread<String>
                t1 = new NonBlockingExchangerThread<String>(exchanger,1,4000),
                t2 = new NonBlockingExchangerThread<String>(exchanger,2,4000),
                t3 = new NonBlockingExchangerThread<String>(exchanger,3,4000),
                t4 = new NonBlockingExchangerThread<String>(exchanger,4,4000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);
        System.out.println("-----------------------------------------");
    }

    @Test
    public void test_zero_timeout_exchange_message() throws InterruptedException {
        System.out.println("-------test_zero_timeout_exchange_message - BEGIN---------");
        NonBlockingExchanger<String> exchanger = new NonBlockingExchanger<String>();
        NonBlockingExchangerThread<String>
                t1 = new NonBlockingExchangerThread<String>(exchanger,1,0),
                t2 = new NonBlockingExchangerThread<String>(exchanger,2,2000),
                t3 = new NonBlockingExchangerThread<String>(exchanger,3,2000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        System.out.println("-----------------------------------------");
    }

    @Test
    public void test_timeout_exchange_message() throws InterruptedException {
        System.out.println("-------test_timeout_exchange_message - BEGIN---------");
        NonBlockingExchanger<String> exchanger = new NonBlockingExchanger<String>();
        NonBlockingExchangerThread<String>
                t1 = new NonBlockingExchangerThread<String>(exchanger,1,1000),
                t2 = new NonBlockingExchangerThread<String>(exchanger,2,2000),
                t3 = new NonBlockingExchangerThread<String>(exchanger,3,2000);

        t1.start();
        Thread.sleep(1500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        System.out.println("-----------------------------------------");
    }

    @Test
    public void test_interrupted_exchange_message() throws InterruptedException {
        System.out.println("-------test_interrupted_exchange_message - BEGIN---------");
        NonBlockingExchanger<String> exchanger = new NonBlockingExchanger<String>();
        NonBlockingExchangerThread<String>
                t1 = new NonBlockingExchangerThread<String>(exchanger,1,1000),
                t2 = new NonBlockingExchangerThread<String>(exchanger,2,2000),
                t3 = new NonBlockingExchangerThread<String>(exchanger,3,2000);

        t1.start();
        Thread.sleep(500);
        t1.interrupt();
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        System.out.println("-----------------------------------------");
    }

    @Test
    public void test_interrupted_rearm_exchange_message() throws InterruptedException {
        System.out.println("-------test_interrupted_rearm_exchange_message - BEGIN---------");
        NonBlockingExchanger<String> exchanger = new NonBlockingExchanger<String>();
        NonBlockingExchangerThread<String>
                t1 = new NonBlockingExchangerThread<String>(exchanger,1,4000),
                t2 = new NonBlockingExchangerThread<String>(exchanger,2,4000),
                t3 = new NonBlockingExchangerThread<String>(exchanger,3,2000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(5);
        t1.interrupt();
        Thread.sleep(10);
        t3.start();
        Thread.sleep(2500);
        System.out.println("-----------------------------------------");
    }



}
