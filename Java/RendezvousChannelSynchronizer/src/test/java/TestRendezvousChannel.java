import org.junit.Test;

/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class TestRendezvousChannel {


    //  //Responses -> Client gives an integer, server gives back the double of it.
    @Test
    public void test_server_starts_first() throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ServerThread(1, rendChannel, 5000, 500);
        Thread t2 = new ClientThread(1, rendChannel,10,
                5000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(1000);

    }

    @Test
    public void test_client_starts_first() throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ClientThread(1, rendChannel,20,
                5000);
        Thread t2 = new ServerThread(1, rendChannel, 5000, 500);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
    }
    @Test
    public void test_long_waiting_in_rendezvous()throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t2= new ServerThread(1, rendChannel, 5000, 5000);
        Thread t1 = new ClientThread(1, rendChannel,5,
                5000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(5000);
    }

    @Test
    public void test_client_zero_time() throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ClientThread(1, rendChannel,4, 0);
        Thread t2 = new ServerThread(1, rendChannel, 5000, 500);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);

    }

    @Test
    public void test_client_zero_time_after_server_starts()throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ServerThread(1, rendChannel, 5000, 500);
        Thread t2 = new ClientThread(1, rendChannel,10, 0);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);

    }

    @Test
    public void test_server_zero_time() throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ServerThread(1, rendChannel, 0, 500);
        Thread t2 = new ClientThread(1, rendChannel,10, 5000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
    }

    @Test
    public void test_server_zero_time_after_client_starts()throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ServerThread(1, rendChannel, 5000, 500);
        Thread t2 = new ClientThread(1, rendChannel,10, 5000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(1000);

    }

    @Test
    public void test_multiple_client_one_server()throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ServerThread(1, rendChannel, 5000, 500);
        Thread t2 = new ClientThread(1, rendChannel,10, 5000);
        Thread t3 = new ClientThread(2, rendChannel,20, 5000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(1000);

    }

    @Test
    public void test_multiple_server_one_client()throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ServerThread(1, rendChannel, 5000, 500);
        Thread t2 = new ServerThread(2, rendChannel, 5000, 500);
        Thread t3 = new ClientThread(1, rendChannel, 10, 5000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(1000);

    }

    @Test
    public void test_multiple_clients_first_and_servers()throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ClientThread(1, rendChannel,10, 5000);
        Thread t2 = new ClientThread(2, rendChannel,20, 5000);
        Thread t3 = new ServerThread(1, rendChannel, 5000, 500);
        Thread t4 = new ServerThread(2, rendChannel, 5000, 500);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);

    }


    @Test
    public void test_multiple_servers_first_and_clients()throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ServerThread(1, rendChannel, 5000, 500);
        Thread t2 = new ServerThread(2, rendChannel, 5000, 500);
        Thread t3 = new ClientThread(1, rendChannel,10, 5000);
        Thread t4 = new ClientThread(2, rendChannel,20, 5000);

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(500);
        t4.start();
        Thread.sleep(500);

    }

    @Test
    public void test_client_interrupted() throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ClientThread(1, rendChannel,10, 5000);
        Thread t2 = new ClientThread(2, rendChannel,20, 5000);
        Thread t3 = new ServerThread(1, rendChannel,5000, 500);

        t1.start();
        Thread.sleep(500);
        t1.interrupt();
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(800);
    }

    @Test
    public void test_server_interrupted() throws InterruptedException {
        RendezvousChannel<Integer,Integer> rendChannel = new RendezvousChannel<Integer,Integer>();
        Thread t1 = new ServerThread(1, rendChannel, 5000, 500);
        Thread t2 = new ServerThread(2, rendChannel, 5000, 500);
        Thread t3 = new ClientThread(1, rendChannel,20, 5000);

        t1.start();
        Thread.sleep(500);
        t1.interrupt();
        t2.start();
        Thread.sleep(500);
        t3.start();
        Thread.sleep(800);
    }


}
