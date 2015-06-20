import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ruben Gomes on 20/06/2015.
 */
public class TestNonBlockingFutureHolder {

    private static final boolean GET_VALUE = true;
    private static final boolean SET_VALUE = false;


    public static class NonBlockingThread<T> extends Thread {

        NonBlockingFutureHolder<T> nonBlockingFutureHolder;
        int id;
        T value;
        long timeout;
        boolean isGetValue;


        public NonBlockingThread(NonBlockingFutureHolder<T> nonBlockingFutureHolder,int id, T value, long timeout, boolean isGetValue) {
            this.nonBlockingFutureHolder = nonBlockingFutureHolder;
            this.id = id;
            this.value = value;
            this.timeout = timeout;
            this.isGetValue = isGetValue;
        }

        @Override
        public void run() {
            if (isGetValue) {
                System.out.printf("Thread with id %d try get value \n", id);
                System.out.printf("thread with id %d getValue = %s \n",
                        id, (value = nonBlockingFutureHolder.getValue(timeout)) == null ? value+" by timeout" : value);

            }else
            try {
                System.out.printf("Thread with id %d try to put value %s \n", id,value);
                nonBlockingFutureHolder.setValue(value);
                System.out.printf("Thread with id %d put value %s successfully\n",id,value);
            }catch(IllegalStateException e){
                System.out.printf("Thread with id %d throw IllegalStateException - " + e.getMessage() + "\n", id);
            }
            }
    }

    @Test
    public void simple_test_thread_waiting_for_integer_value() throws InterruptedException {
        System.out.println("---------simple_test_thread_waiting_for_integer_value() BEGIN------------");
        NonBlockingFutureHolder<Integer> futureHolder = new NonBlockingFutureHolder<Integer>();
        NonBlockingThread<Integer>
                t1 = new NonBlockingThread<Integer>(futureHolder,1,null,5000,GET_VALUE),
                t2 = new NonBlockingThread<Integer>(futureHolder,2,10,5000,SET_VALUE);

        t1.start();
        Thread.sleep(200);
        t2.start();
        Thread.sleep(200);
        assertEquals(Integer.valueOf(10),futureHolder.getValue(5000));
        assertEquals(Thread.State.TERMINATED,t1.getState());
        assertEquals(Thread.State.TERMINATED,t2.getState());

        System.out.println("-------------------------------------------------------------------------");
    }


    @Test
    public void test_multiple_threads_waiting_for_integer_value() throws InterruptedException {
        System.out.println("------test_multiple_threads_waiting_for_integer_value() BEGIN-------------");
        NonBlockingFutureHolder<Integer> futureHolder = new NonBlockingFutureHolder<Integer>();
        NonBlockingThread<Integer>
                t1 = new NonBlockingThread<Integer>(futureHolder,1,null,5000,GET_VALUE),
                t2 = new NonBlockingThread<Integer>(futureHolder,2,null,5000,GET_VALUE),
                t3 = new NonBlockingThread<Integer>(futureHolder,3,null,5000,GET_VALUE),
                t4 = new NonBlockingThread<Integer>(futureHolder,4,20,5000,SET_VALUE);

        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);
        assertEquals(Thread.State.TERMINATED,t1.getState());
        assertEquals(Thread.State.TERMINATED,t2.getState());
        assertEquals(Thread.State.TERMINATED,t3.getState());
        assertEquals(Thread.State.TERMINATED,t4.getState());
        assertEquals(Integer.valueOf(20),futureHolder.getValue(5000));
        System.out.println("-------------------------------------------------------------------------");
    }

    @Test
    public void test_multiples_set_values_throw_IllegalStateException() throws InterruptedException{
        System.out.println("------test_multiples_set_values_throw_IllegalStateException() BEGIN------");
        NonBlockingFutureHolder<Integer> futureHolder = new NonBlockingFutureHolder<Integer>();
        NonBlockingThread<Integer>
                t1 = new NonBlockingThread<Integer>(futureHolder,1,10,5000,SET_VALUE),
                t2 = new NonBlockingThread<Integer>(futureHolder,2,20,5000,SET_VALUE);
        t1.start();
        Thread.sleep(200);
        assertEquals(Thread.State.TERMINATED, t1.getState());
        t2.start();
        Thread.sleep(200);
        assertEquals(Thread.State.TERMINATED,t2.getState());
        assertEquals(Integer.valueOf(10),futureHolder.getValue(5000));
        System.out.println("-------------------------------------------------------------------------");
    }

    @Test
    public void test_simple_thread_terminate_by_zero_timeout() throws InterruptedException{
        System.out.println("--------test_simple_thread_terminate_by_zero_timeout() BEGIN--------------");
        NonBlockingFutureHolder<Integer> futureHolder = new NonBlockingFutureHolder<Integer>();
        NonBlockingThread<Integer>
                t1 = new NonBlockingThread<Integer>(futureHolder,1,null,0,GET_VALUE);
        t1.start();
        Thread.sleep(10);
        assertEquals(Thread.State.TERMINATED, t1.getState());
        System.out.println("-------------------------------------------------------------------------");
    }

    @Test
    public void test_simple_thread_terminate_by_timeout() throws InterruptedException {
        System.out.println("----------test_simple_thread_terminate_by_timeout() BEGIN----------------");
        NonBlockingFutureHolder<Integer> futureHolder = new NonBlockingFutureHolder<Integer>();
        NonBlockingThread<Integer>
                t1 = new NonBlockingThread<Integer>(futureHolder,1,null,2000,GET_VALUE);
        t1.start();
        Thread.sleep(5000);
        assertEquals(Thread.State.TERMINATED, t1.getState());
        System.out.println("--------------------------------------------------------------------------");
    }



    @Test
    public void test_multiple_threads_but_one_terminate_by_timeout_and_the_other_get_the_value() throws InterruptedException {
        System.out.println("----test_multiple_threads_but_one_terminate_by_timeout_and_the_other_get_the_value() BEGIN---");
        NonBlockingFutureHolder<Integer> futureHolder = new NonBlockingFutureHolder<Integer>();
        NonBlockingThread<Integer>
                t1 = new NonBlockingThread<Integer>(futureHolder,1,null,2000,GET_VALUE),
                t2 = new NonBlockingThread<Integer>(futureHolder,2,null,4000,GET_VALUE),
                t3 = new NonBlockingThread<Integer>(futureHolder,3,10,1000,SET_VALUE);
        t1.start();
        t2.start();
        Thread.sleep(5000);
        assertEquals(Thread.State.TERMINATED, t1.getState());
        t3.start();
        Thread.sleep(500);
        assertEquals(Integer.valueOf(10),futureHolder.getValue(5000));
        assertEquals(Thread.State.TERMINATED, t1.getState());
        assertEquals(Thread.State.TERMINATED, t1.getState());
        assertEquals(Thread.State.TERMINATED, t1.getState());
        System.out.println("----------------------------------------------------------------------------------------------");

    }







}



