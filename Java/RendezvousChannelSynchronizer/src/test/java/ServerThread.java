import java.util.concurrent.TimeoutException;

/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class ServerThread extends Thread{


    RendezvousChannel<Integer,Integer> channel;
    long timeout, waitForReply;
    int id;

    public ServerThread(int id, RendezvousChannel<Integer,Integer>channel,
                        long timeout, long waitForReply) {
        this.id = id;
        this.channel = channel;
        this.timeout = timeout;
        this.waitForReply = waitForReply;
    }

    @Override
    public void run() {
        try {
            Token<Integer> token = channel.accept(timeout);
            System.out.println("Server " + id + " started rendezvous");
            Thread.sleep(waitForReply);
            channel.reply(token, token.value() *2);

        } catch (TimeoutException e) {
            System.out.println("Timeout exceeded in Server " + id);
        } catch (InterruptedException e) {
            System.out.println("Server " + id + " interrupted");
        }
    }
}
