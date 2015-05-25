import org.junit.Test;

/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class TestRw_Semaphore {

    @Test
    public void test_simple_downRead_upRead_without_write() throws InterruptedException {

        Rw_Semaphore semaphore = new Rw_Semaphore();

        TestThreadDownRead t1 = new TestThreadDownRead(1,semaphore);
        TestThreadUpRead t2 = new TestThreadUpRead(2,semaphore);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(1000);
    }

    @Test
    public void test_simple_downRead_upRead_with_write() throws InterruptedException {
        Rw_Semaphore semaphore = new Rw_Semaphore();

        TestThreadDownRead t1 = new TestThreadDownRead(1,semaphore);
        TestThreadUpRead t2 = new TestThreadUpRead(2,semaphore);
        TestThreadDownWrite t3 = new TestThreadDownWrite(3,semaphore);
        TestThreadUpWrite t4 = new TestThreadUpWrite(4,semaphore);

        t3.start();
        Thread.sleep(500);
        t1.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
    }

    @Test
    public void test_multiple_try_acquire_read_locks_and_one_write_lock() throws InterruptedException {
        Rw_Semaphore semaphore = new Rw_Semaphore();
        TestThreadDownWrite t1 = new TestThreadDownWrite(1,semaphore);
        TestThreadUpWrite t2 = new TestThreadUpWrite(2,semaphore);
        TestThreadDownRead t3 = new TestThreadDownRead(3,semaphore);
        TestThreadDownRead t4 = new TestThreadDownRead(4,semaphore);
        TestThreadDownRead t5 = new TestThreadDownRead(5,semaphore);
        TestThreadUpRead t6 = new TestThreadUpRead(6,semaphore);


        t1.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);
        t5.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t6.start();
        Thread.sleep(500);



    }
}
