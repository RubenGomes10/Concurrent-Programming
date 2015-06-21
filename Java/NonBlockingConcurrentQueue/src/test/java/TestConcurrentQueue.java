import concurrentQueue.ConcurrentQueue;
import concurrentQueue.ConcurrentQueue.*;
import org.junit.Test;

/**
 * Created by Ruben Gomes on 21/06/2015.
 */


public class TestConcurrentQueue {

    private static class PutThread extends Thread{
        ConcurrentQueue<String> queue;
        int id;
        String value;
        public PutThread(ConcurrentQueue<String> queue,int id, String value){
            this.value = value;
            this.id = id;
            this.queue = queue;
        }

        @Override
        public void run(){
            if(queue.isEmpty())
                System.out.println("queue is empty in put method!");
            queue.put(value);
            System.out.printf("thread with id %d put value = %s in queue\n", id, value);

        }
    }
    private static class TakeThread extends Thread{
        ConcurrentQueue<String> queue;
        int id;
        public TakeThread(ConcurrentQueue<String> queue, int id){
            this.queue = queue;
            this.id = id;
        }
        @Override
        public void run(){
            String val = queue.tryTake();
            if(val == null)
                System.out.println("queue is empty in take method!");
            else System.out.printf("Thread wih id %d took value = %s from queue \n", id, val);
        }
    }


    @Test
    public void test_simple_put_and_get() throws InterruptedException{
        System.out.println("-------test_simple_put_and_get - BEGIN---------");
        ConcurrentQueue<String> queue = new ConcurrentQueue<String>();
        Thread t1 = new PutThread(queue,1, "msg1");
        Thread t2 = new PutThread(queue,2, "msg2");
        Thread t3 = new PutThread(queue,3, "msg3");
        Thread t4 = new TakeThread(queue,1);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);


        Node<String> node = queue.dummy.next;
        while(node != null){
            System.out.println(node.elem);
            node = node.next;
        }
        System.out.println("-----------------------------------------");
    }

    @Test
    public void test_empty_queue_get() throws InterruptedException{
        System.out.println("-------test_empty queue get - BEGIN--------");
        ConcurrentQueue<String> queue = new ConcurrentQueue<String>();
        Thread t1 = new TakeThread(queue,1);

        t1.start();
        Thread.sleep(500);

        Node<String> node = queue.dummy.next;
        while(node != null){
            System.out.println(node.elem);
            node = node.next;
        }
        System.out.println("-------------------------------------------");
    }

    @Test
    public void test_multiple_queue_get_and_put() throws InterruptedException {
        System.out.println("-------test_multiple_queue_get_and_put - BEGIN--------");
        ConcurrentQueue<String> queue = new ConcurrentQueue<String>();
        Thread  t1 = new PutThread(queue,1,"msg1"),
                t2 = new PutThread(queue,2,"msg2"),
                t3 = new PutThread(queue,3,"msg3"),
                t4 = new PutThread(queue,4,"msg4");
        Thread  t5 = new TakeThread(queue,1),
                t6 = new TakeThread(queue,2);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t5.start();
        Thread.sleep(500);
        t6.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);


        Node<String> node = queue.dummy.next;
        while(node != null){
            System.out.println(node.elem);
            node = node.next;
        }
        System.out.println("----------------------------------------------------");


    }


}
