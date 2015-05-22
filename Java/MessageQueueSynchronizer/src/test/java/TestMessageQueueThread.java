import java.util.function.IntPredicate;

/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public class TestMessageQueueThread<T> extends Thread {



    int id;
    long timeout;
    boolean isSend;
    MessageQueue<T> sync;
    IntPredicate selector;


    public TestMessageQueueThread(int id, long timeout, boolean isSend,MessageQueue<T> sync,IntPredicate selector){

        this.id = id;
        this.timeout = timeout;
        this.isSend = isSend;
        this.sync = sync;
        this.selector = selector;
    }

    @Override
    public void run(){
        T msg = (T) ("Message " + id);
        if(isSend) {
            sync.send(msg, id);
            System.out.println("Thread " + id + " send " + msg);
        }else{
            try {
                msg = sync.receive(selector,timeout);
            }catch (InterruptedException e) {
                    System.out.println("Interrupted Exception occurred");
                    return;
            }
            if (msg == null)
                System.out.println("Timeout occurred or queue is empty");
            else
                System.out.println("Thread "+ id + " received filtered message " + msg);
        }
    }

}

