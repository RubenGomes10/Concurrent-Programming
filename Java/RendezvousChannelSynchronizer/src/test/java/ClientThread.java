import java.util.concurrent.TimeoutException;

/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class ClientThread extends Thread{

    RendezvousChannel<Integer,Integer> channel;
    long timeout;
    int id;
    Integer request;

    public ClientThread(int id, RendezvousChannel<Integer,Integer> channel,
                        Integer request, long timeout) {
        this.id = id;
        this.channel = channel;
        this.request = request;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            Integer response = channel.request(request, timeout);
            System.out.println("Client " + id + " asked Integer of : \"" + request
                    + "\" and received: \"" + response + "\"");
        } catch (TimeoutException e) {
            System.out.println("Timeout exceeded in Client " + id);
        } catch (InterruptedException e) {
            System.out.println("Client " + id + " interrupted");
        }
    }
}

