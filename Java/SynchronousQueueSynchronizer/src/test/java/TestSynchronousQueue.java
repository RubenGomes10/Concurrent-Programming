import org.junit.Test;

/**
 * Created by Ruben Gomes on 23/05/2015.
 */
public class TestSynchronousQueue {

    @Test
    public void test_simple_put_and_take_message() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();
        ThreadClientPut<String> t1 = new ThreadClientPut<String>(1,queue);
        ThreadServer<String> t2 = new ThreadServer<String>(queue);

        t1.start();
        Thread.sleep(4000);
        t2.start();
        Thread.sleep(2000);
    }

    @Test
    public void test_multiple_put_and_take_message() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();
        ThreadClientPut<String>
                t1 = new ThreadClientPut<String>(1,queue),
                t2 = new ThreadClientPut<String>(2,queue),
                t3 = new ThreadClientPut<String>(3,queue);

        ThreadServer<String>
                t4 = new ThreadServer<String>(queue),
                t5 = new ThreadServer<String>(queue),
                t6 = new ThreadServer<String>(queue);

        t1.start();
        Thread.sleep(2000);
        t2.start();
        Thread.sleep(2000);
        t3.start();
        Thread.sleep(2000);
        t4.start();
        Thread.sleep(2000);
        t5.start();
        Thread.sleep(2000);
        t6.start();
        Thread.sleep(2000);

    }

    @Test
    public void test_multiple_takes_waiting_for_put_values() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();

        ThreadServer<String>
                t1 = new ThreadServer<String>(queue),
                t2 = new ThreadServer<String>(queue),
                t3 = new ThreadServer<String>(queue);

        ThreadClientPut<String>
                t4 = new ThreadClientPut<String>(1,queue),
                t5 = new ThreadClientPut<String>(2,queue),
                t6 = new ThreadClientPut<String>(3,queue);

        t1.start();
        Thread.sleep(2000);
        t2.start();
        Thread.sleep(2000);
        t3.start();
        Thread.sleep(2000);
        t4.start();
        Thread.sleep(2000);
        t5.start();
        Thread.sleep(2000);
        t6.start();
        Thread.sleep(2000);

    }

    @Test
    public void test_multiple_aleatory_calls_on_methods_put_and_take_messages() throws InterruptedException {

        ISynchronousQueue<String> queue = new SynchronousQueue<String>();

        ThreadServer<String>
                t1 = new ThreadServer<String>(queue),
                t2 = new ThreadServer<String>(queue),
                t3 = new ThreadServer<String>(queue);
        ThreadClientPut<String>
                t4 = new ThreadClientPut<String>(1,queue),
                t5 = new ThreadClientPut<String>(2,queue),
                t6 = new ThreadClientPut<String>(3,queue);

        t1.start();
        System.out.println("waiting for work!");
        Thread.sleep(1000);
        t4.start();
        Thread.sleep(500);
        System.out.println("waiting for work!");
        t2.start();
        Thread.sleep(1000);
        t3.start();
        Thread.sleep(1000);
        t5.start();
        Thread.sleep(500);
        t6.start();
        Thread.sleep(500);
    }


    @Test
    public void test_simple_empty_queue_waiting_for_put_values() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();

        ThreadServer<String>
                t1 = new ThreadServer<String>(queue);

        t1.start();
        System.out.println("waiting for work!");
        Thread.sleep(4000);

    }

    @Test
    public void test_simple_empty_queue_waiting_for_put_values_and_take_value() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();

        ThreadServer<String>
                t1 = new ThreadServer<String>(queue);
        ThreadClientPut<String>
                t2 = new ThreadClientPut<String>(1,queue);

        t1.start();
        System.out.println("waiting for work!");
        Thread.sleep(4000);
        t2.start();
        Thread.sleep(1000);
    }

    //test interrupt

    @Test
    public void test_interrupt_threads_from_put_messages() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();
        ThreadClientPut<String>
                t1 = new ThreadClientPut<String>(1,queue),
                t2 = new ThreadClientPut<String>(2,queue),
                t3 = new ThreadClientPut<String>(3,queue);

        t1.start();
        Thread.sleep(200);
        t1.interrupt();
        t2.start();
        Thread.sleep(200);
        t2.interrupt();
        t3.start();
        Thread.sleep(200);
        t3.interrupt();
        Thread.sleep(200);

    }


    @Test
    public void test_interrupt_threads_from_take_messages() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();
        ThreadServer<String>
                t1 = new ThreadServer<String>(queue),
                t2 = new ThreadServer<String>(queue),
                t3 = new ThreadServer<String>(queue);

        t1.start();
        Thread.sleep(200);
        t1.interrupt();
        t2.start();
        Thread.sleep(200);
        t2.interrupt();
        t3.start();
        Thread.sleep(200);
        t3.interrupt();
        Thread.sleep(200);

    }



    @Test
    public void test_interrupt_threads_from_put_but_one_put_message_successful() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();
        ThreadClientPut<String>
                t1 = new ThreadClientPut<String>(1,queue),
                t2 = new ThreadClientPut<String>(2,queue),
                t3 = new ThreadClientPut<String>(3,queue);
        ThreadServer<String>
                t4 = new ThreadServer<String>(queue);

        t1.start();
        Thread.sleep(10);
        t2.start();
        Thread.sleep(10);
        t3.start();
        Thread.sleep(200);
        t2.interrupt();
        Thread.sleep(200);
        t3.interrupt();
        t4.start();
    }

    @Test
    public void test_interrupt_threads_from_take_but_one_of_them_take_message_successful() throws InterruptedException {
        ISynchronousQueue<String> queue = new SynchronousQueue<String>();
        ThreadClientPut<String>
                t1 = new ThreadClientPut<String>(1,queue);
        ThreadServer<String>
                t2 = new ThreadServer<String>(queue),
                t3 = new ThreadServer<String>(queue),
                t4 = new ThreadServer<String>(queue);

        t2.start();
        Thread.sleep(10);
        t3.start();
        Thread.sleep(10);
        t4.start();
        Thread.sleep(10);
        t2.interrupt();
        Thread.sleep(200);
        t4.interrupt();
        Thread.sleep(200);
        t1.start();
        Thread.sleep(1000);


    }
}
