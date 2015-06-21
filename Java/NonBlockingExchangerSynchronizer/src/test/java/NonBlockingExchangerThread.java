import java.util.concurrent.TimeoutException;

/**
 * Created by Ruben Gomes on 21/06/2015.
 */
public class NonBlockingExchangerThread<T> extends Thread {

    private NonBlockingExchanger<T> nonBlockingExchanger;
    private long timeout;
    private int id;

    public NonBlockingExchangerThread(NonBlockingExchanger<T> nonBlockingExchanger,int id,long timeout) {
        this.id = id;
        this.timeout = timeout;
        this.nonBlockingExchanger = nonBlockingExchanger;
    }


    public void run(){
        T msg = (T) ("Message " + id);
        System.out.println("Thread " + id + " is sending " + msg);
        try {
            msg = nonBlockingExchanger.exchange(msg, timeout);
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted Exception occurred");
            return;
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Thread "+ id + " received " + msg);
    }
}
