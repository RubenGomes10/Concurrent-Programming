import org.junit.Test;

/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public class TestExchanger {

    @Test
    public void test_simple_exchange_Message() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<String>();
        TestExchangerThread<String>
                t1 = new TestExchangerThread<String>(1,2000,exchanger),
                t2 = new TestExchangerThread<String>(2,2000,exchanger);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
    }

    @Test
    public void test_multiple_exchange_Message() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<String>();
        TestExchangerThread<String>
                t1 = new TestExchangerThread<String>(1,4000,exchanger),
                t2 = new TestExchangerThread<String>(2,4000,exchanger),
                t3 = new TestExchangerThread<String>(3,4000,exchanger),
                t4 = new TestExchangerThread<String>(4,4000,exchanger);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);
    }

    @Test
    public void test_zero_timeout_exchange_message() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<String>();
        TestExchangerThread<String>
                t1 = new TestExchangerThread<String>(1,0,exchanger),
                t2 = new TestExchangerThread<String>(2,2000,exchanger),
                t3 = new TestExchangerThread<String>(3,2000,exchanger);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
    }

    @Test
    public void test_timeout_exchange_message() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<String>();
        TestExchangerThread<String>
                t1 = new TestExchangerThread<String>(1,1000,exchanger),
                t2 = new TestExchangerThread<String>(2,2000,exchanger),
                t3 = new TestExchangerThread<String>(3,2000,exchanger);

        t1.start();
        Thread.sleep(1500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
    }

    @Test
    public void test_interrupted_exchange_message() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<String>();
        TestExchangerThread<String>
                t1 = new TestExchangerThread<String>(1,1000,exchanger),
                t2 = new TestExchangerThread<String>(2,2000,exchanger),
                t3 = new TestExchangerThread<String>(3,2000,exchanger);

        t1.start();
        Thread.sleep(500);
        t1.interrupt();
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
    }

    @Test
    public void test_interrupted_rearm_exchange_message() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<String>();
        TestExchangerThread<String>
                t1 = new TestExchangerThread<String>(1,4000,exchanger),
                t2 = new TestExchangerThread<String>(2,4000,exchanger),
                t3 = new TestExchangerThread<String>(3,2000,exchanger);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(5);
        t1.interrupt();
        Thread.sleep(10);
        t3.start();
        Thread.sleep(2500);
    }





}
