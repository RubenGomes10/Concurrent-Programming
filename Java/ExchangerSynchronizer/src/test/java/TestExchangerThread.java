/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public class TestExchangerThread<T> extends Thread {

    private Exchanger<T> sync;
    private long timeout;
    private int id;

    public TestExchangerThread(int id,long timeout, Exchanger<T> sync) {
        this.id = id;
        this.timeout = timeout;
        this.sync = sync;
    }


    public void run(){
        T msg = (T) ("Message " + id);
        System.out.println("Thread " + id + " is sending " + msg);
        try {
            msg = sync.exchange(msg, timeout);
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted Exception occurred");
            return;
        }
        if (msg == null)
            System.out.println("Timeout occurred");
        else
            System.out.println("Thread "+ id + " received " + msg);
    }
}

