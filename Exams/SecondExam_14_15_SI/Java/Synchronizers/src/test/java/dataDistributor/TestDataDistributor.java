package dataDistributor;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ruben Gomes on 30/06/2015.
 */
public class TestDataDistributor {


    private List<Integer> dataToSend;

    @Before
    public void setInstance(){

        dataToSend = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
    }




    @Test
    public void test_simple_put_and_take_with_max_elems_permit_5() throws InterruptedException {
        DataDistributor<Integer> dataDistributor = new DataDistributor<>(5);
        WorkerThread<Integer> workerThread = new WorkerThread<>(dataDistributor,dataToSend,1);
        ConsumerThread<Integer> consumerThread = new ConsumerThread<>(dataDistributor,1);

        workerThread.start();
        Thread.sleep(500);
        consumerThread.start();
        Thread.sleep(500);
        System.out.printf("Consumer Thread with id %d take  -> ", 1);
        System.out.println(consumerThread.getDataTaked());
    }

    @Test
    public void test_simple_put_and_two_takes_with_max_elems_permit_5() throws InterruptedException {
        DataDistributor<Integer> dataDistributor = new DataDistributor<>(5);
        WorkerThread<Integer> workerThread = new WorkerThread<>(dataDistributor,dataToSend,1);
        ConsumerThread<Integer> consumerThread1 = new ConsumerThread<>(dataDistributor,1);
        ConsumerThread<Integer> consumerThread2 = new ConsumerThread<>(dataDistributor,2);

        workerThread.start();
        Thread.sleep(500);
        consumerThread1.start();
        Thread.sleep(500);
        System.out.printf("Consumer Thread with id %d take  -> ", 1);
        System.out.println(consumerThread1.getDataTaked());
        consumerThread2.start();
        Thread.sleep(500);
        System.out.printf("Consumer Thread with id %d take  -> ", 2);
        System.out.println(consumerThread2.getDataTaked());
    }

    @Test   //should take first thread 6 elems and second thread 4
    public void test_simple_put_and_two_takes_with_max_elems_permit_6() throws InterruptedException {
        DataDistributor<Integer> dataDistributor = new DataDistributor<>(6);
        WorkerThread<Integer> workerThread = new WorkerThread<>(dataDistributor,dataToSend,1);
        ConsumerThread<Integer> consumerThread1 = new ConsumerThread<>(dataDistributor,1);
        ConsumerThread<Integer> consumerThread2 = new ConsumerThread<>(dataDistributor,2);

        workerThread.start();
        Thread.sleep(500);
        consumerThread1.start();
        Thread.sleep(500);
        System.out.printf("Consumer Thread with id %d take  -> ", 1);
        System.out.println(consumerThread1.getDataTaked());
        consumerThread2.start();
        Thread.sleep(500);
        System.out.printf("Consumer Thread with id %d take  -> ", 2);
        System.out.println(consumerThread2.getDataTaked());
    }

    @Test   //should take second thread(last in) 6 elems and first thread (first in) //LIFO
    public void test_simple_2_takes_with_max_elems_permit_6_and_put_in_last() throws InterruptedException {
        DataDistributor<Integer> dataDistributor = new DataDistributor<>(6);
        WorkerThread<Integer> workerThread = new WorkerThread<>(dataDistributor,dataToSend,1);
        ConsumerThread<Integer> consumerThread1 = new ConsumerThread<>(dataDistributor,1);
        ConsumerThread<Integer> consumerThread2 = new ConsumerThread<>(dataDistributor,2);


        consumerThread1.start();
        Thread.sleep(500);
        consumerThread2.start();
        Thread.sleep(500);
        workerThread.start();
        Thread.sleep(1000);
        System.out.printf("Consumer Thread with id %d take  -> ",1);
        System.out.println(consumerThread1.getDataTaked());
        System.out.printf("Consumer Thread with id %d take  -> ", 2);
        System.out.println(consumerThread2.getDataTaked());

    }



}
