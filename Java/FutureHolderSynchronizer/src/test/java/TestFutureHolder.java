import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ruben Gomes on 20/05/2015.
 */
public class TestFutureHolder {

    private static final boolean GET_VALUE = true;
    private static final boolean SET_VALUE = false;

    @Test
    public void simple_test_thread_waiting_for_integer_value() throws InterruptedException {
        FutureHolder<Integer> futureHolder = new FutureHolder<Integer>();
        TestFutureHolderThread<Integer> t1 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,5000,null),
                                        t2 = new TestFutureHolderThread<Integer>(futureHolder,SET_VALUE,5000,10);

        t1.start();
        Thread.sleep(200);
        assertEquals(Thread.State.TIMED_WAITING,t1.getState());
        t2.start();
        Thread.sleep(200);
        assertEquals(Thread.State.TERMINATED,t2.getState());
        assertEquals(Integer.valueOf(10),futureHolder.getValue(5000));
    }

    @Test
    public void test_multiple_threads_waiting_for_integer_value() throws InterruptedException {
        FutureHolder<Integer> futureHolder = new FutureHolder<Integer>();
        TestFutureHolderThread<Integer>
                t1 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,5000,null),
                t2 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,5000,null),
                t3 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,5000,null),
                t4 = new TestFutureHolderThread<Integer>(futureHolder,SET_VALUE,5000,10);

        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(500);
        assertEquals(Thread.State.TIMED_WAITING,t1.getState());
        assertEquals(Thread.State.TIMED_WAITING,t2.getState());
        assertEquals(Thread.State.TIMED_WAITING,t3.getState());
        t4.start();
        Thread.sleep(500);
        assertEquals(Thread.State.TERMINATED,t1.getState());
        assertEquals(Thread.State.TERMINATED,t2.getState());
        assertEquals(Thread.State.TERMINATED,t3.getState());
        assertEquals(Thread.State.TERMINATED,t4.getState());
        assertEquals(Integer.valueOf(10),futureHolder.getValue(5000));
    }

    @Test
    public void test_multiples_set_values_throw_IllegalStateException() throws InterruptedException{
        FutureHolder<Integer> futureHolder = new FutureHolder<Integer>();
        TestFutureHolderThread<Integer>
                t1 = new TestFutureHolderThread<Integer>(futureHolder,SET_VALUE,5000,10),
                t2 = new TestFutureHolderThread<Integer>(futureHolder,SET_VALUE,5000,20);
        t1.start();
        Thread.sleep(200);
        assertEquals(Thread.State.TERMINATED, t1.getState());
        t2.start();
        Thread.sleep(200);
        assertEquals(Thread.State.TERMINATED,t2.getState());
        assertEquals(Integer.valueOf(10),futureHolder.getValue(5000));
    }

    @Test
    public void test_simple_thread_terminate_by_zero_timeout() throws InterruptedException{
        FutureHolder<Integer> futureHolder = new FutureHolder<Integer>();
        TestFutureHolderThread<Integer>
                t1 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,0,null);
        t1.start();
        Thread.sleep(10);
        assertEquals(Thread.State.TERMINATED, t1.getState());

    }

    @Test
    public void test_simple_thread_terminate_by_timeout() throws InterruptedException {
        FutureHolder<Integer> futureHolder = new FutureHolder<Integer>();
        TestFutureHolderThread<Integer>
                t1 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,2000,null);
        t1.start();
        Thread.sleep(10);
        assertEquals(Thread.State.TIMED_WAITING, t1.getState());
        Thread.sleep(3000);
        assertEquals(Thread.State.TERMINATED, t1.getState());
    }


    @Test
    public void test_multiple_threads_but_one_terminate_by_timeout_and_the_other_get_the_value() throws InterruptedException {
        FutureHolder<Integer> futureHolder = new FutureHolder<Integer>();
        TestFutureHolderThread<Integer>
                t1 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,2000,null),
                t2 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,4000,null),
                t3 = new TestFutureHolderThread<Integer>(futureHolder,SET_VALUE,1000,10);
        t1.start();
        t2.start();
        Thread.sleep(10);
        assertEquals(Thread.State.TIMED_WAITING, t1.getState());
        assertEquals(Thread.State.TIMED_WAITING, t2.getState());
        Thread.sleep(3000);
        assertEquals(Thread.State.TERMINATED, t1.getState());
        t3.start();
        Thread.sleep(10);
        assertEquals(Thread.State.TERMINATED, t2.getState());
        assertEquals(Thread.State.TERMINATED, t3.getState());
        assertEquals(Integer.valueOf(10),futureHolder.getValue(5000));
    }

    @Test
    public void test_simple_thread_interrupt_and_the_value_is_not_available() throws InterruptedException {
        FutureHolder<Integer> futureHolder = new FutureHolder<Integer>();
        TestFutureHolderThread<Integer>
                t1 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,3000,null);

        t1.start();
        Thread.sleep(100);
        assertEquals(Thread.State.TIMED_WAITING, t1.getState());
        t1.interrupt();
        Thread.sleep(100);
        assertEquals(Thread.State.TERMINATED, t1.getState());
    }

    @Test
    public void test_simple_thread_interrupt_but_value_already_available() throws InterruptedException {
        FutureHolder<Integer> futureHolder = new FutureHolder<Integer>();
        TestFutureHolderThread<Integer>
                t1 = new TestFutureHolderThread<Integer>(futureHolder,GET_VALUE,3000,null),
                t2 = new TestFutureHolderThread<Integer>(futureHolder,SET_VALUE,1000,10);

        t1.start();
        Thread.sleep(100);
        assertEquals(Thread.State.TIMED_WAITING, t1.getState());
        t2.start();
        Thread.sleep(5);
        t1.interrupt();
        Thread.sleep(10);
        assertEquals(Thread.State.TERMINATED, t2.getState());
        assertEquals(Thread.State.TERMINATED, t1.getState());
    }


}
